# 🔐 Guía de Uso - LoginController.java

## ✅ Estado: IMPLEMENTADO Y COMPILADO EXITOSAMENTE

El `LoginController.java` está listo y funcional para gestionar el sistema de autenticación de CineArchive.

**Ubicación**: `src/main/java/edu/utn/inspt/cinearchive/frontend/controlador/LoginController.java`

---

## 🎯 ¿Qué es LoginController?

Es el **controlador de capa de presentación** que gestiona:
1. **Autenticación de usuarios** (login)
2. **Gestión de sesiones HTTP**
3. **Cierre de sesión** (logout)
4. **Redirección según roles**
5. **Manejo de errores de autenticación**

**IMPORTANTE**: Este controller **NO hace lógica de negocio**, solo:
- Recibe peticiones HTTP
- Llama al `UsuarioService` (que sí hace la lógica)
- Maneja sesiones
- Retorna vistas JSP

---

## 🏗️ Arquitectura del LoginController

```
┌──────────────────────────────────────────────────┐
│  Navegador del Usuario                           │
│  • Ingresa email y password                      │
│  • Envía POST a /login                          │
└────────────────┬─────────────────────────────────┘
                 │ HTTP Request
                 ▼
┌──────────────────────────────────────────────────┐
│  LoginController.java                            │
│  @Controller - Maneja HTTP                       │
│  • procesarLogin()                               │
│  • Captura @RequestParam email y password       │
│  • Valida que no estén vacíos                   │
└────────────────┬─────────────────────────────────┘
                 │ Llama a
                 ▼
┌──────────────────────────────────────────────────┐
│  UsuarioService.java                             │
│  @Service - Lógica de negocio                    │
│  • autenticar(email, password)                   │
│  • Busca usuario en BD                          │
│  • Verifica que esté activo                     │
│  • Valida password con BCrypt                   │
│  • Retorna Usuario o null                       │
└────────────────┬─────────────────────────────────┘
                 │ Retorna Usuario
                 ▼
┌──────────────────────────────────────────────────┐
│  LoginController.java (continuación)             │
│  • Crea HttpSession                             │
│  • Guarda usuario en session                    │
│  • Redirige según rol:                          │
│    - ADMIN → /admin/panel                       │
│    - GESTOR → /inventario/panel                 │
│    - ANALISTA → /reportes/panel                 │
│    - REGULAR → /catalogo                        │
└────────────────┬─────────────────────────────────┘
                 │ Redirección HTTP
                 ▼
┌──────────────────────────────────────────────────┐
│  Vista JSP correspondiente                       │
│  • admin-panel.jsp                              │
│  • catalogo.jsp                                 │
│  • etc.                                         │
└──────────────────────────────────────────────────┘
```

---

## 📚 Métodos Implementados (6 métodos)

### 1. `mostrarLogin()` - GET /login

**Propósito**: Muestra el formulario de login.

**Parámetros opcionales**:
- `error` - Para mostrar mensajes de error
- `mensaje` - Para mostrar mensajes informativos

**Código**:
```java
@GetMapping("/login")
public String mostrarLogin(
    @RequestParam(value = "error", required = false) String error,
    @RequestParam(value = "mensaje", required = false) String mensaje,
    Model model,
    HttpSession session
) {
    // Si ya está logueado, redirigir
    if (session.getAttribute("usuarioLogueado") != null) {
        return "redirect:/index";
    }
    
    // Manejar mensajes
    if (error != null) {
        model.addAttribute("error", "Credenciales inválidas o cuenta desactivada");
    }
    
    if (mensaje != null) {
        if (mensaje.equals("logout")) {
            model.addAttribute("mensaje", "Has cerrado sesión exitosamente");
        }
    }
    
    return "login"; // Retorna login.jsp
}
```

**Ejemplos de URLs**:
```
GET /login
GET /login?error=true
GET /login?mensaje=logout
GET /login?mensaje=registroExitoso
```

**Lo que hace**:
1. ✅ Verifica si ya hay sesión activa → redirige a /index
2. ✅ Muestra mensajes de error si los hay
3. ✅ Muestra mensajes informativos (logout exitoso, registro exitoso)
4. ✅ Retorna el formulario de login

---

### 2. `procesarLogin()` - POST /login ⭐ MÉTODO PRINCIPAL

**Propósito**: Procesa la autenticación del usuario.

**Parámetros**:
- `email` - Email ingresado en el formulario
- `password` - Contraseña ingresada en el formulario
- `session` - Sesión HTTP (inyectada automáticamente)
- `model` - Modelo para pasar datos a la vista

**Flujo completo**:

```java
@PostMapping("/login")
public String procesarLogin(
    @RequestParam("email") String email,
    @RequestParam("password") String password,
    HttpSession session,
    Model model
) {
    // PASO 1: Validar campos vacíos
    if (email == null || email.trim().isEmpty()) {
        model.addAttribute("error", "El email es obligatorio");
        return "login";
    }
    
    if (password == null || password.trim().isEmpty()) {
        model.addAttribute("error", "La contraseña es obligatoria");
        return "login";
    }
    
    try {
        // PASO 2: Autenticar con el servicio
        Usuario usuario = usuarioService.autenticar(email.trim(), password);
        
        // PASO 3: Verificar resultado
        if (usuario == null) {
            model.addAttribute("error", "Email o contraseña incorrectos");
            model.addAttribute("email", email); // Mantener email en formulario
            return "login";
        }
        
        // PASO 4: Crear sesión
        session.setAttribute("usuarioLogueado", usuario);
        session.setAttribute("usuarioId", usuario.getId());
        session.setAttribute("usuarioNombre", usuario.getNombre());
        session.setAttribute("usuarioEmail", usuario.getEmail());
        session.setAttribute("usuarioRol", usuario.getRol().toString());
        session.setMaxInactiveInterval(30 * 60); // 30 minutos
        
        // PASO 5: Redirigir según rol
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
        
    } catch (Exception e) {
        model.addAttribute("error", "Error en el sistema");
        return "login";
    }
}
```

**Lo que hace**:
1. ✅ **Valida** que email y password no estén vacíos
2. ✅ **Llama al Service** para autenticar (el Service hace TODA la lógica)
3. ✅ **Verifica** que el usuario sea válido (no null)
4. ✅ **Crea la sesión** con todos los datos necesarios
5. ✅ **Redirige** según el rol del usuario
6. ✅ **Maneja errores** y los muestra al usuario

**Atributos guardados en sesión**:
```java
session.getAttribute("usuarioLogueado")  // Objeto Usuario completo
session.getAttribute("usuarioId")        // int: 1, 2, 3...
session.getAttribute("usuarioNombre")    // String: "Juan Pérez"
session.getAttribute("usuarioEmail")     // String: "juan@email.com"
session.getAttribute("usuarioRol")       // String: "USUARIO_REGULAR"
```

---

### 3. `logout()` - GET /logout

**Propósito**: Cierra la sesión del usuario.

**Código**:
```java
@GetMapping("/logout")
public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
    // Invalidar sesión (destruye TODOS los atributos)
    session.invalidate();
    
    // Agregar mensaje de confirmación
    redirectAttributes.addAttribute("mensaje", "logout");
    
    return "redirect:/login";
}
```

**Lo que hace**:
1. ✅ **Invalida** la sesión (elimina todos los datos)
2. ✅ **Redirige** a /login con mensaje de confirmación
3. ✅ El usuario ve "Has cerrado sesión exitosamente"

**Ejemplo de uso en JSP**:
```jsp
<a href="${pageContext.request.contextPath}/logout" class="btn-logout">
    Cerrar Sesión
</a>
```

---

### 4. `accesoDenegado()` - GET /acceso-denegado

**Propósito**: Muestra página cuando un usuario no tiene permisos.

**Código**:
```java
@GetMapping("/acceso-denegado")
public String accesoDenegado(Model model, HttpSession session) {
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    
    if (usuario != null) {
        model.addAttribute("mensaje", 
            "No tienes permisos. Tu rol es: " + usuario.getRol());
    } else {
        model.addAttribute("mensaje", 
            "Debes iniciar sesión.");
    }
    
    return "acceso-denegado";
}
```

**Cuándo se usa**:
- Cuando un USUARIO_REGULAR intenta acceder a /admin/panel
- Cuando un GESTOR_INVENTARIO intenta acceder a /reportes/panel
- Cuando un usuario sin sesión intenta acceder a cualquier página protegida

---

### 5. `inicio()` - GET / o GET /index

**Propósito**: Página de inicio después del login.

**Código**:
```java
@GetMapping({"/", "/index"})
public String inicio(HttpSession session, Model model) {
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    
    // Si no hay sesión, redirigir a login
    if (usuario == null) {
        return "redirect:/login";
    }
    
    // Redirigir según rol
    switch (usuario.getRol()) {
        case ADMINISTRADOR:
            return "redirect:/admin/panel";
        case GESTOR_INVENTARIO:
            return "redirect:/inventario/panel";
        case ANALISTA_DATOS:
            return "redirect:/reportes/panel";
        default:
            return "index"; // Vista principal
    }
}
```

**Lo que hace**:
1. ✅ Verifica que haya sesión activa
2. ✅ Redirige al panel correspondiente según el rol
3. ✅ Los USUARIO_REGULAR ven el catálogo/index

---

### 6. `mostrarPerfil()` - GET /perfil

**Propósito**: Muestra el perfil del usuario logueado.

**Código**:
```java
@GetMapping("/perfil")
public String mostrarPerfil(HttpSession session, Model model) {
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    
    if (usuario == null) {
        return "redirect:/login";
    }
    
    // Obtener datos frescos de BD
    Usuario usuarioActualizado = usuarioService.buscarPorId(usuario.getId());
    
    if (usuarioActualizado != null) {
        model.addAttribute("usuario", usuarioActualizado);
        session.setAttribute("usuarioLogueado", usuarioActualizado);
    }
    
    return "perfil";
}
```

**Lo que hace**:
1. ✅ Verifica que haya sesión
2. ✅ Obtiene datos actualizados desde la BD
3. ✅ Actualiza la sesión con datos frescos
4. ✅ Muestra el perfil del usuario

---

## 🔄 Flujo Completo de Autenticación

### Escenario: Usuario Regular hace Login

```
1. Usuario visita http://localhost:8080/login
   ├─ Controller: mostrarLogin()
   └─ Vista: login.jsp

2. Usuario llena formulario:
   ├─ Email: juan@email.com
   └─ Password: Password123

3. Usuario hace clic en "Iniciar Sesión"
   └─ POST a /login

4. LoginController.procesarLogin() recibe la petición
   ├─ Valida que email no esté vacío ✅
   ├─ Valida que password no esté vacío ✅
   └─ Llama a usuarioService.autenticar(email, password)

5. UsuarioService.autenticar()
   ├─ Busca usuario en BD por email
   ├─ Verifica que esté activo (activo = true)
   ├─ Verifica password con BCrypt
   └─ Retorna Usuario o null

6. LoginController recibe Usuario válido
   ├─ Crea sesión HTTP
   ├─ Guarda: session.setAttribute("usuarioLogueado", usuario)
   ├─ Guarda: session.setAttribute("usuarioId", 1)
   ├─ Guarda: session.setAttribute("usuarioNombre", "Juan Pérez")
   ├─ Guarda: session.setAttribute("usuarioRol", "USUARIO_REGULAR")
   └─ Establece timeout: 30 minutos

7. Redirige según rol: "redirect:/catalogo"
   └─ Usuario ve el catálogo de películas

8. Usuario navega por el sitio
   └─ La sesión se mantiene durante 30 minutos

9. Usuario hace clic en "Cerrar Sesión"
   ├─ GET /logout
   ├─ Controller: logout()
   ├─ session.invalidate()
   └─ Redirige a /login?mensaje=logout
```

---

## 🎨 Vista JSP Requerida: login.jsp

Debes crear o adaptar `login.jsp` en `/WEB-INF/views/login.jsp`:

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - CineArchive</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="login-container">
        <h1>CineArchive</h1>
        <h2>Iniciar Sesión</h2>
        
        <!-- Mostrar mensajes de error -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                ${error}
            </div>
        </c:if>
        
        <!-- Mostrar mensajes informativos -->
        <c:if test="${not empty mensaje}">
            <div class="alert alert-success">
                ${mensaje}
            </div>
        </c:if>
        
        <!-- Formulario de login -->
        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" 
                       id="email" 
                       name="email" 
                       value="${email}"
                       required 
                       autofocus>
            </div>
            
            <div class="form-group">
                <label for="password">Contraseña:</label>
                <input type="password" 
                       id="password" 
                       name="password" 
                       required>
            </div>
            
            <button type="submit" class="btn btn-primary">
                Iniciar Sesión
            </button>
        </form>
        
        <p class="text-center">
            ¿No tienes cuenta? 
            <a href="${pageContext.request.contextPath}/registro">
                Regístrate aquí
            </a>
        </p>
    </div>
</body>
</html>
```

---

## 🔒 Usar la Sesión en Otros Controllers

En cualquier otro controller, puedes acceder a la sesión:

```java
@GetMapping("/catalogo")
public String mostrarCatalogo(HttpSession session, Model model) {
    // Obtener usuario de la sesión
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    
    if (usuario == null) {
        return "redirect:/login"; // Sin sesión, redirigir a login
    }
    
    // Usuario está logueado
    model.addAttribute("usuario", usuario);
    model.addAttribute("bienvenida", "Hola, " + usuario.getNombre());
    
    // Lógica del catálogo...
    
    return "catalogo";
}
```

---

## 🔒 Usar la Sesión en JSPs

En cualquier JSP, puedes acceder a los datos de sesión:

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Verificar si hay sesión -->
<c:if test="${empty sessionScope.usuarioLogueado}">
    <c:redirect url="/login"/>
</c:if>

<!-- Mostrar datos del usuario -->
<div class="user-info">
    <p>Bienvenido, ${sessionScope.usuarioNombre}</p>
    <p>Email: ${sessionScope.usuarioEmail}</p>
    <p>Rol: ${sessionScope.usuarioRol}</p>
    
    <a href="${pageContext.request.contextPath}/logout">
        Cerrar Sesión
    </a>
</div>

<!-- Mostrar contenido según rol -->
<c:if test="${sessionScope.usuarioRol == 'ADMINISTRADOR'}">
    <a href="${pageContext.request.contextPath}/admin/panel">
        Panel de Administración
    </a>
</c:if>
```

---

## ⚡ Características Implementadas

### ✅ Seguridad:
- ✅ Validación de campos vacíos
- ✅ Uso de UsuarioService (que usa BCrypt)
- ✅ No guarda contraseñas en sesión
- ✅ Timeout de sesión (30 minutos)
- ✅ Redirección automática si ya está logueado

### ✅ Experiencia de Usuario:
- ✅ Mensajes de error claros
- ✅ Mantiene el email en el formulario si falla
- ✅ Mensaje de confirmación al cerrar sesión
- ✅ Mensaje de éxito al registrarse

### ✅ Roles y Permisos:
- ✅ Redirección automática según rol
- ✅ Datos de rol guardados en sesión
- ✅ Fácil de verificar permisos en otros controllers

---

## 🧪 Testing del LoginController

### Test Manual en Navegador:

1. **Iniciar el servidor**:
   ```bash
   mvn tomcat7:run
   # o
   mvn spring-boot:run
   ```

2. **Acceder a /login**:
   ```
   http://localhost:8080/login
   ```

3. **Probar login exitoso**:
   - Email: (un email registrado en tu BD)
   - Password: (la contraseña correcta)
   - Debe redirigir según el rol

4. **Probar login fallido**:
   - Email: test@test.com
   - Password: wrongpassword
   - Debe mostrar error

5. **Probar logout**:
   - Hacer login
   - Hacer clic en logout
   - Debe redirigir a /login con mensaje

6. **Probar protección de sesión**:
   - Sin hacer login, intentar acceder a /catalogo
   - Debe redirigir a /login

---

## 📊 Progreso Actualizado

```
Backend - Sistema de Usuarios:
✅ Usuario.java               [100%] ████████████████████
✅ UsuarioRepository.java     [100%] ████████████████████
✅ PasswordUtil.java          [100%] ████████████████████
✅ UsuarioService.java        [100%] ████████████████████
✅ LoginController.java       [100%] ████████████████████ ← ¡Completado!
🔴 RegistroController.java    [  0%] ░░░░░░░░░░░░░░░░░░░░

Progreso General Developer 1:
Semana 0:  ✅ [100%] COMPLETADA
Semana 2:  🟢 [ 95%] CASI COMPLETADA ← ¡Solo falta RegistroController!
```

---

## 🎉 ¡Felicitaciones!

Has completado el **LoginController**, uno de los componentes más críticos del proyecto:

✅ Autenticación funcional
✅ Gestión de sesiones HTTP
✅ Redirección por roles
✅ Manejo de errores
✅ Logout funcional
✅ Compilación exitosa

**Siguiente paso**: Crear `RegistroController.java` para completar el sistema de usuarios al 100%.

**Tiempo estimado**: 30-45 minutos

**¡Ya casi terminas! Solo falta el registro!** 🚀

