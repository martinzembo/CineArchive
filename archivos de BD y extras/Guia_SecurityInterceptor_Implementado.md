# 🔒 Guía de Uso - SecurityInterceptor.java

## ✅ Estado: IMPLEMENTADO Y COMPILADO EXITOSAMENTE

El `SecurityInterceptor.java` está listo y configurado para proteger automáticamente las rutas de CineArchive según roles de usuario.

**Ubicación**: `src/main/java/edu/utn/inspt/cinearchive/backend/config/SecurityInterceptor.java`

---

## 🎯 ¿Qué es SecurityInterceptor?

Es un **interceptor HTTP de Spring MVC** que:
1. **Intercepta TODAS las peticiones** antes de que lleguen al Controller
2. **Verifica si hay sesión activa** (usuario logueado)
3. **Valida permisos según el rol** del usuario
4. **Redirige automáticamente** si no tiene acceso
5. **Protege rutas sensibles** (admin, inventario, reportes)

**Ventaja**: No necesitas validar sesiones manualmente en cada Controller.

---

## 🏗️ Arquitectura del SecurityInterceptor

```
┌──────────────────────────────────────────────────┐
│  Usuario hace request a cualquier URL            │
│  Ejemplo: GET /admin/panel                       │
└────────────────┬─────────────────────────────────┘
                 │ HTTP Request
                 ▼
┌──────────────────────────────────────────────────┐
│  DispatcherServlet (Spring)                      │
│  Recibe la petición                              │
└────────────────┬─────────────────────────────────┘
                 │
                 ▼
┌──────────────────────────────────────────────────┐
│  SecurityInterceptor.preHandle()                 │
│  ⚠️ SE EJECUTA ANTES DEL CONTROLLER              │
│                                                   │
│  1. ¿Es ruta pública? (login, registro, css)    │
│     → SÍ: PERMITIR (return true)                │
│     → NO: Continuar validación                  │
│                                                   │
│  2. ¿Hay sesión activa?                          │
│     → NO: Redirigir a /login (return false)     │
│     → SÍ: Continuar validación                  │
│                                                   │
│  3. ¿Usuario tiene el rol necesario?            │
│     → NO: Redirigir a /acceso-denegado          │
│     → SÍ: PERMITIR (return true)                │
└────────────────┬─────────────────────────────────┘
                 │ Si return true
                 ▼
┌──────────────────────────────────────────────────┐
│  AdminPanelController.mostrarPanel()             │
│  El controller se ejecuta normalmente            │
└────────────────┬─────────────────────────────────┘
                 │
                 ▼
┌──────────────────────────────────────────────────┐
│  SecurityInterceptor.postHandle()                │
│  Se ejecuta DESPUÉS del controller               │
│  • Agrega datos del usuario al modelo           │
└────────────────┬─────────────────────────────────┘
                 │
                 ▼
┌──────────────────────────────────────────────────┐
│  ViewResolver renderiza la vista                 │
│  admin-panel.jsp con datos del usuario           │
└────────────────┬─────────────────────────────────┘
                 │
                 ▼
┌──────────────────────────────────────────────────┐
│  SecurityInterceptor.afterCompletion()           │
│  Se ejecuta al finalizar todo                    │
│  • Logging opcional                              │
│  • Limpieza de recursos                          │
└──────────────────────────────────────────────────┘
```

---

## 📚 Métodos Implementados

### 1. `preHandle()` - Validación de Seguridad ⭐ PRINCIPAL

**Propósito**: Se ejecuta ANTES de que el Controller procese la petición.

**Flujo de validación**:

```java
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String path = obtenerRutaRelativa(request);
    
    // PASO 1: ¿Es ruta pública?
    if (esRutaPublica(path)) {
        return true; // Permitir sin verificar sesión
    }
    
    // PASO 2: ¿Hay sesión activa?
    HttpSession session = request.getSession(false);
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    
    if (usuario == null) {
        response.sendRedirect("/login");
        return false; // Bloquear
    }
    
    // PASO 3: ¿Usuario tiene permisos para esta ruta?
    if (path.startsWith("/admin") && usuario.getRol() != ADMINISTRADOR) {
        response.sendRedirect("/acceso-denegado");
        return false; // Bloquear
    }
    
    // Usuario autenticado y con permisos
    return true; // Permitir
}
```

**Retorno**:
- `true` → Continúa al Controller
- `false` → Bloquea y redirige

---

### 2. `esRutaPublica()` - Identificar Rutas Públicas

**Propósito**: Determina qué rutas NO requieren autenticación.

**Rutas públicas configuradas**:

```java
RUTAS EXACTAS:
✅ /                      (Home público si existe)
✅ /login                 (Formulario de login)
✅ /registro              (Formulario de registro)
✅ /logout                (Cerrar sesión)
✅ /acceso-denegado       (Página de error)

RUTAS POR PATRÓN:
✅ /registro/verificar-*  (Endpoints AJAX)

RECURSOS ESTÁTICOS:
✅ /css/**                (Hojas de estilo)
✅ /js/**                 (JavaScript)
✅ /img/**                (Imágenes)
✅ /images/**             (Imágenes alternativas)
✅ /fonts/**              (Tipografías)
✅ /disenio/**            (Diseños HTML estáticos)
✅ /resources/**          (Recursos generales)
✅ /static/**             (Archivos estáticos)

EXTENSIONES:
✅ *.css, *.js            (CSS y JavaScript)
✅ *.jpg, *.jpeg, *.png   (Imágenes)
✅ *.gif, *.ico, *.svg    (Íconos y gráficos)
✅ *.woff, *.woff2, *.ttf (Fuentes web)
```

**Ejemplo**:
```java
esRutaPublica("/login");           // true
esRutaPublica("/css/styles.css");  // true
esRutaPublica("/admin/panel");     // false → requiere autenticación
```

---

### 3. Protección por Roles

**Configuración actual**:

#### **Rutas de ADMINISTRADOR**:
```java
Patrón: /admin/**

Permitido:
✅ ADMINISTRADOR

Bloqueado:
❌ USUARIO_REGULAR
❌ GESTOR_INVENTARIO
❌ ANALISTA_DATOS

Ejemplo:
- /admin/panel
- /admin/usuarios
- /admin/configuracion
```

#### **Rutas de GESTOR DE INVENTARIO**:
```java
Patrón: /inventario/**

Permitido:
✅ GESTOR_INVENTARIO
✅ ADMINISTRADOR (tiene acceso a todo)

Bloqueado:
❌ USUARIO_REGULAR
❌ ANALISTA_DATOS

Ejemplo:
- /inventario/panel
- /inventario/agregar-contenido
- /inventario/editar/{id}
```

#### **Rutas de ANALISTA DE DATOS**:
```java
Patrón: /reportes/** o /analytics/**

Permitido:
✅ ANALISTA_DATOS
✅ ADMINISTRADOR

Bloqueado:
❌ USUARIO_REGULAR
❌ GESTOR_INVENTARIO

Ejemplo:
- /reportes/panel
- /reportes/mas-alquilados
- /analytics/demografico
```

#### **Rutas de USUARIO REGULAR**:
```java
Cualquier otra ruta autenticada

Ejemplo:
- /catalogo
- /perfil
- /alquileres/mis-alquileres
- /listas/crear
```

---

### 4. `postHandle()` - Después del Controller

**Propósito**: Se ejecuta DESPUÉS del Controller pero ANTES de renderizar la vista.

**Funcionalidad implementada**:
```java
@Override
public void postHandle(HttpServletRequest request, HttpServletResponse response, 
                      Object handler, ModelAndView modelAndView) {
    
    if (modelAndView != null) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
            
            // Agregar usuario a TODAS las vistas automáticamente
            if (usuario != null) {
                modelAndView.addObject("usuarioActual", usuario);
            }
        }
    }
}
```

**Beneficio**: Todas tus vistas JSP tienen acceso a `${usuarioActual}` automáticamente.

**Uso en JSP**:
```jsp
<div class="user-menu">
    <span>Hola, ${usuarioActual.nombre}</span>
    <span>Rol: ${usuarioActual.rol}</span>
</div>
```

---

### 5. `afterCompletion()` - Después de Todo

**Propósito**: Se ejecuta al FINALIZAR toda la petición (después de renderizar).

**Uso actual**: Logging de errores
```java
@Override
public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                           Object handler, Exception ex) {
    if (ex != null) {
        System.err.println("Error en " + request.getRequestURI() + ": " + ex.getMessage());
    }
}
```

**Usos opcionales**:
- Limpieza de recursos (conexiones, archivos temporales)
- Métricas de rendimiento
- Auditoría de accesos
- Logging detallado

---

## 🔧 Configuración en WebMvcConfig

El interceptor se registra en `WebMvcConfig.java`:

```java
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private SecurityInterceptor securityInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor)
                .addPathPatterns("/**") // Intercepta TODAS las rutas
                .excludePathPatterns(   // Excepto recursos estáticos
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/images/**",
                        "/fonts/**",
                        "/disenio/**",
                        "/resources/**",
                        "/static/**"
                );
    }
}
```

**Explicación**:
- `addPathPatterns("/**")` → Intercepta todo
- `excludePathPatterns(...)` → Excluye recursos estáticos (mejora performance)

---

## 🔄 Flujos de Uso

### Escenario 1: Usuario Regular intenta acceder a /admin/panel

```
1. Usuario ingresa URL: http://localhost:8080/admin/panel

2. SecurityInterceptor.preHandle() se ejecuta:
   ├─ ¿Es ruta pública (/admin/panel)? → NO
   ├─ ¿Hay sesión activa? → SÍ (usuarioLogueado existe)
   ├─ ¿Es ruta /admin/**? → SÍ
   ├─ ¿Usuario es ADMINISTRADOR? → NO (es USUARIO_REGULAR)
   └─ response.sendRedirect("/acceso-denegado")
   └─ return false

3. El Controller AdminPanelController.mostrarPanel() NO se ejecuta

4. Usuario ve la página /acceso-denegado
   "No tienes permisos. Tu rol es: USUARIO_REGULAR"
```

---

### Escenario 2: Usuario sin sesión intenta acceder a /perfil

```
1. Usuario ingresa URL: http://localhost:8080/perfil

2. SecurityInterceptor.preHandle() se ejecuta:
   ├─ ¿Es ruta pública (/perfil)? → NO
   ├─ ¿Hay sesión activa? → NO (session.getAttribute("usuarioLogueado") = null)
   └─ response.sendRedirect("/login")
   └─ return false

3. El Controller NO se ejecuta

4. Usuario es redirigido a /login
```

---

### Escenario 3: Administrador accede a /admin/panel (éxito)

```
1. Administrador ingresa URL: http://localhost:8080/admin/panel

2. SecurityInterceptor.preHandle() se ejecuta:
   ├─ ¿Es ruta pública? → NO
   ├─ ¿Hay sesión activa? → SÍ
   ├─ ¿Es ruta /admin/**? → SÍ
   ├─ ¿Usuario es ADMINISTRADOR? → SÍ ✅
   └─ return true

3. AdminPanelController.mostrarPanel() se ejecuta
   └─ Retorna ModelAndView con datos

4. SecurityInterceptor.postHandle() se ejecuta:
   └─ Agrega usuarioActual al modelo

5. ViewResolver renderiza admin-panel.jsp
   └─ Tiene acceso a ${usuarioActual}

6. SecurityInterceptor.afterCompletion() se ejecuta
   └─ Logging/limpieza

7. Usuario ve el panel de administración
```

---

### Escenario 4: Usuario accede a recursos estáticos

```
1. Navegador solicita: http://localhost:8080/css/styles.css

2. WebMvcConfig excluye /css/** del interceptor
   └─ SecurityInterceptor NO se ejecuta (por performance)

3. Spring sirve el archivo directamente

4. styles.css se descarga sin verificar sesión
```

---

## ⚙️ Personalización del SecurityInterceptor

### Agregar nuevas rutas públicas:

```java
private boolean esRutaPublica(String path) {
    // Agregar tu ruta aquí
    if (path.equals("/mi-ruta-publica") || 
        path.startsWith("/api/publico")) {
        return true;
    }
    
    // ...resto del código
}
```

### Agregar nueva protección por rol:

```java
@Override
public boolean preHandle(...) {
    // ...código existente...
    
    // Nueva protección: Rutas de Soporte Técnico
    if (path.startsWith("/soporte")) {
        if (usuario.getRol() != Usuario.Rol.SOPORTE_TECNICO && 
            usuario.getRol() != Usuario.Rol.ADMINISTRADOR) {
            response.sendRedirect(contextPath + "/acceso-denegado");
            return false;
        }
    }
    
    return true;
}
```

### Agregar logging detallado:

```java
@Override
public void afterCompletion(...) {
    // Logging de todos los accesos
    HttpSession session = request.getSession(false);
    String usuario = "Anónimo";
    
    if (session != null) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        if (u != null) {
            usuario = u.getEmail();
        }
    }
    
    System.out.println(LocalDateTime.now() + 
        " | " + usuario + 
        " | " + request.getMethod() + 
        " | " + request.getRequestURI() +
        " | " + response.getStatus());
}
```

---

## 🧪 Testing del SecurityInterceptor

### Test Manual:

#### 1. **Probar ruta pública sin sesión**:
```
- Acceder a: http://localhost:8080/login
- Resultado esperado: ✅ Se muestra el formulario de login
```

#### 2. **Probar ruta protegida sin sesión**:
```
- Acceder a: http://localhost:8080/catalogo
- Resultado esperado: ✅ Redirige automáticamente a /login
```

#### 3. **Probar ruta de admin como usuario regular**:
```
- Hacer login como USUARIO_REGULAR
- Acceder a: http://localhost:8080/admin/panel
- Resultado esperado: ✅ Redirige a /acceso-denegado
```

#### 4. **Probar ruta de admin como administrador**:
```
- Hacer login como ADMINISTRADOR
- Acceder a: http://localhost:8080/admin/panel
- Resultado esperado: ✅ Muestra el panel de administración
```

#### 5. **Probar recursos estáticos**:
```
- Sin sesión, acceder a: http://localhost:8080/css/styles.css
- Resultado esperado: ✅ Se descarga el archivo sin redirigir
```

---

## ✅ Ventajas del SecurityInterceptor

### Antes (sin interceptor):
```java
@GetMapping("/admin/panel")
public String mostrarPanel(HttpSession session, Model model) {
    // Tienes que hacer esto en CADA método del controller
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    
    if (usuario == null) {
        return "redirect:/login";
    }
    
    if (usuario.getRol() != Usuario.Rol.ADMINISTRADOR) {
        return "redirect:/acceso-denegado";
    }
    
    // Finalmente tu lógica
    model.addAttribute("datos", obtenerDatos());
    return "admin-panel";
}
```

### Ahora (con interceptor):
```java
@GetMapping("/admin/panel")
public String mostrarPanel(Model model) {
    // El interceptor ya validó TODO automáticamente
    // Solo escribes tu lógica
    model.addAttribute("datos", obtenerDatos());
    return "admin-panel";
}
```

**Beneficios**:
- ✅ Código más limpio
- ✅ Menos duplicación
- ✅ Seguridad centralizada
- ✅ Fácil de mantener
- ✅ Difícil de olvidar validaciones

---

## 📊 Matriz de Permisos

| Ruta | USUARIO_REGULAR | GESTOR_INVENTARIO | ANALISTA_DATOS | ADMINISTRADOR |
|------|-----------------|-------------------|----------------|---------------|
| `/login` | ✅ Público | ✅ Público | ✅ Público | ✅ Público |
| `/registro` | ✅ Público | ✅ Público | ✅ Público | ✅ Público |
| `/catalogo` | ✅ Permitido | ✅ Permitido | ✅ Permitido | ✅ Permitido |
| `/perfil` | ✅ Permitido | ✅ Permitido | ✅ Permitido | ✅ Permitido |
| `/admin/**` | ❌ Denegado | ❌ Denegado | ❌ Denegado | ✅ Permitido |
| `/inventario/**` | ❌ Denegado | ✅ Permitido | ❌ Denegado | ✅ Permitido |
| `/reportes/**` | ❌ Denegado | ❌ Denegado | ✅ Permitido | ✅ Permitido |
| `/css/**` | ✅ Público | ✅ Público | ✅ Público | ✅ Público |

---

## 🎉 Resumen

Has implementado exitosamente el **SecurityInterceptor**, que proporciona:

✅ **Protección automática** de todas las rutas
✅ **Validación de sesiones** sin código duplicado
✅ **Control de acceso por roles** centralizado
✅ **Redirección automática** a login o acceso-denegado
✅ **Rutas públicas** configurables
✅ **Datos de usuario** inyectados en todas las vistas
✅ **Performance optimizada** (excluye recursos estáticos)
✅ **Fácil de extender** y personalizar

**Compilación**: ✅ BUILD SUCCESS

**El sistema de seguridad de CineArchive está completo y funcional!** 🔒

---

## 📈 Progreso Final

```
Sistema de Usuarios + Seguridad:
✅ Usuario.java               [100%] ████████████████████
✅ UsuarioRepository.java     [100%] ████████████████████
✅ PasswordUtil.java          [100%] ████████████████████
✅ UsuarioService.java        [100%] ████████████████████
✅ LoginController.java       [100%] ████████████████████
✅ RegistroController.java    [100%] ████████████████████
✅ SecurityInterceptor.java   [100%] ████████████████████ ← ¡Completado!

Total: [100%] ████████████████████ SISTEMA COMPLETO
```

**¡FELICITACIONES! Has completado el sistema de autenticación y seguridad completo!** 🎉

