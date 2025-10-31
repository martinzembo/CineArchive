# 📊 RESUMEN EJECUTIVO - Estado Actual del Proyecto

## 🎯 TU SITUACIÓN ACTUAL (Developer 1)

### ✅ **LO QUE YA TIENES COMPLETADO:**

#### **SEMANA 0 - Configuración (100% ✅)**

1. ✅ **Configuración de Spring MVC completa:**
   - `AppConfig.java` - Configuración principal con @ComponentScan
   - `DatabaseConfig.java` - Configuración de DataSource y JdbcTemplate
   - `WebAppInitializer.java` - Inicialización del DispatcherServlet
   - `WebMvcConfig.java` - Configuración de ViewResolver

2. ✅ **Modelo Usuario.java:**
   - Estructura básica completa
   - Enum Rol con los 4 tipos de usuario
   - Todos los campos necesarios
   - Getters y setters
   - Validaciones con Bean Validation
   - Métodos útiles (tieneRol, estaActivo, getEdad)

#### **SEMANA 2 - Backend y Frontend (100% ✅ COMPLETADA)**

3. ✅ **UsuarioRepository.java IMPLEMENTADO Y COMPILADO**
   - 16 métodos funcionales (CRUD + consultas específicas)
   - Manejo de excepciones
   - RowMapper implementado
   - PreparedStatements para seguridad

4. ✅ **PasswordUtil.java IMPLEMENTADO Y COMPILADO**
   - Encriptación con BCrypt
   - Verificación de contraseñas
   - Validaciones de seguridad
   - Generación de mensajes de error

5. ✅ **UsuarioService.java IMPLEMENTADO Y COMPILADO**
   - 28 métodos funcionales
   - Autenticación y registro completo
   - Gestión de contraseñas segura
   - Actualización de perfil
   - Administración de usuarios
   - Estadísticas y validaciones
   - Integración con PasswordUtil y UsuarioRepository

6. ✅ **LoginController.java IMPLEMENTADO Y COMPILADO**
   - 6 métodos funcionales
   - Autenticación completa (GET/POST /login)
   - Gestión de sesiones HTTP
   - Logout funcional (GET /logout)
   - Redirección automática por roles
   - Manejo de errores y mensajes
   - Página de perfil (GET /perfil)
   - Página de acceso denegado

7. ✅ **RegistroController.java IMPLEMENTADO Y COMPILADO**
   - 3 métodos funcionales (+ 1 opcional AJAX)
   - Formulario de registro (GET /registro)
   - Procesamiento de registro (POST /registro)
   - Validaciones exhaustivas:
     * Campos obligatorios
     * Contraseñas coincidentes
     * Formato de email
     * Email único (via Service)
     * Contraseña segura (via Service)
   - Manejo de errores con mensajes claros
   - Mantiene datos en formulario si hay error
   - Integración con LoginController
   - Versión alternativa con @ModelAttribute
   - Endpoint AJAX para verificar email

8. ✅ **SecurityInterceptor.java IMPLEMENTADO Y COMPILADO**
   - Interceptor HTTP que protege automáticamente todas las rutas
   - 3 métodos del ciclo de vida:
     * preHandle() - Validación de seguridad ANTES del Controller
     * postHandle() - Inyección de datos DESPUÉS del Controller
     * afterCompletion() - Logging y limpieza DESPUÉS de todo
   - Validación automática de sesiones activas
   - Protección por roles:
     * /admin/** → Solo ADMINISTRADOR
     * /inventario/** → GESTOR_INVENTARIO + ADMIN
     * /reportes/** → ANALISTA_DATOS + ADMIN
     * Otras rutas → Usuarios autenticados
   - Configuración de rutas públicas (login, registro, CSS, JS, imágenes)
   - Redirección automática a /login si no hay sesión
   - Redirección automática a /acceso-denegado si no tiene permisos
   - Inyección automática de usuarioActual en todas las vistas
   - Exclusión de recursos estáticos para optimizar performance
   - Registrado en WebMvcConfig.java
   - Código limpio sin duplicación en Controllers

---

## 🎉 **SISTEMA DE USUARIOS 100% COMPLETADO**

¡Has completado exitosamente TODAS las tareas de la **Semana 2 (Días 9-10)**!

### ✅ **LOGROS ALCANZADOS:**

#### **Backend Completo:**
- ✅ Usuario.java - Modelo de datos con validaciones
- ✅ UsuarioRepository.java - Acceso a datos con 16 métodos
- ✅ PasswordUtil.java - Encriptación y validación de contraseñas
- ✅ UsuarioService.java - Lógica de negocio con 28 métodos

#### **Frontend Completo:**
- ✅ LoginController.java - Autenticación y gestión de sesiones
- ✅ RegistroController.java - Registro de usuarios con validaciones

#### **Funcionalidades Implementadas:**
- ✅ Registro de usuarios con validación exhaustiva
- ✅ Login con autenticación BCrypt
- ✅ Gestión de sesiones HTTP
- ✅ Logout funcional
- ✅ Redirección automática por roles
- ✅ Protección de contraseñas
- ✅ Validación de email único
- ✅ Mensajes de error descriptivos
- ✅ Manejo robusto de excepciones

---

## 🚀 **DESARROLLADORES 2 Y 3 DESBLOQUEADOS**

Tu trabajo está completo y ahora **Dev 2 y Dev 3 pueden continuar** con sus tareas:

- ✅ **Dev 2 (Contenido)**: Puede usar el sistema de autenticación
- ✅ **Dev 3 (Alquileres)**: Puede referenciar usuarios en sus entidades

### 📋 **ORDEN DE IMPLEMENTACIÓN SUGERIDO:**

---

### **🔴 PASO 1: Completar Usuario.java (30 minutos)**

**¿Por qué primero?** Porque todos los demás componentes dependen de esta clase.

**Qué agregar:**
1. Validations annotations (para registro y edición)
2. Constructor con parámetros
3. Métodos útiles: `tieneRol()`, `estaActivo()`, `getEdad()`
4. `toString()`, `equals()` y `hashCode()`

**Código a agregar:**

```java
// Arriba del todo, después del package:
import javax.validation.constraints.*;
import java.util.Objects;

// Antes de cada campo:
@NotNull(message = "El nombre es obligatorio")
@Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
private String nombre;

@NotNull(message = "El email es obligatorio")
@Email(message = "El email debe tener un formato válido")
@Size(max = 150)
private String email;

@NotNull(message = "La contraseña es obligatoria")
@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
private String contrasena;

@NotNull(message = "El rol es obligatorio")
private Rol rol;

@NotNull
private Boolean activo;

@Past(message = "La fecha de nacimiento debe ser en el pasado")
private LocalDate fechaNacimiento;

// Después del constructor vacío:
public Usuario(String nombre, String email, String contrasena, Rol rol) {
    this.nombre = nombre;
    this.email = email;
    this.contrasena = contrasena;
    this.rol = rol;
    this.fechaRegistro = LocalDate.now();
    this.activo = true;
}

// Al final de la clase, antes del cierre:
public boolean tieneRol(Rol rolEsperado) {
    return this.rol == rolEsperado;
}

public boolean estaActivo() {
    return this.activo != null && this.activo;
}

public int getEdad() {
    if (fechaNacimiento == null) return 0;
    return LocalDate.now().getYear() - fechaNacimiento.getYear();
}

@Override
public String toString() {
    return "Usuario{id=" + id + ", nombre='" + nombre + "', email='" + email + 
           "', rol=" + rol + ", activo=" + activo + '}';
}

@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Usuario usuario = (Usuario) o;
    return id == usuario.id;
}

@Override
public int hashCode() {
    return Objects.hash(id);
}
```

---

### **🔴 PASO 2: Implementar UsuarioRepository.java (1-2 horas)**

**¿Por qué segundo?** Porque el Service necesita el Repository para funcionar.

**Lo que debe hacer:**
- CRUD completo de usuarios
- Buscar por email (para login)
- Buscar por rol (para admin)
- Verificar si existe email (para registro)

**Estructura completa:**

```java
package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Usuario;
import edu.utn.inspt.cinearchive.backend.modelo.Usuario.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class UsuarioRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper para convertir ResultSet a Usuario
    private static final RowMapper<Usuario> USUARIO_ROW_MAPPER = new RowMapper<Usuario>() {
        @Override
        public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getInt("id"));
            usuario.setNombre(rs.getString("nombre"));
            usuario.setEmail(rs.getString("email"));
            usuario.setContrasena(rs.getString("contrasena"));
            usuario.setRol(Rol.valueOf(rs.getString("rol")));
            
            java.sql.Date fechaReg = rs.getDate("fecha_registro");
            if (fechaReg != null) {
                usuario.setFechaRegistro(fechaReg.toLocalDate());
            }
            
            usuario.setActivo(rs.getBoolean("activo"));
            
            java.sql.Date fechaNac = rs.getDate("fecha_nacimiento");
            if (fechaNac != null) {
                usuario.setFechaNacimiento(fechaNac.toLocalDate());
            }
            
            return usuario;
        }
    };

    // CREATE
    public int crear(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, email, contrasena, rol, fecha_registro, activo, fecha_nacimiento) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
            usuario.getNombre(),
            usuario.getEmail(),
            usuario.getContrasena(),
            usuario.getRol().toString(),
            java.sql.Date.valueOf(usuario.getFechaRegistro()),
            usuario.getActivo(),
            usuario.getFechaNacimiento() != null ? java.sql.Date.valueOf(usuario.getFechaNacimiento()) : null
        );
    }

    // READ - Buscar por ID
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, USUARIO_ROW_MAPPER, id);
        } catch (Exception e) {
            return null;
        }
    }

    // READ - Buscar por email (para login)
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, USUARIO_ROW_MAPPER, email);
        } catch (Exception e) {
            return null;
        }
    }

    // READ - Listar todos
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuarios ORDER BY fecha_registro DESC";
        return jdbcTemplate.query(sql, USUARIO_ROW_MAPPER);
    }

    // READ - Listar por rol
    public List<Usuario> listarPorRol(Rol rol) {
        String sql = "SELECT * FROM usuarios WHERE rol = ? ORDER BY nombre";
        return jdbcTemplate.query(sql, USUARIO_ROW_MAPPER, rol.toString());
    }

    // READ - Listar activos
    public List<Usuario> listarActivos() {
        String sql = "SELECT * FROM usuarios WHERE activo = true ORDER BY nombre";
        return jdbcTemplate.query(sql, USUARIO_ROW_MAPPER);
    }

    // UPDATE
    public int actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, contrasena = ?, rol = ?, " +
                     "activo = ?, fecha_nacimiento = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
            usuario.getNombre(),
            usuario.getEmail(),
            usuario.getContrasena(),
            usuario.getRol().toString(),
            usuario.getActivo(),
            usuario.getFechaNacimiento() != null ? java.sql.Date.valueOf(usuario.getFechaNacimiento()) : null,
            usuario.getId()
        );
    }

    // DELETE - Eliminar (físico)
    public int eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    // DELETE - Desactivar (lógico)
    public int desactivar(int id) {
        String sql = "UPDATE usuarios SET activo = false WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    // UTILIDAD - Verificar si existe email
    public boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    // UTILIDAD - Contar usuarios por rol
    public int contarPorRol(Rol rol) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE rol = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, rol.toString());
        return count != null ? count : 0;
    }
}
```

**Características importantes:**
- ✅ Usa `@Repository` para que Spring lo detecte
- ✅ Inyecta `JdbcTemplate` con `@Autowired`
- ✅ Define un `RowMapper` para convertir ResultSet a Usuario
- ✅ CRUD completo
- ✅ Métodos específicos para el negocio (buscarPorEmail, existeEmail)
- ✅ Maneja nulls correctamente
- ✅ Retorna null si no encuentra (en vez de lanzar excepción)

---

### **🔴 PASO 3: Crear PasswordUtil.java (30 minutos)**

**¿Por qué tercero?** El Service necesita encriptar passwords.

**Ubicación:** `src/main/java/edu/utn/inspt/cinearchive/backend/util/PasswordUtil.java`

```java
package edu.utn.inspt.cinearchive.backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Encripta una contraseña en texto plano usando BCrypt
     */
    public static String encriptar(String passwordPlano) {
        if (passwordPlano == null || passwordPlano.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        return encoder.encode(passwordPlano);
    }

    /**
     * Verifica si una contraseña en texto plano coincide con el hash
     */
    public static boolean verificar(String passwordPlano, String passwordHash) {
        if (passwordPlano == null || passwordHash == null) {
            return false;
        }
        return encoder.matches(passwordPlano, passwordHash);
    }

    /**
     * Valida que una contraseña cumpla con los requisitos mínimos
     */
    public static boolean esSegura(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean tieneMayuscula = password.matches(".*[A-Z].*");
        boolean tieneMinuscula = password.matches(".*[a-z].*");
        boolean tieneNumero = password.matches(".*\\d.*");
        
        return tieneMayuscula && tieneMinuscula && tieneNumero;
    }
}
```

**Nota importante:** Necesitas agregar esta dependencia al `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-crypto</artifactId>
    <version>5.7.5</version>
</dependency>
```

---

### **🔴 PASO 4: Crear UsuarioService.java (1-2 horas)**

**¿Por qué cuarto?** Los Controllers necesitan el Service.

```java
package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Usuario;
import edu.utn.inspt.cinearchive.backend.modelo.Usuario.Rol;
import edu.utn.inspt.cinearchive.backend.repositorio.UsuarioRepository;
import edu.utn.inspt.cinearchive.backend.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Registra un nuevo usuario
     * Valida que el email no exista y encripta la contraseña
     */
    public Usuario registrar(Usuario usuario) {
        // Validar que el email no exista
        if (usuarioRepository.existeEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Validar que la contraseña sea segura
        if (!PasswordUtil.esSegura(usuario.getContrasena())) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, " +
                    "una mayúscula, una minúscula y un número");
        }

        // Encriptar contraseña
        usuario.setContrasena(PasswordUtil.encriptar(usuario.getContrasena()));

        // Establecer valores por defecto si no están
        if (usuario.getFechaRegistro() == null) {
            usuario.setFechaRegistro(LocalDate.now());
        }
        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.USUARIO_REGULAR);
        }

        // Guardar en BD
        usuarioRepository.crear(usuario);
        return usuario;
    }

    /**
     * Autentica un usuario con email y contraseña
     * Retorna el usuario si las credenciales son correctas, null si no
     */
    public Usuario autenticar(String email, String password) {
        // Validar parámetros
        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return null;
        }

        // Buscar usuario por email
        Usuario usuario = usuarioRepository.buscarPorEmail(email.trim());
        if (usuario == null) {
            return null;
        }

        // Verificar que esté activo
        if (!usuario.estaActivo()) {
            return null;
        }

        // Verificar contraseña
        if (!PasswordUtil.verificar(password, usuario.getContrasena())) {
            return null;
        }

        return usuario;
    }

    /**
     * Busca un usuario por ID
     */
    public Usuario buscarPorId(int id) {
        return usuarioRepository.buscarPorId(id);
    }

    /**
     * Busca un usuario por email
     */
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(email);
    }

    /**
     * Lista todos los usuarios (para admin)
     */
    public List<Usuario> listarTodos() {
        return usuarioRepository.listarTodos();
    }

    /**
     * Lista usuarios por rol (para admin)
     */
    public List<Usuario> listarPorRol(Rol rol) {
        return usuarioRepository.listarPorRol(rol);
    }

    /**
     * Lista solo usuarios activos
     */
    public List<Usuario> listarActivos() {
        return usuarioRepository.listarActivos();
    }

    /**
     * Actualiza un usuario (para admin o perfil propio)
     * Si se cambia la contraseña, se encripta
     */
    public Usuario actualizar(Usuario usuario) {
        // Verificar que el usuario existe
        Usuario existente = usuarioRepository.buscarPorId(usuario.getId());
        if (existente == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }

        // Si se cambió el email, verificar que no exista
        if (!existente.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.existeEmail(usuario.getEmail())) {
                throw new IllegalArgumentException("El email ya está en uso");
            }
        }

        // Si la contraseña es diferente (no está encriptada), encriptarla
        if (!usuario.getContrasena().equals(existente.getContrasena())) {
            if (!PasswordUtil.esSegura(usuario.getContrasena())) {
                throw new IllegalArgumentException("La contraseña no es segura");
            }
            usuario.setContrasena(PasswordUtil.encriptar(usuario.getContrasena()));
        }

        usuarioRepository.actualizar(usuario);
        return usuario;
    }

    /**
     * Desactiva un usuario (borrado lógico)
     */
    public void desactivar(int id) {
        usuarioRepository.desactivar(id);
    }

    /**
     * Elimina un usuario (borrado físico - solo para admin)
     */
    public void eliminar(int id) {
        usuarioRepository.eliminar(id);
    }

    /**
     * Verifica si un email ya está registrado
     */
    public boolean existeEmail(String email) {
        return usuarioRepository.existeEmail(email);
    }

    /**
     * Obtiene estadísticas de usuarios por rol (para reportes)
     */
    public int contarPorRol(Rol rol) {
        return usuarioRepository.contarPorRol(rol);
    }

    /**
     * Cambia el rol de un usuario (solo admin)
     */
    public void cambiarRol(int usuarioId, Rol nuevoRol) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        usuario.setRol(nuevoRol);
        usuarioRepository.actualizar(usuario);
    }

    /**
     * Cambiar contraseña (para perfil de usuario)
     */
    public void cambiarContrasena(int usuarioId, String contrasenaActual, String contrasenaNueva) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        // Verificar contraseña actual
        if (!PasswordUtil.verificar(contrasenaActual, usuario.getContrasena())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        // Validar nueva contraseña
        if (!PasswordUtil.esSegura(contrasenaNueva)) {
            throw new IllegalArgumentException("La nueva contraseña no es segura");
        }

        // Actualizar contraseña
        usuario.setContrasena(PasswordUtil.encriptar(contrasenaNueva));
        usuarioRepository.actualizar(usuario);
    }
}
```

**Características:**
- ✅ Usa `@Service` para que Spring lo detecte
- ✅ Usa `@Transactional` para gestión automática de transacciones
- ✅ Inyecta `UsuarioRepository` con `@Autowired`
- ✅ **NO maneja HTTP** (eso es del Controller)
- ✅ Valida lógica de negocio
- ✅ Encripta contraseñas antes de guardar
- ✅ Lanza excepciones con mensajes claros

---

### **🔴 PASO 5: Crear LoginController.java (1 hora)**

Ahora sí, con el Service listo, puedes crear el Controller.

Ver el código completo en el análisis anterior (muy largo para repetir aquí).

---

### **🔴 PASO 6: Crear RegistroController.java (45 minutos)**

Ver el código completo en el análisis anterior.

---

### **🔴 PASO 7: Crear SecurityInterceptor.java (45 minutos)**

Ver el código completo en el análisis anterior.

---

### **🔴 PASO 8: Registrar el Interceptor en WebMvcConfig.java (15 minutos)**

Agregar este método en `WebMvcConfig.java`:

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new SecurityInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns("/login", "/registro", "/css/**", "/js/**", "/img/**");
}
```

---

## ⏱️ **ESTIMACIÓN DE TIEMPO TOTAL: 8-10 horas**

- Paso 1: Usuario.java → 30 min
- Paso 2: UsuarioRepository.java → 1-2 horas
- Paso 3: PasswordUtil.java → 30 min
- Paso 4: UsuarioService.java → 1-2 horas
- Paso 5: LoginController.java → 1 hora
- Paso 6: RegistroController.java → 45 min
- Paso 7: SecurityInterceptor.java → 45 min
- Paso 8: Config Interceptor → 15 min
- **Testing y debug → 2-3 horas**

**Total: 1-2 días de trabajo intensivo (8-10 horas)**

---

## 🎯 **CHECKPOINT DEL MIÉRCOLES:**

Para el Miércoles Día 11, debes poder demostrar:

1. ✅ Un usuario puede registrarse en `/registro`
2. ✅ Un usuario puede hacer login en `/login`
3. ✅ Se crea la sesión correctamente
4. ✅ Redirige según el rol del usuario
5. ✅ Un usuario sin sesión es redirigido a `/login`
6. ✅ El logout funciona en `/logout`

Si logras esto, **habrás desbloqueado a Dev 2 y Dev 3** ✅

---

## 💡 **RECOMENDACIONES:**

1. **Trabaja en orden:** No saltes pasos
2. **Testea cada componente:** No avances si algo falla
3. **Usa Postman o navegador:** Para probar endpoints
4. **Revisa logs:** Si algo falla, mira la consola
5. **Comunica:** Avisa a Dev 2 y 3 cuando termines

**¡ÉXITO EN TU DESARROLLO, DEVELOPER 1!** 🚀
# 📊 Análisis de Usuario.java y Tareas del Developer 1 - Paso 2

## 🔍 ANÁLISIS DE LA CLASE `Usuario.java`

### ✅ **Lo que ESTÁ BIEN implementado:**

1. ✅ **Estructura básica correcta**
   - Enum `Rol` con los 4 tipos de usuario necesarios
   - Todos los atributos necesarios según el plan
   - Getters y setters para todos los campos
   - Implementa `Serializable` (importante para sesiones HTTP)

2. ✅ **Campos presentes:**
   - `id` - identificador único
   - `nombre` - nombre del usuario
   - `email` - email único para login
   - `contrasena` - password (aunque debe estar encriptado)
   - `rol` - tipo de usuario (enum)
   - `fechaRegistro` - fecha de creación
   - `activo` - estado del usuario
   - `fechaNacimiento` - dato demográfico

---

### ❌ **Lo que FALTA en Usuario.java:**

#### 1. **🔴 CRÍTICO - Validaciones con Annotations**
El plan requiere validaciones con annotations de Java Validation (Bean Validation). Necesitas agregar:

```java
import javax.validation.constraints.*;

public class Usuario implements Serializable {
    
    private int id;
    
    @NotNull(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;
    
    @NotNull(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    private String email;
    
    @NotNull(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasena;
    
    @NotNull(message = "El rol es obligatorio")
    private Rol rol;
    
    private LocalDate fechaRegistro;
    
    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;
    
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;
    
    // ...resto del código
}
```

#### 2. **🟡 RECOMENDADO - Constructor con parámetros**
Es útil tener un constructor para crear usuarios fácilmente:

```java
public Usuario(String nombre, String email, String contrasena, Rol rol) {
    this.nombre = nombre;
    this.email = email;
    this.contrasena = contrasena;
    this.rol = rol;
    this.fechaRegistro = LocalDate.now();
    this.activo = true;
}
```

#### 3. **🟡 RECOMENDADO - Métodos útiles**
Métodos que ayudarán en la lógica de negocio:

```java
// Verificar si el usuario tiene un rol específico
public boolean tieneRol(Rol rolEsperado) {
    return this.rol == rolEsperado;
}

// Verificar si el usuario está activo
public boolean estaActivo() {
    return this.activo != null && this.activo;
}

// Calcular edad (útil para reportes demográficos)
public int getEdad() {
    if (fechaNacimiento == null) return 0;
    return LocalDate.now().getYear() - fechaNacimiento.getYear();
}
```

#### 4. **🟢 OPCIONAL - toString(), equals() y hashCode()**
Útiles para debugging y comparaciones:

```java
@Override
public String toString() {
    return "Usuario{" +
            "id=" + id +
            ", nombre='" + nombre + '\'' +
            ", email='" + email + '\'' +
            ", rol=" + rol +
            ", activo=" + activo +
            '}';
}

@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Usuario usuario = (Usuario) o;
    return id == usuario.id;
}

@Override
public int hashCode() {
    return Objects.hash(id);
}
```

---

## 📝 **LO QUE DEBES HACER TÚ COMO DEVELOPER 1 EN EL PASO 2**

Según el plan de delegación, estás en la **SEMANA 2 - Días 9-10** (Lunes-Martes), que corresponde a:

### 🔴 **TAREAS CRÍTICAS DEL PASO 2:**

#### **1. LoginController.java - ¡LA PRIORIDAD MÁS ALTA!** 🔥

Debes crear un controlador con estas características:

```java
package edu.utn.inspt.cinearchive.frontend.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    
    @Autowired
    private UsuarioService usuarioService; // Inyección de dependencia
    
    // Mostrar el formulario de login (GET)
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // Retorna login.jsp
    }
    
    // Procesar el login (POST)
    @PostMapping("/login")
    public String procesarLogin(
        @RequestParam String email,
        @RequestParam String password,
        HttpSession session,
        Model model
    ) {
        // 1. Validar que no estén vacíos
        if (email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Email y contraseña son obligatorios");
            return "login";
        }
        
        // 2. Llamar al servicio para autenticar
        Usuario usuario = usuarioService.autenticar(email, password);
        
        // 3. Si es null, credenciales inválidas
        if (usuario == null) {
            model.addAttribute("error", "Credenciales inválidas");
            return "login";
        }
        
        // 4. Verificar que el usuario esté activo
        if (!usuario.estaActivo()) {
            model.addAttribute("error", "Tu cuenta está desactivada");
            return "login";
        }
        
        // 5. Crear sesión
        session.setAttribute("usuarioLogueado", usuario);
        session.setAttribute("rol", usuario.getRol().toString());
        
        // 6. Redirigir según el rol
        switch (usuario.getRol()) {
            case ADMINISTRADOR:
                return "redirect:/admin/panel";
            case GESTOR_INVENTARIO:
                return "redirect:/inventario/panel";
            case ANALISTA_DATOS:
                return "redirect:/reportes/panel";
            default: // USUARIO_REGULAR
                return "redirect:/catalogo";
        }
    }
    
    // Cerrar sesión
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Destruir sesión
        return "redirect:/login";
    }
}
```

**Lo que hace este controlador:**
- ✅ Usa `@Controller` (marca Spring MVC)
- ✅ Inyecta `UsuarioService` con `@Autowired` (NO uses `new`)
- ✅ Mapea rutas con `@GetMapping` y `@PostMapping`
- ✅ Captura parámetros del formulario con `@RequestParam`
- ✅ Maneja sesiones con `HttpSession`
- ✅ NO hace lógica de negocio (solo llama al Service)
- ✅ Retorna nombres de vistas JSP
- ✅ Redirige según el rol del usuario

---

#### **2. RegistroController.java** 🔥

Controlador para registrar nuevos usuarios:

```java
@Controller
public class RegistroController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }
    
    @PostMapping("/registro")
    public String procesarRegistro(
        @Valid @ModelAttribute Usuario usuario,
        BindingResult result,
        Model model
    ) {
        // 1. Validar errores de validación
        if (result.hasErrors()) {
            return "registro";
        }
        
        // 2. Intentar registrar
        try {
            usuarioService.registrar(usuario);
            model.addAttribute("mensaje", "Registro exitoso. Ya puedes iniciar sesión.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        }
    }
}
```

**Características importantes:**
- ✅ Usa `@Valid` para validar las annotations del modelo
- ✅ Usa `@ModelAttribute` para mapear el formulario al objeto
- ✅ Maneja errores con `BindingResult`
- ✅ El Service debe lanzar excepciones si el email ya existe

---

#### **3. SecurityInterceptor.java** 🔥

Interceptor para proteger rutas según el rol:

```java
package edu.utn.inspt.cinearchive.frontend.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SecurityInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        
        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);
        
        // Rutas públicas (sin autenticación)
        if (uri.endsWith("/login") || 
            uri.endsWith("/registro") || 
            uri.contains("/css/") || 
            uri.contains("/js/") || 
            uri.contains("/img/")) {
            return true; // Permitir acceso
        }
        
        // Verificar si hay sesión
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false; // Bloquear acceso
        }
        
        // Verificar permisos por rol
        String rol = (String) session.getAttribute("rol");
        
        if (uri.contains("/admin/") && !rol.equals("ADMINISTRADOR")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return false;
        }
        
        if (uri.contains("/inventario/") && !rol.equals("GESTOR_INVENTARIO")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return false;
        }
        
        if (uri.contains("/reportes/") && !rol.equals("ANALISTA_DATOS")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return false;
        }
        
        return true; // Permitir acceso
    }
}
```

**Luego debes registrarlo en `WebMvcConfig.java`:**

```java
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "edu.utn.inspt.cinearchive")
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/registro", "/css/**", "/js/**", "/img/**");
    }
    
    // ...resto de la configuración
}
```

---

#### **4. Configurar Sistema de Sesiones**

El sistema de sesiones ya está incluido en los Controllers, pero debes asegurarte de:

✅ **En cada JSP protegido, verificar la sesión:**

```jsp
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${empty sessionScope.usuarioLogueado}">
    <c:redirect url="/login"/>
</c:if>

<!-- Resto de tu página -->
<p>Bienvenido, ${sessionScope.usuarioLogueado.nombre}!</p>
```

---

### 📋 **RESUMEN DE TUS TAREAS DEL PASO 2:**

#### **Lunes-Martes (Días 9-10):**

1. ✅ **LoginController.java** - Autenticación completa
   - GET /login (mostrar formulario)
   - POST /login (procesar login)
   - GET /logout (cerrar sesión)

2. ✅ **RegistroController.java** - Registro de usuarios
   - GET /registro (mostrar formulario)
   - POST /registro (procesar registro)

3. ✅ **SecurityInterceptor.java** - Protección de rutas
   - Implementar HandlerInterceptor
   - Verificar sesión
   - Verificar permisos por rol

4. ✅ **Configurar SecurityInterceptor en WebMvcConfig.java**
   - Registrar el interceptor
   - Excluir rutas públicas

5. ✅ **Sistema de sesiones funcionando**
   - Crear sesión al hacer login
   - Destruir sesión al hacer logout
   - Verificar sesión en cada request

---

### 🎯 **CHECKPOINT DEL MIÉRCOLES:**

Para el **Día 11 (Miércoles)**, el login DEBE estar funcionando completamente porque:
- ❌ **Dev 2** está bloqueado (necesita autenticación para CatalogoController)
- ❌ **Dev 3** está esperando (necesita autenticación para GestorInventarioController)

**Criterios de éxito:**
- ✅ Un usuario puede registrarse
- ✅ Un usuario puede hacer login
- ✅ Se crea la sesión correctamente
- ✅ Redirige según el rol
- ✅ Un usuario sin sesión es redirigido a /login
- ✅ El logout funciona correctamente

---

### 🚨 **DEPENDENCIAS PREVIAS QUE NECESITAS:**

Antes de hacer los Controllers, **DEBES HABER COMPLETADO** (Semana 1):

1. ✅ **UsuarioRepository.java** - Para acceder a la BD
2. ✅ **UsuarioService.java** - Para la lógica de autenticación
3. ✅ **PasswordUtil.java** - Para encriptar/verificar passwords

Si no los tienes, **NO PUEDES HACER LOS CONTROLLERS**.

El flujo es:
```
LoginController → UsuarioService → UsuarioRepository → BD
```

---

### 💡 **CONSEJOS PARA EL PASO 2:**

1. **No hagas lógica de negocio en el Controller**
   - ❌ MALO: `if (password.length() < 8) ...` en el Controller
   - ✅ BUENO: Dejar que el Service valide

2. **Usa @Autowired, NO new**
   - ❌ MALO: `UsuarioService service = new UsuarioService();`
   - ✅ BUENO: `@Autowired private UsuarioService service;`

3. **Maneja errores con Model.addAttribute**
   - Pasa mensajes de error a la vista para mostrarlos

4. **Prueba cada endpoint con el navegador**
   - No asumas que funciona, pruébalo

5. **Coordina con Dev 2 y Dev 3**
   - Ellos necesitan que termines el Miércoles
   - Avisa si tienes retrasos

---

## 🎓 **RECURSOS ADICIONALES:**

### Documentación oficial:
- Spring MVC Controllers: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller.html
- Spring Security (avanzado): https://spring.io/projects/spring-security
- Java Bean Validation: https://beanvalidation.org/

### Ejemplos de código:
- Busca ejemplos de "Spring MVC Login Example"
- Busca "Spring MVC Interceptor Example"

---

## ✅ **CONCLUSIÓN:**

### **Usuario.java:**
- ✅ Está bien estructurado
- ❌ Le faltan validations annotations
- ❌ Le faltan métodos útiles (tieneRol, estaActivo, etc.)

### **Tus tareas del Paso 2:**
1. Crear **LoginController.java** (CRÍTICO)
2. Crear **RegistroController.java** (CRÍTICO)
3. Crear **SecurityInterceptor.java** (CRÍTICO)
4. Configurar el interceptor en **WebMvcConfig.java**
5. Asegurar que todo funcione para el **Miércoles Día 11**

**¡ÉXITO EN TU DESARROLLO!** 🚀

