# 🎯 Guía de Uso - UsuarioService.java

## ✅ Estado: IMPLEMENTADO Y COMPILADO EXITOSAMENTE

La clase `UsuarioService.java` está lista para usar en el proyecto CineArchive.

**Ubicación**: `src/main/java/edu/utn/inspt/cinearchive/backend/servicio/UsuarioService.java`

---

## 🎯 ¿Qué es UsuarioService?

Es la **capa de lógica de negocio** para la gestión de usuarios. Su responsabilidad es:
1. **Validar reglas de negocio** (email único, contraseña segura, etc.)
2. **Orquestar operaciones** entre Repository y Utils
3. **Manejar transacciones** con @Transactional
4. **NO maneja HTTP** (eso es responsabilidad del Controller)

---

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────┐
│  LoginController                                 │
│  @Controller - Maneja HTTP                      │
│  • @GetMapping("/login")                        │
│  • @PostMapping("/login")                       │
└─────────────────┬───────────────────────────────┘
                  │ Llama a
                  ▼
┌─────────────────────────────────────────────────┐
│  UsuarioService                                  │
│  @Service - Lógica de negocio                   │
│  • Valida reglas de negocio                     │
│  • Encripta contraseñas (usa PasswordUtil)      │
│  • Orquesta operaciones                         │
└─────────────────┬───────────────────────────────┘
                  │ Llama a
                  ▼
┌─────────────────────────────────────────────────┐
│  UsuarioRepository                               │
│  @Repository - Acceso a datos                   │
│  • Ejecuta SQL (usa JdbcTemplate)               │
│  • CRUD en tabla usuarios                       │
└─────────────────┬───────────────────────────────┘
                  │ Accede a
                  ▼
┌─────────────────────────────────────────────────┐
│  MySQL Database                                  │
│  Tabla: usuarios                                 │
└─────────────────────────────────────────────────┘
```

---

## 📚 Métodos Disponibles (28 métodos)

### 1️⃣ **AUTENTICACIÓN Y REGISTRO**

#### `registrar(String nombre, String email, String password, Rol rol)` → Usuario

**Propósito**: Registra un nuevo usuario validando email único y contraseña segura.

**Ejemplo en RegistroController**:
```java
@PostMapping("/registro")
public String procesarRegistro(
    @RequestParam String nombre,
    @RequestParam String email,
    @RequestParam String password,
    Model model
) {
    try {
        // UsuarioService hace TODAS las validaciones
        Usuario nuevoUsuario = usuarioService.registrar(
            nombre, 
            email, 
            password, 
            Usuario.Rol.USUARIO_REGULAR
        );
        
        model.addAttribute("mensaje", "¡Registro exitoso! Ya puedes iniciar sesión");
        return "login";
        
    } catch (IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage());
        return "registro";
    }
}
```

**Lo que hace internamente**:
1. ✅ Verifica que el email no exista (lanza excepción si existe)
2. ✅ Valida que la contraseña sea segura (8+ chars, mayúscula, minúscula, número)
3. ✅ Encripta la contraseña con BCrypt
4. ✅ Establece valores por defecto (fechaRegistro, activo=true)
5. ✅ Guarda en BD y retorna el usuario con su ID

---

#### `registrar(Usuario usuario)` → Usuario

**Propósito**: Sobrecarga del método anterior que acepta un objeto Usuario.

**Ejemplo con @ModelAttribute**:
```java
@PostMapping("/registro")
public String procesarRegistro(
    @ModelAttribute Usuario usuario,
    Model model
) {
    try {
        usuarioService.registrar(usuario);
        return "redirect:/login?registroExitoso=true";
    } catch (IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage());
        return "registro";
    }
}
```

---

#### `autenticar(String email, String password)` → Usuario o null

**Propósito**: Autentica un usuario verificando email y contraseña.

**Ejemplo en LoginController**:
```java
@PostMapping("/login")
public String procesarLogin(
    @RequestParam String email,
    @RequestParam String password,
    HttpSession session,
    Model model
) {
    // UsuarioService valida TODO: existe, está activo, password correcta
    Usuario usuario = usuarioService.autenticar(email, password);
    
    if (usuario == null) {
        model.addAttribute("error", "Credenciales inválidas o cuenta desactivada");
        return "login";
    }
    
    // Login exitoso - crear sesión
    session.setAttribute("usuarioLogueado", usuario);
    session.setAttribute("rol", usuario.getRol().toString());
    
    // Redirigir según rol
    switch (usuario.getRol()) {
        case ADMINISTRADOR:
            return "redirect:/admin/panel";
        case GESTOR_INVENTARIO:
            return "redirect:/inventario/panel";
        case ANALISTA_DATOS:
            return "redirect:/reportes/panel";
        default:
            return "redirect:/catalogo";
    }
}
```

**Lo que hace internamente**:
1. ✅ Valida que email y password no estén vacíos
2. ✅ Busca el usuario por email
3. ✅ Verifica que esté activo
4. ✅ Verifica la contraseña con BCrypt
5. ✅ Opcionalmente regenera el hash si es antiguo
6. ✅ Retorna el usuario o null si falla alguna validación

---

### 2️⃣ **GESTIÓN DE CONTRASEÑAS**

#### `cambiarContrasena(int usuarioId, String passwordActual, String passwordNueva)`

**Propósito**: Cambia la contraseña verificando la actual.

**Ejemplo en PerfilController**:
```java
@PostMapping("/perfil/cambiar-password")
public String cambiarPassword(
    @RequestParam String passwordActual,
    @RequestParam String passwordNueva,
    @RequestParam String passwordConfirm,
    HttpSession session,
    RedirectAttributes redirectAttributes
) {
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    
    // Validar que las contraseñas coincidan
    if (!passwordNueva.equals(passwordConfirm)) {
        redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
        return "redirect:/perfil";
    }
    
    try {
        usuarioService.cambiarContrasena(
            usuario.getId(), 
            passwordActual, 
            passwordNueva
        );
        
        redirectAttributes.addFlashAttribute("mensaje", "Contraseña actualizada exitosamente");
        return "redirect:/perfil";
        
    } catch (IllegalArgumentException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/perfil";
    }
}
```

---

#### `restablecerContrasena(int usuarioId, String passwordNueva)`

**Propósito**: Restablece contraseña sin verificar la actual (solo admin).

**Ejemplo en AdminController**:
```java
@PostMapping("/admin/usuarios/{id}/restablecer-password")
public String restablecerPassword(
    @PathVariable int id,
    @RequestParam String passwordNueva,
    RedirectAttributes redirectAttributes
) {
    try {
        usuarioService.restablecerContrasena(id, passwordNueva);
        redirectAttributes.addFlashAttribute("mensaje", 
            "Contraseña restablecida. El usuario debe cambiarla en su próximo login");
        return "redirect:/admin/usuarios";
    } catch (IllegalArgumentException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/admin/usuarios";
    }
}
```

---

### 3️⃣ **ACTUALIZACIÓN DE PERFIL**

#### `actualizarPerfil(int usuarioId, String nombre, String email, LocalDate fechaNacimiento)` → Usuario

**Propósito**: Actualiza datos del perfil (NO la contraseña).

**Ejemplo en PerfilController**:
```java
@PostMapping("/perfil/actualizar")
public String actualizarPerfil(
    @RequestParam String nombre,
    @RequestParam String email,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento,
    HttpSession session,
    RedirectAttributes redirectAttributes
) {
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    
    try {
        Usuario actualizado = usuarioService.actualizarPerfil(
            usuario.getId(), 
            nombre, 
            email, 
            fechaNacimiento
        );
        
        // Actualizar sesión con datos nuevos
        session.setAttribute("usuarioLogueado", actualizado);
        
        redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado exitosamente");
        return "redirect:/perfil";
        
    } catch (IllegalArgumentException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/perfil";
    }
}
```

---

#### `actualizar(Usuario usuario)` → Usuario

**Propósito**: Actualización completa de usuario (para admin).

**Ejemplo en AdminController**:
```java
@PostMapping("/admin/usuarios/{id}/actualizar")
public String actualizarUsuario(
    @PathVariable int id,
    @ModelAttribute Usuario usuario,
    RedirectAttributes redirectAttributes
) {
    usuario.setId(id); // Asegurar que tenga el ID correcto
    
    try {
        usuarioService.actualizar(usuario);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado");
        return "redirect:/admin/usuarios";
    } catch (IllegalArgumentException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/admin/usuarios/" + id + "/editar";
    }
}
```

---

### 4️⃣ **CONSULTAS**

#### `buscarPorId(int id)` → Usuario o null
```java
Usuario usuario = usuarioService.buscarPorId(5);
if (usuario != null) {
    System.out.println("Usuario encontrado: " + usuario.getNombre());
}
```

#### `buscarPorEmail(String email)` → Usuario o null
```java
Usuario usuario = usuarioService.buscarPorEmail("juan@email.com");
```

#### `listarTodos()` → List<Usuario>
```java
List<Usuario> todosUsuarios = usuarioService.listarTodos();
model.addAttribute("usuarios", todosUsuarios);
```

#### `listarPorRol(Rol rol)` → List<Usuario>
```java
List<Usuario> admins = usuarioService.listarPorRol(Usuario.Rol.ADMINISTRADOR);
List<Usuario> gestores = usuarioService.listarPorRol(Usuario.Rol.GESTOR_INVENTARIO);
```

#### `listarActivos()` → List<Usuario>
```java
List<Usuario> usuariosActivos = usuarioService.listarActivos();
```

#### `existeEmail(String email)` → boolean
```java
if (usuarioService.existeEmail("nuevo@email.com")) {
    // Email ya existe
}
```

#### `buscarPorNombre(String nombre)` → List<Usuario>
```java
List<Usuario> resultados = usuarioService.buscarPorNombre("Juan");
```

---

### 5️⃣ **ADMINISTRACIÓN**

#### `desactivar(int id)` → boolean
```java
boolean desactivado = usuarioService.desactivar(5);
if (desactivado) {
    System.out.println("Usuario desactivado");
}
```

#### `activar(int id)` → boolean
```java
boolean activado = usuarioService.activar(5);
```

#### `cambiarEstado(int id, boolean activo)` → boolean
```java
usuarioService.cambiarEstado(5, false); // Desactivar
usuarioService.cambiarEstado(5, true);  // Activar
```

#### `eliminar(int id)` → boolean
```java
// ⚠️ CUIDADO: Elimina físicamente el registro
boolean eliminado = usuarioService.eliminar(5);
```

#### `cambiarRol(int usuarioId, Rol nuevoRol)`
```java
usuarioService.cambiarRol(5, Usuario.Rol.ADMINISTRADOR);
```

---

### 6️⃣ **ESTADÍSTICAS**

#### `contarPorRol(Rol rol)` → int
```java
int totalUsuarios = usuarioService.contarPorRol(Usuario.Rol.USUARIO_REGULAR);
int totalAdmins = usuarioService.contarPorRol(Usuario.Rol.ADMINISTRADOR);
```

#### `contarActivos()` → int
```java
int usuariosActivos = usuarioService.contarActivos();
```

#### `contarTotal()` → int
```java
int totalUsuarios = usuarioService.contarTotal();
```

#### `obtenerEstadisticas()` → String
```java
String estadisticas = usuarioService.obtenerEstadisticas();
System.out.println(estadisticas);
// Salida:
// === Estadísticas de Usuarios ===
// Total usuarios: 50
// Usuarios activos: 45
// 
// Por rol:
// - Usuarios Regulares: 40
// - Administradores: 2
// - Gestores de Inventario: 3
// - Analistas de Datos: 5
```

---

### 7️⃣ **VALIDACIONES**

#### `tieneRol(int usuarioId, Rol rol)` → boolean
```java
if (usuarioService.tieneRol(5, Usuario.Rol.ADMINISTRADOR)) {
    // El usuario 5 es administrador
}
```

#### `estaActivo(int usuarioId)` → boolean
```java
if (usuarioService.estaActivo(5)) {
    // El usuario 5 está activo
}
```

---

## 🎯 Ejemplos Completos de Uso en Controllers

### Ejemplo 1: LoginController Completo

```java
@Controller
public class LoginController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }
    
    @PostMapping("/login")
    public String procesarLogin(
        @RequestParam String email,
        @RequestParam String password,
        HttpSession session,
        Model model
    ) {
        Usuario usuario = usuarioService.autenticar(email, password);
        
        if (usuario == null) {
            model.addAttribute("error", "Credenciales inválidas");
            return "login";
        }
        
        session.setAttribute("usuarioLogueado", usuario);
        session.setAttribute("rol", usuario.getRol().toString());
        
        return "redirect:/catalogo";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
```

---

### Ejemplo 2: RegistroController Completo

```java
@Controller
public class RegistroController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }
    
    @PostMapping("/registro")
    public String procesarRegistro(
        @RequestParam String nombre,
        @RequestParam String email,
        @RequestParam String password,
        @RequestParam String passwordConfirm,
        Model model
    ) {
        // Validar que las contraseñas coincidan
        if (!password.equals(passwordConfirm)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "registro";
        }
        
        try {
            usuarioService.registrar(nombre, email, password, Usuario.Rol.USUARIO_REGULAR);
            model.addAttribute("mensaje", "¡Registro exitoso!");
            return "login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        }
    }
}
```

---

### Ejemplo 3: AdminUsuariosController

```java
@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuariosController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping
    public String listar(Model model) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("estadisticas", usuarioService.obtenerEstadisticas());
        return "admin/usuarios/lista";
    }
    
    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable int id, RedirectAttributes ra) {
        if (usuarioService.desactivar(id)) {
            ra.addFlashAttribute("mensaje", "Usuario desactivado");
        } else {
            ra.addFlashAttribute("error", "Usuario no encontrado");
        }
        return "redirect:/admin/usuarios";
    }
    
    @PostMapping("/{id}/cambiar-rol")
    public String cambiarRol(
        @PathVariable int id, 
        @RequestParam String rol,
        RedirectAttributes ra
    ) {
        try {
            Usuario.Rol nuevoRol = Usuario.Rol.valueOf(rol);
            usuarioService.cambiarRol(id, nuevoRol);
            ra.addFlashAttribute("mensaje", "Rol actualizado");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }
}
```

---

## 🔒 Características de Seguridad Implementadas

### ✅ Lo que UsuarioService HACE automáticamente:

1. **Validación de email único**
   - Verifica en BD antes de registrar
   - Lanza excepción si ya existe

2. **Validación de contraseña segura**
   - Mínimo 8 caracteres
   - Al menos una mayúscula
   - Al menos una minúscula
   - Al menos un número

3. **Encriptación automática**
   - Usa BCrypt con 12 rondas
   - Salt único por contraseña
   - Hash diferente cada vez

4. **Verificación de estado activo**
   - No permite login a usuarios desactivados
   - Protege cuentas suspendidas

5. **Regeneración de hashes antiguos**
   - Detecta hashes con factor de trabajo bajo
   - Los actualiza automáticamente en el login

---

## ⚠️ Errores Comunes a Evitar

### ❌ NO hacer esto:
```java
// MALO: Crear instancia con new
UsuarioService service = new UsuarioService(); // ❌
```

### ✅ Hacer esto:
```java
// BIEN: Inyectar con @Autowired
@Autowired
private UsuarioService usuarioService; // ✅
```

---

### ❌ NO hacer esto:
```java
// MALO: Encriptar en el Controller
String hash = PasswordUtil.encriptar(password);
usuario.setContrasena(hash);
usuarioService.registrar(usuario);
```

### ✅ Hacer esto:
```java
// BIEN: El Service encripta automáticamente
usuarioService.registrar(nombre, email, password, rol); // ✅
```

---

### ❌ NO hacer esto:
```java
// MALO: Validar en el Controller
if (!email.contains("@")) {
    return "error";
}
if (password.length() < 8) {
    return "error";
}
```

### ✅ Hacer esto:
```java
// BIEN: El Service valida automáticamente
try {
    usuarioService.registrar(nombre, email, password, rol);
} catch (IllegalArgumentException e) {
    model.addAttribute("error", e.getMessage());
}
```

---

## 🎉 ¡Listo para Usar!

El `UsuarioService` está **completamente implementado** con:

✅ **28 métodos funcionales**
✅ **Validaciones automáticas**
✅ **Encriptación de contraseñas**
✅ **Manejo de transacciones**
✅ **Gestión completa de usuarios**
✅ **Estadísticas y reportes**
✅ **Documentación Javadoc**
✅ **Compilación exitosa**

**Siguiente paso**: Crear los **Controllers** (LoginController, RegistroController) que usen este Service.

---

## 📊 Progreso Actual

```
Backend - Sistema de Usuarios:
✅ Usuario.java               [100%] ████████████████████
✅ UsuarioRepository.java     [100%] ████████████████████
✅ PasswordUtil.java          [100%] ████████████████████
✅ UsuarioService.java        [100%] ████████████████████
🔴 LoginController.java       [  0%] ░░░░░░░░░░░░░░░░░░░░
🔴 RegistroController.java    [  0%] ░░░░░░░░░░░░░░░░░░░░

Progreso General Developer 1:
Semana 0:  ✅ [100%] COMPLETADA
Semana 2:  🟡 [ 80%] EN PROGRESO
```

**¡Vas muy bien! Solo faltan los Controllers para completar el sistema de usuarios!** 🚀

