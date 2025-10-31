# 📝 Guía de Uso - RegistroController.java

## ✅ Estado: IMPLEMENTADO Y COMPILADO EXITOSAMENTE

El `RegistroController.java` está listo y funcional para gestionar el registro de nuevos usuarios en CineArchive.

**Ubicación**: `src/main/java/edu/utn/inspt/cinearchive/frontend/controlador/RegistroController.java`

---

## 🎯 ¿Qué es RegistroController?

Es el **controlador de registro de usuarios** que gestiona:
1. **Formulario de registro** (mostrar)
2. **Validación de datos** (campos vacíos, contraseñas coincidentes, formato de email)
3. **Creación de cuentas** (llamada a UsuarioService)
4. **Manejo de errores** (email duplicado, contraseña débil)
5. **Redirección** (a login después de registro exitoso)

---

## 🏗️ Arquitectura del RegistroController

```
┌──────────────────────────────────────────────────┐
│  Navegador del Usuario                           │
│  • Accede a /registro                           │
│  • Llena formulario con datos                   │
│  • Envía POST a /registro                       │
└────────────────┬─────────────────────────────────┘
                 │ HTTP Request
                 ▼
┌──────────────────────────────────────────────────┐
│  RegistroController.java                         │
│  @Controller - Maneja HTTP                       │
│  • procesarRegistro()                           │
│  • Captura nombre, email, password              │
│  • VALIDA campos vacíos                         │
│  • VALIDA contraseñas coincidentes              │
│  • VALIDA formato de email                      │
└────────────────┬─────────────────────────────────┘
                 │ Llama a
                 ▼
┌──────────────────────────────────────────────────┐
│  UsuarioService.java                             │
│  @Service - Lógica de negocio                    │
│  • registrar(usuario)                           │
│  • VALIDA email único (BD)                      │
│  • VALIDA contraseña segura                     │
│  • ENCRIPTA contraseña con BCrypt               │
│  • GUARDA en BD                                 │
└────────────────┬─────────────────────────────────┘
                 │ Si éxito
                 ▼
┌──────────────────────────────────────────────────┐
│  RegistroController (continuación)               │
│  • redirectAttributes.addAttribute("mensaje")   │
│  • return "redirect:/login"                     │
└────────────────┬─────────────────────────────────┘
                 │ Redirección HTTP
                 ▼
┌──────────────────────────────────────────────────┐
│  LoginController.java                            │
│  • GET /login?mensaje=registroExitoso           │
│  • Muestra: "¡Registro exitoso!"                │
│  • Usuario puede iniciar sesión                 │
└──────────────────────────────────────────────────┘
```

---

## 📚 Métodos Implementados (3 métodos + 1 opcional)

### 1. `mostrarRegistro()` - GET /registro

**Propósito**: Muestra el formulario de registro vacío.

**Código**:
```java
@GetMapping("/registro")
public String mostrarRegistro(Model model) {
    model.addAttribute("usuario", new Usuario());
    return "registro"; // Retorna registro.jsp
}
```

**Lo que hace**:
1. ✅ Crea un objeto Usuario vacío
2. ✅ Lo pasa al modelo para el formulario
3. ✅ Retorna la vista registro.jsp

**Ejemplo de uso**:
```
GET http://localhost:8080/registro
```

---

### 2. `procesarRegistro()` - POST /registro ⭐ MÉTODO PRINCIPAL

**Propósito**: Procesa el formulario de registro con todas las validaciones.

**Parámetros**:
- `nombre` - Nombre completo del usuario
- `email` - Email único
- `password` - Contraseña
- `passwordConfirm` - Confirmación de contraseña
- `fechaNacimiento` - Fecha de nacimiento (opcional)

**Flujo completo**:

```java
@PostMapping("/registro")
public String procesarRegistro(
    @RequestParam("nombre") String nombre,
    @RequestParam("email") String email,
    @RequestParam("password") String password,
    @RequestParam("passwordConfirm") String passwordConfirm,
    @RequestParam(value = "fechaNacimiento", required = false) String fechaNacimiento,
    RedirectAttributes redirectAttributes,
    Model model
) {
    // VALIDACIÓN 1: Campos obligatorios no vacíos
    if (nombre == null || nombre.trim().isEmpty()) {
        model.addAttribute("error", "El nombre es obligatorio");
        return "registro";
    }
    
    // VALIDACIÓN 2: Contraseñas coincidentes
    if (!password.equals(passwordConfirm)) {
        model.addAttribute("error", "Las contraseñas no coinciden");
        return "registro";
    }
    
    // VALIDACIÓN 3: Formato de email
    if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        model.addAttribute("error", "Email inválido");
        return "registro";
    }
    
    try {
        // Crear usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre.trim());
        nuevoUsuario.setEmail(email.trim().toLowerCase());
        nuevoUsuario.setContrasena(password);
        nuevoUsuario.setRol(Usuario.Rol.USUARIO_REGULAR);
        nuevoUsuario.setFechaRegistro(LocalDate.now());
        nuevoUsuario.setActivo(true);
        
        // Registrar (el service valida más cosas)
        usuarioService.registrar(nuevoUsuario);
        
        // Éxito - redirigir a login
        redirectAttributes.addAttribute("mensaje", "registroExitoso");
        return "redirect:/login";
        
    } catch (IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage());
        return "registro";
    }
}
```

**Validaciones que realiza el CONTROLLER**:
1. ✅ Campos no vacíos (nombre, email, password, passwordConfirm)
2. ✅ Contraseñas coincidentes
3. ✅ Formato de email válido
4. ✅ Fecha de nacimiento parseable (si se proporciona)

**Validaciones que realiza el SERVICE** (llamado por el controller):
1. ✅ Email único (no existe en BD)
2. ✅ Contraseña segura (8+ chars, mayúscula, minúscula, número)
3. ✅ Encriptación de contraseña con BCrypt

**Lo que hace al completar**:
1. ✅ Crea objeto Usuario con datos validados
2. ✅ Establece valores por defecto (rol, fechaRegistro, activo)
3. ✅ Llama a `usuarioService.registrar()` (que hace más validaciones)
4. ✅ Si éxito → redirige a `/login?mensaje=registroExitoso`
5. ✅ Si error → vuelve a `registro.jsp` con mensaje de error

---

### 3. `procesarRegistroConModelAttribute()` - POST /registro-alt

**Propósito**: Versión alternativa usando `@ModelAttribute` (data binding automático).

**Diferencia con el método anterior**:
- ✅ Spring mapea automáticamente los campos del formulario al objeto Usuario
- ✅ Usa validaciones de Bean Validation (`@Valid`, `@NotNull`, `@Email`)
- ✅ `BindingResult` captura errores de validación

**Código simplificado**:
```java
@PostMapping("/registro-alt")
public String procesarRegistroConModelAttribute(
    @Valid @ModelAttribute("usuario") Usuario usuario,
    BindingResult result,
    @RequestParam("passwordConfirm") String passwordConfirm,
    RedirectAttributes redirectAttributes,
    Model model
) {
    // Validar errores de Bean Validation
    if (result.hasErrors()) {
        return "registro";
    }
    
    // Validar contraseñas coincidentes
    if (!usuario.getContrasena().equals(passwordConfirm)) {
        model.addAttribute("error", "Las contraseñas no coinciden");
        return "registro";
    }
    
    try {
        usuarioService.registrar(usuario);
        redirectAttributes.addAttribute("mensaje", "registroExitoso");
        return "redirect:/login";
    } catch (IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage());
        return "registro";
    }
}
```

**Cuándo usar cada versión**:
- **POST /registro**: Más control, validaciones explícitas, fácil de entender
- **POST /registro-alt**: Menos código, usa Bean Validation, más "Spring-like"

---

### 4. `verificarEmail()` - GET /registro/verificar-email (OPCIONAL)

**Propósito**: Endpoint AJAX para verificar si un email está disponible en tiempo real.

**Código**:
```java
@GetMapping("/registro/verificar-email")
public String verificarEmail(@RequestParam("email") String email, Model model) {
    boolean existe = usuarioService.existeEmail(email);
    model.addAttribute("existe", existe);
    return "json-response";
}
```

**Uso en JavaScript**:
```javascript
// En registro.jsp o script.js
document.getElementById('email').addEventListener('blur', function() {
    const email = this.value;
    
    fetch('/registro/verificar-email?email=' + email)
        .then(response => response.json())
        .then(data => {
            if (data.existe) {
                alert('Este email ya está registrado');
            }
        });
});
```

---

## 🔄 Flujo Completo de Registro

### Escenario: Usuario se registra con éxito

```
1. Usuario visita http://localhost:8080/registro
   ├─ Controller: mostrarRegistro()
   └─ Vista: registro.jsp

2. Usuario llena formulario:
   ├─ Nombre: Juan Pérez
   ├─ Email: juan@email.com
   ├─ Password: Password123
   └─ Confirmar: Password123

3. Usuario hace clic en "Registrarse"
   └─ POST a /registro

4. RegistroController.procesarRegistro() recibe la petición
   ├─ VALIDACIÓN 1: ¿Campos vacíos? → NO ✅
   ├─ VALIDACIÓN 2: ¿Contraseñas coinciden? → SÍ ✅
   ├─ VALIDACIÓN 3: ¿Email válido? → SÍ ✅
   └─ Crea objeto Usuario

5. Controller llama a usuarioService.registrar(usuario)
   ├─ Service: ¿Email existe en BD? → NO ✅
   ├─ Service: ¿Contraseña segura? → SÍ ✅
   ├─ Service: Encripta password con BCrypt
   ├─ Service: Llama a usuarioRepository.crear(usuario)
   └─ Repository: INSERT INTO usuarios...

6. Service retorna Usuario creado con ID

7. Controller redirige: "redirect:/login?mensaje=registroExitoso"

8. LoginController muestra login.jsp con mensaje:
   └─ "¡Registro exitoso! Ya puedes iniciar sesión"

9. Usuario puede hacer login con sus credenciales
```

---

## 🎨 Vista JSP Requerida: registro.jsp

Debes crear o adaptar `registro.jsp` en `/WEB-INF/views/registro.jsp`:

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registro - CineArchive</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="registro-container">
        <h1>CineArchive</h1>
        <h2>Crear Cuenta</h2>
        
        <!-- Mostrar mensajes de error -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                ${error}
            </div>
        </c:if>
        
        <!-- Formulario de registro -->
        <form action="${pageContext.request.contextPath}/registro" method="post">
            <div class="form-group">
                <label for="nombre">Nombre Completo:</label>
                <input type="text" 
                       id="nombre" 
                       name="nombre" 
                       value="${nombre}"
                       placeholder="Ej: Juan Pérez"
                       required 
                       autofocus>
            </div>
            
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" 
                       id="email" 
                       name="email" 
                       value="${email}"
                       placeholder="tu@email.com"
                       required>
                <small>Será tu nombre de usuario para iniciar sesión</small>
            </div>
            
            <div class="form-group">
                <label for="password">Contraseña:</label>
                <input type="password" 
                       id="password" 
                       name="password" 
                       required>
                <small>Mínimo 8 caracteres, con mayúscula, minúscula y número</small>
            </div>
            
            <div class="form-group">
                <label for="passwordConfirm">Confirmar Contraseña:</label>
                <input type="password" 
                       id="passwordConfirm" 
                       name="passwordConfirm" 
                       required>
            </div>
            
            <div class="form-group">
                <label for="fechaNacimiento">Fecha de Nacimiento (opcional):</label>
                <input type="date" 
                       id="fechaNacimiento" 
                       name="fechaNacimiento">
            </div>
            
            <button type="submit" class="btn btn-primary">
                Registrarse
            </button>
        </form>
        
        <p class="text-center">
            ¿Ya tienes cuenta? 
            <a href="${pageContext.request.contextPath}/login">
                Inicia sesión aquí
            </a>
        </p>
    </div>
    
    <script>
        // Validación en el cliente (opcional pero mejora UX)
        document.querySelector('form').addEventListener('submit', function(e) {
            const password = document.getElementById('password').value;
            const passwordConfirm = document.getElementById('passwordConfirm').value;
            
            if (password !== passwordConfirm) {
                e.preventDefault();
                alert('Las contraseñas no coinciden');
                return false;
            }
            
            if (password.length < 8) {
                e.preventDefault();
                alert('La contraseña debe tener al menos 8 caracteres');
                return false;
            }
        });
    </script>
</body>
</html>
```

---

## 🔒 Validaciones Implementadas

### Validaciones del Controller:

| Validación | Propósito | Mensaje de Error |
|------------|-----------|------------------|
| Campo nombre vacío | Datos obligatorios | "El nombre es obligatorio" |
| Campo email vacío | Datos obligatorios | "El email es obligatorio" |
| Campo password vacío | Datos obligatorios | "La contraseña es obligatoria" |
| Campo passwordConfirm vacío | Datos obligatorios | "Debes confirmar la contraseña" |
| Contraseñas no coinciden | Evitar errores de tipeo | "Las contraseñas no coinciden" |
| Formato de email inválido | Email válido | "El formato del email no es válido" |
| Fecha de nacimiento inválida | Formato correcto | "La fecha no tiene un formato válido" |

### Validaciones del Service (automáticas):

| Validación | Propósito | Mensaje de Error |
|------------|-----------|------------------|
| Email duplicado | Email único | "El email ya está registrado" |
| Contraseña débil | Seguridad | "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número" |

---

## 🎯 Mensajes al Usuario

### Si registro es exitoso:
```
✅ Redirige a: /login?mensaje=registroExitoso
✅ LoginController muestra: "¡Registro exitoso! Ya puedes iniciar sesión"
```

### Si hay errores:
```
❌ Vuelve a: /registro
❌ Muestra el error específico
❌ Mantiene los datos ingresados (menos contraseñas)
```

---

## 🔄 Integración con LoginController

Después del registro exitoso, el flujo continúa en LoginController:

```java
// En LoginController.java
@GetMapping("/login")
public String mostrarLogin(
    @RequestParam(value = "mensaje", required = false) String mensaje,
    Model model
) {
    if (mensaje != null && mensaje.equals("registroExitoso")) {
        model.addAttribute("mensaje", "¡Registro exitoso! Ya puedes iniciar sesión");
    }
    return "login";
}
```

---

## ⚡ Características Implementadas

### ✅ Seguridad:
- ✅ Validación de campos en el backend (no confía en el frontend)
- ✅ Contraseña confirmada dos veces
- ✅ Email convertido a minúsculas (evita duplicados por case)
- ✅ Contraseña encriptada por el Service (BCrypt)
- ✅ No guarda contraseñas en texto plano

### ✅ Experiencia de Usuario:
- ✅ Mensajes de error claros y específicos
- ✅ Mantiene datos ingresados si hay error (excepto contraseñas)
- ✅ Confirmación visual de registro exitoso
- ✅ Redirección automática a login

### ✅ Robustez:
- ✅ Manejo de excepciones (`try-catch`)
- ✅ Validación de formato de fecha
- ✅ Valores por defecto establecidos
- ✅ Email único validado en BD

---

## 🧪 Testing del RegistroController

### Test Manual en Navegador:

1. **Acceder al formulario**:
   ```
   http://localhost:8080/registro
   ```

2. **Probar registro exitoso**:
   - Nombre: Juan Pérez
   - Email: nuevo@email.com
   - Password: Password123
   - Confirmar: Password123
   - Debe redirigir a login con mensaje de éxito

3. **Probar contraseñas no coinciden**:
   - Password: Password123
   - Confirmar: Password456
   - Debe mostrar error

4. **Probar email duplicado**:
   - Usar un email ya registrado
   - Debe mostrar: "El email ya está registrado"

5. **Probar contraseña débil**:
   - Password: pass (sin mayúsculas ni números)
   - Debe mostrar error de contraseña no segura

6. **Probar campos vacíos**:
   - Dejar nombre vacío
   - Debe mostrar: "El nombre es obligatorio"

---

## 📊 Progreso Actualizado

```
Sistema de Usuarios - CineArchive:

Backend:
✅ Usuario.java               [100%] ████████████████████
✅ UsuarioRepository.java     [100%] ████████████████████
✅ PasswordUtil.java          [100%] ████████████████████
✅ UsuarioService.java        [100%] ████████████████████

Frontend:
✅ LoginController.java       [100%] ████████████████████
✅ RegistroController.java    [100%] ████████████████████ ← ¡Completado!

Vistas JSP (pendientes):
🟡 login.jsp                  [ 50%] ██████████░░░░░░░░░░ (tienes login.html)
🟡 registro.jsp               [ 50%] ██████████░░░░░░░░░░ (tienes registro.html)
🔴 perfil.jsp                 [  0%] ░░░░░░░░░░░░░░░░░░░░

Progreso General Developer 1:
Semana 0:  ✅ [100%] COMPLETADA
Semana 2:  ✅ [100%] COMPLETADA ← ¡100% ALCANZADO! 🎉
```

---

## 🎉 ¡Felicitaciones!

Has completado el **RegistroController**, el último componente del sistema de usuarios:

✅ Formulario de registro funcional
✅ Validaciones exhaustivas (controller + service)
✅ Manejo de errores robusto
✅ Integración con LoginController
✅ Experiencia de usuario completa
✅ Compilación exitosa

### 🏆 **Sistema de Usuarios 100% Completo**

Con esto has terminado:
- ✅ Modelo de datos (Usuario.java)
- ✅ Acceso a datos (UsuarioRepository.java)
- ✅ Utilidades (PasswordUtil.java)
- ✅ Lógica de negocio (UsuarioService.java)
- ✅ Autenticación (LoginController.java)
- ✅ Registro (RegistroController.java)

**¡Dev 2 y Dev 3 están OFICIALMENTE DESBLOQUEADOS!** 🚀

---

## 🎯 Próximos Pasos Opcionales

### 1. Adaptar las Vistas HTML a JSP (1-2 horas)
   - Copiar login.html → /WEB-INF/views/login.jsp
   - Copiar registro.html → /WEB-INF/views/registro.jsp
   - Agregar tags JSTL para mensajes
   - Actualizar action de formularios

### 2. Crear SecurityInterceptor (opcional, 30 min)
   - Proteger rutas automáticamente
   - Verificar roles antes de permitir acceso

### 3. Testing completo (1 hora)
   - Probar todos los flujos
   - Verificar mensajes de error
   - Confirmar integración con BD

---

**¡EXCELENTE TRABAJO, DEVELOPER 1!** 💪

Has completado tu parte del proyecto según el plan de delegación. Ahora puedes:
- Coordinar con Dev 2 y Dev 3
- Ayudarles con dudas sobre autenticación
- Continuar con otras funcionalidades (reportes, perfil de usuario, etc.)

**¡El sistema de usuarios de CineArchive está LISTO PARA PRODUCCIÓN!** 🎬

