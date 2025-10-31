# 🔐 Guía de Uso - PasswordUtil.java

## ✅ Estado: IMPLEMENTADO Y COMPILADO EXITOSAMENTE

La clase `PasswordUtil.java` está lista para usar en el proyecto CineArchive.

**Ubicación**: `src/main/java/edu/utn/inspt/cinearchive/backend/util/PasswordUtil.java`

---

## 🎯 ¿Qué es PasswordUtil?

Es una **clase de utilidad** que proporciona métodos estáticos para:
1. **Encriptar contraseñas** con BCrypt
2. **Verificar contraseñas** contra hashes almacenados
3. **Validar requisitos de seguridad** de contraseñas
4. **Generar mensajes de validación** para feedback al usuario

---

## 🔧 Tecnología Usada: BCrypt

### ¿Por qué BCrypt?
- ✅ **Salt automático**: Genera un salt único para cada contraseña
- ✅ **Hash adaptativo**: Lento por diseño (previene fuerza bruta)
- ✅ **Diferente cada vez**: Mismo password → diferentes hashes
- ✅ **Estándar de la industria**: Usado por Google, Facebook, GitHub, etc.

### Ejemplo de Hash BCrypt:
```
Password: "MiPassword123"
Hash 1:   "$2a$12$K5v7vWxq3pJ1Y9Nh8..."  ← Diferente
Hash 2:   "$2a$12$X8k2mPqr5tL4Z2Bm7..."  ← Diferente
```
Aunque sean del mismo password, BCrypt genera hashes únicos.

---

## 📚 Métodos Disponibles

### 1. `encriptar(String passwordPlano)` → String

**Propósito**: Convierte una contraseña en texto plano a un hash seguro de BCrypt.

**Cuándo usar**: 
- Al **registrar un nuevo usuario**
- Al **cambiar la contraseña** de un usuario

**Ejemplo de uso en UsuarioService**:
```java
@Service
public class UsuarioService {
    
    public Usuario registrar(String nombre, String email, String password, Usuario.Rol rol) {
        // 1. Validar que la contraseña sea segura
        if (!PasswordUtil.esSegura(password)) {
            throw new IllegalArgumentException("La contraseña no cumple los requisitos de seguridad");
        }
        
        // 2. Encriptar la contraseña
        String hashPassword = PasswordUtil.encriptar(password);
        
        // 3. Crear usuario con la contraseña encriptada
        Usuario usuario = new Usuario(nombre, email, hashPassword, rol);
        
        // 4. Guardar en BD
        return usuarioRepository.crear(usuario);
    }
}
```

**Resultado**:
```java
String password = "MiPassword123";
String hash = PasswordUtil.encriptar(password);
// hash = "$2a$12$K5v7vWxq3pJ1Y9Nh8NZ.5OuX8L2f3g4h5i6j7k8l9m0n1o2p3q4r5"
//         └──┘ └─┘ └──────────────────────┘ └──────────────────────────┘
//          │    │           │                           │
//      Algoritmo │         Salt                   Hash de la password
//              Rounds (12)
```

**⚠️ IMPORTANTE**:
- NUNCA guardes contraseñas en texto plano en la BD
- SIEMPRE encripta antes de guardar
- El hash resultante tiene **60 caracteres** (tu columna BD debe ser VARCHAR(255))

---

### 2. `verificar(String passwordPlano, String passwordHash)` → boolean

**Propósito**: Verifica si una contraseña en texto plano coincide con un hash.

**Cuándo usar**: 
- Durante el **login** para autenticar usuarios
- Al **cambiar contraseña** para verificar la contraseña actual

**Ejemplo de uso en UsuarioService (Login)**:
```java
@Service
public class UsuarioService {
    
    public Usuario autenticar(String email, String password) {
        // 1. Buscar usuario por email
        Usuario usuario = usuarioRepository.buscarPorEmail(email);
        
        if (usuario == null) {
            return null; // Usuario no existe
        }
        
        // 2. Verificar que esté activo
        if (!usuario.estaActivo()) {
            return null; // Cuenta desactivada
        }
        
        // 3. Verificar contraseña
        String hashGuardadoEnBD = usuario.getContrasena(); // "$2a$12$K5v7..."
        boolean passwordCorrecta = PasswordUtil.verificar(password, hashGuardadoEnBD);
        
        if (!passwordCorrecta) {
            return null; // Contraseña incorrecta
        }
        
        // 4. Todo OK, retornar usuario autenticado
        return usuario;
    }
}
```

**Resultado**:
```java
String passwordIngresada = "MiPassword123";
String hashEnBD = "$2a$12$K5v7vWxq3pJ1Y9Nh8NZ.5OuX8L2f3g4h5i6j7k8l9m0n1o2p3q4r5";

boolean esCorrecta = PasswordUtil.verificar(passwordIngresada, hashEnBD);
// esCorrecta = true  → Login exitoso ✅
// esCorrecta = false → Credenciales inválidas ❌
```

**⚠️ IMPORTANTE**:
- NUNCA compares contraseñas con `equals()` o `==`
- BCrypt maneja la comparación de forma segura
- Retorna `false` si el hash está corrupto o no es válido

---

### 3. `esSegura(String password)` → boolean

**Propósito**: Valida que una contraseña cumpla requisitos **básicos** de seguridad.

**Requisitos validados**:
- ✅ Mínimo 8 caracteres
- ✅ Al menos una letra mayúscula (A-Z)
- ✅ Al menos una letra minúscula (a-z)
- ✅ Al menos un número (0-9)

**Cuándo usar**: 
- Antes de **registrar un usuario**
- Antes de **cambiar contraseña**

**Ejemplo de uso en UsuarioService**:
```java
@Service
public class UsuarioService {
    
    public Usuario registrar(Usuario usuario, String password) {
        // Validar antes de encriptar
        if (!PasswordUtil.esSegura(password)) {
            throw new IllegalArgumentException(
                "La contraseña debe tener al menos 8 caracteres, " +
                "una mayúscula, una minúscula y un número"
            );
        }
        
        usuario.setContrasena(PasswordUtil.encriptar(password));
        return usuarioRepository.crear(usuario);
    }
}
```

**Resultados de ejemplo**:
```java
PasswordUtil.esSegura("Password1");      // ✅ true  (cumple todos)
PasswordUtil.esSegura("password1");      // ❌ false (falta mayúscula)
PasswordUtil.esSegura("PASSWORD1");      // ❌ false (falta minúscula)
PasswordUtil.esSegura("Password");       // ❌ false (falta número)
PasswordUtil.esSegura("Pass1");          // ❌ false (menos de 8 caracteres)
PasswordUtil.esSegura("MiPasswordSegura123"); // ✅ true
```

---

### 4. `esMuySegura(String password)` → boolean

**Propósito**: Valida que una contraseña cumpla requisitos **FUERTES** de seguridad.

**Requisitos validados**:
- ✅ Mínimo 10 caracteres (no 8)
- ✅ Al menos una letra mayúscula
- ✅ Al menos una letra minúscula
- ✅ Al menos un número
- ✅ Al menos un carácter especial (!@#$%^&*()_+-=[]{}|;:,.<>?)

**Cuándo usar**: 
- Para **cuentas de administrador**
- Para **sistemas que requieren alta seguridad**

**Ejemplo de uso**:
```java
@Service
public class UsuarioService {
    
    public Usuario crearAdministrador(String nombre, String email, String password) {
        // Los admins necesitan contraseñas más fuertes
        if (!PasswordUtil.esMuySegura(password)) {
            throw new IllegalArgumentException(
                "Los administradores requieren contraseñas fuertes: " +
                "mínimo 10 caracteres, con mayúsculas, minúsculas, " +
                "números y caracteres especiales"
            );
        }
        
        String hash = PasswordUtil.encriptar(password);
        Usuario admin = new Usuario(nombre, email, hash, Usuario.Rol.ADMINISTRADOR);
        return usuarioRepository.crear(admin);
    }
}
```

**Resultados de ejemplo**:
```java
PasswordUtil.esMuySegura("Password1!");       // ✅ true  (cumple todos)
PasswordUtil.esMuySegura("Password1");        // ❌ false (falta especial)
PasswordUtil.esMuySegura("Pass1!");           // ❌ false (menos de 10 chars)
PasswordUtil.esMuySegura("MiPassword123@");   // ✅ true
PasswordUtil.esMuySegura("SuperSecure#2024"); // ✅ true
```

---

### 5. `obtenerMensajeValidacion(String password)` → String

**Propósito**: Genera un mensaje descriptivo de lo que le falta a una contraseña.

**Cuándo usar**: 
- Para mostrar **feedback** al usuario durante el registro
- Para mejorar la **experiencia de usuario**

**Ejemplo de uso en LoginController**:
```java
@Controller
public class RegistroController {
    
    @PostMapping("/registro")
    public String procesarRegistro(
        @RequestParam String password,
        Model model
    ) {
        // Validar y obtener mensaje descriptivo
        String mensajeError = PasswordUtil.obtenerMensajeValidacion(password);
        
        if (!mensajeError.isEmpty()) {
            model.addAttribute("error", mensajeError);
            return "registro"; // Volver al formulario con el mensaje
        }
        
        // Contraseña válida, proceder con el registro
        // ...
    }
}
```

**Resultados de ejemplo**:
```java
PasswordUtil.obtenerMensajeValidacion("pass");
// "La contraseña debe tener al menos 8 caracteres. Debe contener al menos una mayúscula. Debe contener al menos un número."

PasswordUtil.obtenerMensajeValidacion("password");
// "La contraseña debe tener al menos 8 caracteres. Debe contener al menos una mayúscula. Debe contener al menos un número."

PasswordUtil.obtenerMensajeValidacion("PASSWORD123");
// "Debe contener al menos una letra minúscula."

PasswordUtil.obtenerMensajeValidacion("Password1");
// "" (vacío, porque es válida)
```

**Uso en JSP**:
```jsp
<form action="${pageContext.request.contextPath}/registro" method="post">
    <input type="password" name="password" id="password" required>
    
    <!-- Mostrar mensaje de error si existe -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    
    <button type="submit">Registrarse</button>
</form>
```

---

### 6. `necesitaRegenerar(String passwordHash)` → boolean

**Propósito**: Verifica si un hash necesita ser regenerado (cuando se aumenta el factor de trabajo).

**Cuándo usar**: 
- Durante el **login**, para actualizar hashes antiguos
- En **tareas de mantenimiento** periódico

**Ejemplo de uso**:
```java
@Service
public class UsuarioService {
    
    public Usuario autenticar(String email, String password) {
        Usuario usuario = usuarioRepository.buscarPorEmail(email);
        
        if (usuario == null) return null;
        
        // Verificar contraseña
        if (!PasswordUtil.verificar(password, usuario.getContrasena())) {
            return null;
        }
        
        // Si el hash es antiguo, regenerarlo
        if (PasswordUtil.necesitaRegenerar(usuario.getContrasena())) {
            String nuevoHash = PasswordUtil.encriptar(password);
            usuario.setContrasena(nuevoHash);
            usuarioRepository.actualizar(usuario);
        }
        
        return usuario;
    }
}
```

---

## 🚀 Ejemplos Completos de Uso

### Ejemplo 1: Registro de Usuario

```java
@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public Usuario registrarUsuario(String nombre, String email, String password, Usuario.Rol rol) {
        // 1. Verificar que el email no exista
        if (usuarioRepository.existeEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        
        // 2. Validar requisitos de seguridad de la contraseña
        if (!PasswordUtil.esSegura(password)) {
            String mensaje = PasswordUtil.obtenerMensajeValidacion(password);
            throw new IllegalArgumentException(mensaje);
        }
        
        // 3. Encriptar la contraseña
        String hashPassword = PasswordUtil.encriptar(password);
        
        // 4. Crear el usuario
        Usuario usuario = new Usuario(nombre, email, hashPassword, rol);
        
        // 5. Guardar en BD
        return usuarioRepository.crear(usuario);
    }
}
```

---

### Ejemplo 2: Login de Usuario

```java
@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public Usuario autenticar(String email, String password) {
        // 1. Validar parámetros
        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return null;
        }
        
        // 2. Buscar usuario por email
        Usuario usuario = usuarioRepository.buscarPorEmail(email.trim());
        if (usuario == null) {
            return null; // Usuario no existe
        }
        
        // 3. Verificar que esté activo
        if (!usuario.estaActivo()) {
            return null; // Cuenta desactivada
        }
        
        // 4. Verificar contraseña con BCrypt
        if (!PasswordUtil.verificar(password, usuario.getContrasena())) {
            return null; // Contraseña incorrecta
        }
        
        // 5. Login exitoso
        return usuario;
    }
}
```

---

### Ejemplo 3: Cambio de Contraseña

```java
@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public void cambiarContrasena(int usuarioId, String passwordActual, String passwordNueva) {
        // 1. Buscar usuario
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        
        // 2. Verificar contraseña actual
        if (!PasswordUtil.verificar(passwordActual, usuario.getContrasena())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }
        
        // 3. Validar nueva contraseña
        if (!PasswordUtil.esSegura(passwordNueva)) {
            String mensaje = PasswordUtil.obtenerMensajeValidacion(passwordNueva);
            throw new IllegalArgumentException(mensaje);
        }
        
        // 4. No permitir usar la misma contraseña
        if (PasswordUtil.verificar(passwordNueva, usuario.getContrasena())) {
            throw new IllegalArgumentException("La nueva contraseña debe ser diferente a la actual");
        }
        
        // 5. Encriptar y actualizar
        String nuevoHash = PasswordUtil.encriptar(passwordNueva);
        usuario.setContrasena(nuevoHash);
        usuarioRepository.actualizar(usuario);
    }
}
```

---

## 🔒 Consideraciones de Seguridad

### ✅ BUENAS PRÁCTICAS:

1. **NUNCA guardes contraseñas en texto plano**
   ```java
   // ❌ MAL
   usuario.setContrasena(password);
   
   // ✅ BIEN
   usuario.setContrasena(PasswordUtil.encriptar(password));
   ```

2. **NUNCA compares contraseñas con ==**
   ```java
   // ❌ MAL
   if (password.equals(usuario.getContrasena())) { ... }
   
   // ✅ BIEN
   if (PasswordUtil.verificar(password, usuario.getContrasena())) { ... }
   ```

3. **NUNCA muestres contraseñas en logs**
   ```java
   // ❌ MAL
   System.out.println("Password: " + password);
   
   // ✅ BIEN
   System.out.println("Password: ********");
   ```

4. **SIEMPRE valida antes de encriptar**
   ```java
   // ✅ BIEN
   if (!PasswordUtil.esSegura(password)) {
       throw new IllegalArgumentException("Contraseña no segura");
   }
   String hash = PasswordUtil.encriptar(password);
   ```

5. **Usa HTTPS en producción**
   - Las contraseñas viajan en texto plano desde el navegador
   - HTTPS encripta la comunicación

---

## 📊 Configuración de la Base de Datos

Tu tabla `usuarios` debe tener una columna para la contraseña:

```sql
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,  -- ⚠️ IMPORTANTE: VARCHAR(255) para el hash
    rol ENUM('USUARIO_REGULAR', 'ADMINISTRADOR', 'GESTOR_INVENTARIO', 'ANALISTA_DATOS'),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    fecha_nacimiento DATE
);
```

**⚠️ IMPORTANTE**: 
- El hash BCrypt tiene **60 caracteres**
- Usa `VARCHAR(255)` para tener margen de seguridad
- Nunca uses `VARCHAR(50)` o el hash se truncará

---

## 🎯 Integración con UsuarioService

Tu `UsuarioService` debería usar `PasswordUtil` de esta forma:

```java
@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    // Registro
    public Usuario registrar(Usuario usuario, String passwordPlano) {
        if (!PasswordUtil.esSegura(passwordPlano)) {
            throw new IllegalArgumentException(
                PasswordUtil.obtenerMensajeValidacion(passwordPlano)
            );
        }
        usuario.setContrasena(PasswordUtil.encriptar(passwordPlano));
        return usuarioRepository.crear(usuario);
    }
    
    // Login
    public Usuario autenticar(String email, String password) {
        Usuario usuario = usuarioRepository.buscarPorEmail(email);
        if (usuario == null || !usuario.estaActivo()) {
            return null;
        }
        return PasswordUtil.verificar(password, usuario.getContrasena()) ? usuario : null;
    }
}
```

---

## ⚡ Rendimiento

### Factor de Trabajo (BCRYPT_ROUNDS = 12)

BCrypt es **lento por diseño** para prevenir ataques de fuerza bruta:

| Rounds | Tiempo por hash |
|--------|----------------|
| 10     | ~150 ms        |
| 12     | ~600 ms        | ← **Actual (recomendado)**
| 14     | ~2.4 seg       |
| 16     | ~9.6 seg       |

**12 rondas** es el balance perfecto:
- ✅ Seguro contra ataques de fuerza bruta
- ✅ Rápido para usuarios legítimos (< 1 segundo)

---

## 🧪 Testing de PasswordUtil

Puedes crear tests unitarios:

```java
@Test
public void testEncriptarYVerificar() {
    String password = "MiPassword123";
    String hash = PasswordUtil.encriptar(password);
    
    assertTrue(PasswordUtil.verificar(password, hash));
    assertFalse(PasswordUtil.verificar("OtraPassword", hash));
}

@Test
public void testValidacionSeguridad() {
    assertTrue(PasswordUtil.esSegura("Password1"));
    assertFalse(PasswordUtil.esSegura("password"));
    assertFalse(PasswordUtil.esSegura("Pass1"));
}

@Test
public void testMensajeValidacion() {
    String mensaje = PasswordUtil.obtenerMensajeValidacion("pass");
    assertTrue(mensaje.contains("8 caracteres"));
    assertTrue(mensaje.contains("mayúscula"));
}
```

---

## ✅ Checklist de Implementación

- [x] PasswordUtil.java creado
- [x] Dependencia BCrypt en pom.xml
- [x] Compilación exitosa
- [ ] Integrado en UsuarioService
- [ ] Usado en LoginController
- [ ] Usado en RegistroController
- [ ] Tabla usuarios con VARCHAR(255)
- [ ] Tests unitarios creados

---

## 🎉 ¡Listo para Usar!

La clase `PasswordUtil` está **completamente implementada** y lista para:

✅ Registrar usuarios con contraseñas seguras
✅ Autenticar usuarios en el login
✅ Cambiar contraseñas de forma segura
✅ Validar requisitos de seguridad
✅ Proporcionar feedback al usuario

**Siguiente paso**: Implementar `UsuarioService.java` usando esta utilidad.

