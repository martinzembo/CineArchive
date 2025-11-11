# 🗺️ RUTAS DEL PROYECTO CINEARCHIVE

**Base URL:** `http://localhost:8080/cinearchive`

---

## 📋 ÍNDICE

1. [Rutas Públicas (Sin autenticación)](#-rutas-públicas-sin-autenticación)
2. [Rutas de Autenticación](#-rutas-de-autenticación)
3. [Rutas Protegidas (Requieren login)](#-rutas-protegidas-requieren-login)
4. [Rutas por Rol](#-rutas-por-rol)
5. [Rutas de API REST](#-rutas-de-api-rest)
6. [Resumen de Configuración](#-resumen-de-configuración)

---

## 🌐 RUTAS PÚBLICAS (Sin autenticación)

Estas rutas son accesibles sin necesidad de estar logueado.

### 1. **Página de Inicio**
- **URL:** `http://localhost:8080/cinearchive/`
- **URL alternativa:** `http://localhost:8080/cinearchive/index`
- **Método:** `GET`
- **Controlador:** `LoginController.inicio()`
- **Comportamiento:**
  - Si **NO hay sesión** → Redirige a `/login`
  - Si **hay sesión (Usuario Regular)** → Redirige a `/catalogo`
  - Si **hay sesión (Admin/Gestor/Analista)** → Redirige a su panel correspondiente

### 2. **Health Check**
- **URL:** `http://localhost:8080/cinearchive/health`
- **Método:** `GET`
- **Controlador:** `HealthController.health()`
- **Respuesta:** JSON con estado del sistema
- **Uso:** Verificar que el servidor está funcionando

---

## 🔐 RUTAS DE AUTENTICACIÓN

### 1. **Página de Login (GET)**
- **URL:** `http://localhost:8080/cinearchive/login`
- **Método:** `GET`
- **Controlador:** `LoginController.mostrarLogin()`
- **Vista:** `login.jsp`
- **Parámetros opcionales:**
  - `?error` → Muestra mensaje de error
  - `?mensaje=logout` → Muestra "Has cerrado sesión exitosamente"
  - `?mensaje=registroExitoso` → Muestra "¡Registro exitoso! Ya puedes iniciar sesión"
- **Comportamiento:**
  - Si ya hay sesión activa → Redirige a `/index`

### 2. **Procesar Login (POST)**
- **URL:** `http://localhost:8080/cinearchive/login`
- **Método:** `POST`
- **Controlador:** `LoginController.procesarLogin()`
- **Parámetros requeridos:**
  - `email` (String)
  - `password` (String)
- **Respuesta exitosa:**
  - Crea sesión HTTP
  - Redirige según rol:
    - **ADMINISTRADOR** → `/admin/panel`
    - **GESTOR_INVENTARIO** → `/inventario/panel`
    - **ANALISTA_DATOS** → `/reportes/panel`
    - **USUARIO_REGULAR** → `/catalogo`
- **Respuesta con error:**
  - Vuelve a mostrar `login.jsp` con mensaje de error

### 3. **Logout**
- **URL:** `http://localhost:8080/cinearchive/logout`
- **Método:** `GET`
- **Controlador:** `LoginController.logout()`
- **Comportamiento:**
  - Invalida la sesión HTTP
  - Redirige a `/login?mensaje=logout`

### 4. **Página de Registro (GET)**
- **URL:** `http://localhost:8080/cinearchive/registro`
- **Método:** `GET`
- **Controlador:** `RegistroController.mostrarFormularioRegistro()`
- **Vista:** `registro.jsp`

### 5. **Procesar Registro (POST)**
- **URL:** `http://localhost:8080/cinearchive/registro`
- **Método:** `POST`
- **Controlador:** `RegistroController.procesarRegistro()`
- **Parámetros requeridos:**
  - `nombre` (String)
  - `email` (String)
  - `password` (String)
  - `confirmarPassword` (String)
- **Validaciones:**
  - Email único (no duplicado)
  - Contraseña mínimo 8 caracteres
  - Contraseñas coinciden
  - Email válido
- **Respuesta exitosa:**
  - Crea usuario en BD con contraseña encriptada (BCrypt)
  - Redirige a `/login?mensaje=registroExitoso`
- **Respuesta con error:**
  - Vuelve a mostrar `registro.jsp` con mensaje de error

### 6. **Registro Alternativo (POST)**
- **URL:** `http://localhost:8080/cinearchive/registro-alt`
- **Método:** `POST`
- **Controlador:** `RegistroController.procesarRegistroAlternativo()`
- **Uso:** Endpoint alternativo para el registro

### 7. **Verificar Email (API)**
- **URL:** `http://localhost:8080/cinearchive/registro/verificar-email?email={email}`
- **Método:** `GET`
- **Controlador:** `RegistroController.verificarEmail()`
- **Parámetros:**
  - `email` (String) - requerido
- **Respuesta:** JSON
  ```json
  {
    "disponible": true/false,
    "mensaje": "Email disponible" / "Email ya registrado"
  }
  ```
- **Uso:** Validación AJAX en tiempo real del formulario de registro

---

## 🔒 RUTAS PROTEGIDAS (Requieren login)

Estas rutas requieren que el usuario esté autenticado. Si no hay sesión, el `SecurityInterceptor` redirige a `/login`.

### 1. **Catálogo de Contenidos**
- **URL:** `http://localhost:8080/cinearchive/catalogo`
- **URL con búsqueda:** `http://localhost:8080/cinearchive/catalogo?q={query}`
- **Método:** `GET`
- **Controlador:** `CatalogoController.catalogo()`
- **Vista:** `catalogo.jsp`
- **Parámetros opcionales:**
  - `q` (String) - Query de búsqueda
- **Requiere:** Sesión activa (cualquier rol)

### 2. **Detalle de Contenido**
- **URL:** `http://localhost:8080/cinearchive/contenido/{id}`
- **Método:** `GET`
- **Controlador:** `DetalleContenidoController.verDetalle()`
- **Vista:** `detalle.jsp`
- **Parámetros:**
  - `{id}` (Long) - ID del contenido
- **Requiere:** Sesión activa (cualquier rol)

### 3. **Mi Lista (Watchlist)**
- **URL:** `http://localhost:8080/cinearchive/mi-lista`
- **Método:** `GET`
- **Controlador:** `ListaController.verMiLista()`
- **Vista:** `mi-lista.jsp`
- **Requiere:** Sesión activa (Usuario Regular)
- **Descripción:** Muestra la lista de contenidos guardados por el usuario

### 4. **Mis Alquileres**
- **URL:** `http://localhost:8080/cinearchive/mis-alquileres`
- **Método:** `GET`
- **Controlador:** `AlquilerController.verMisAlquileres()`
- **Vista:** Muestra alquileres del usuario
- **Requiere:** Sesión activa (Usuario Regular)

### 5. **Alquilar Contenido (POST)**
- **URL:** `http://localhost:8080/cinearchive/alquilar`
- **Método:** `POST`
- **Controlador:** `AlquilerController.alquilar()`
- **Parámetros requeridos:**
  - `contenidoId` (Long)
  - Otros parámetros de alquiler
- **Requiere:** Sesión activa (Usuario Regular)

### 6. **Perfil de Usuario**
- **URL:** `http://localhost:8080/cinearchive/perfil`
- **Método:** `GET`
- **Controlador:** `LoginController.verPerfil()`
- **Vista:** `perfil.jsp` (pendiente de crear)
- **Requiere:** Sesión activa (cualquier rol)

### 7. **Acceso Denegado**
- **URL:** `http://localhost:8080/cinearchive/acceso-denegado`
- **Método:** `GET`
- **Controlador:** `LoginController.accesoDenegado()`
- **Vista:** `acceso-denegado.jsp` (pendiente de crear)
- **Uso:** Página mostrada cuando un usuario intenta acceder a una ruta sin permisos

---

## 👥 RUTAS POR ROL

### 🔴 ADMINISTRADOR
Rutas accesibles **SOLO** para usuarios con rol `ADMINISTRADOR`.

- **Panel de Administración:**
  - `http://localhost:8080/cinearchive/admin/panel`
  - (Pendiente de implementar)

### 🟡 GESTOR DE INVENTARIO
Rutas accesibles para usuarios con rol `GESTOR_INVENTARIO` y `ADMINISTRADOR`.

- **Panel de Inventario:**
  - `http://localhost:8080/cinearchive/inventario/panel`
  - (Pendiente de implementar)

### 🟢 ANALISTA DE DATOS
Rutas accesibles para usuarios con rol `ANALISTA_DATOS` y `ADMINISTRADOR`.

- **Panel de Reportes:**
  - `http://localhost:8080/cinearchive/reportes/panel`
  - (Pendiente de implementar)

### 🔵 USUARIO REGULAR
Rutas accesibles para **todos** los usuarios autenticados.

- `/catalogo`
- `/contenido/{id}`
- `/mi-lista`
- `/mis-alquileres`
- `/alquilar`
- `/perfil`

---

## 🔌 RUTAS DE API REST

Estas rutas devuelven respuestas JSON (en lugar de vistas HTML).

### 1. **Health Check**
```http
GET http://localhost:8080/cinearchive/health
```
**Respuesta:**
```json
{
  "status": "OK",
  "timestamp": "2025-11-02T19:00:00"
}
```

### 2. **Verificar Email Disponible**
```http
GET http://localhost:8080/cinearchive/registro/verificar-email?email=test@test.com
```
**Respuesta:**
```json
{
  "disponible": false,
  "mensaje": "Email ya registrado"
}
```

### 3. **Encriptar Password (Testing)**
```http
GET http://localhost:8080/cinearchive/test/password/encriptar?password=Test123
```
**Respuesta:**
```json
{
  "password": "Test123",
  "hash": "$2a$12$..."
}
```

### 4. **Verificar Password (Testing)**
```http
GET http://localhost:8080/cinearchive/test/password/verificar?password=Test123&hash=$2a$12$...
```
**Respuesta:**
```json
{
  "password": "Test123",
  "hash": "$2a$12$...",
  "match": true
}
```

---

## ⚙️ RESUMEN DE CONFIGURACIÓN

### Servidor
- **Puerto:** `8080`
- **Contexto:** `/cinearchive`
- **Base URL:** `http://localhost:8080/cinearchive`

### Configuración de Sesión
- **Tiempo de expiración:** 30 minutos
- **Atributos de sesión:**
  - `usuarioLogueado` (Usuario)
  - `usuarioId` (Integer)
  - `usuarioNombre` (String)
  - `usuarioEmail` (String)
  - `usuarioRol` (String)

### Configuración de Seguridad (SecurityInterceptor)

#### Rutas públicas (sin interceptar):
- `/login`
- `/registro`
- `/registro-alt`
- `/registro/verificar-email`
- `/health`
- `/css/**`
- `/js/**`
- `/img/**`
- `/test/**` (solo para desarrollo)

#### Rutas protegidas (requieren login):
- Todas las demás rutas

#### Rutas por rol:
- `/admin/**` → Solo ADMINISTRADOR
- `/inventario/**` → GESTOR_INVENTARIO + ADMINISTRADOR
- `/reportes/**` → ANALISTA_DATOS + ADMINISTRADOR
- Resto → Todos los usuarios autenticados

### Vista Resolver
- **Prefijo:** `/WEB-INF/views/`
- **Sufijo:** `.jsp`
- **Ejemplo:** Vista `"login"` → `/WEB-INF/views/login.jsp`

### Base de Datos
- **URL:** `jdbc:mysql://localhost:3306/cinearchive?useSSL=false&serverTimezone=UTC`
- **Driver:** MySQL Connector/J 8.0.33
- **Pool de conexiones:** HikariCP (configurado en DatabaseConfig)

---

## 📊 MAPA MENTAL DE NAVEGACIÓN

```
┌─────────────────────────────────────────────────────────────┐
│                      http://localhost:8080/cinearchive       │
│                                /                             │
└─────────────────────────────────────────────────────────────┘
                                 │
                    ┌────────────┴────────────┐
                    │                         │
            ¿Tiene sesión?              NO → /login
                    │                         │
                   SÍ                         │
                    │                    (formulario)
          ┌─────────┴─────────┐              │
          │                   │              │
    ¿Qué rol tiene?      POST /login ────────┘
          │              (autenticar)
          │
    ┌─────┴─────┬──────────┬───────────┐
    │           │          │           │
USUARIO_      ADMIN    GESTOR      ANALISTA
REGULAR         │         │            │
    │           │         │            │
    ↓           ↓         ↓            ↓
/catalogo   /admin    /inventario  /reportes
    │       /panel     /panel        /panel
    │
    ├── /contenido/{id} (detalle)
    ├── /mi-lista (watchlist)
    ├── /mis-alquileres
    ├── /alquilar (POST)
    └── /perfil
```

---

## 🎯 CONTROLADORES Y SUS RUTAS

| Controlador | Rutas | Descripción |
|------------|-------|-------------|
| `LoginController` | `/`, `/index`, `/login`, `/logout`, `/acceso-denegado`, `/perfil` | Autenticación y sesiones |
| `RegistroController` | `/registro`, `/registro-alt`, `/registro/verificar-email` | Registro de usuarios |
| `CatalogoController` | `/catalogo` | Lista de contenidos |
| `DetalleContenidoController` | `/contenido/{id}` | Detalles de un contenido |
| `ListaController` | `/mi-lista` | Watchlist del usuario |
| `AlquilerController` | `/mis-alquileres`, `/alquilar` | Gestión de alquileres |
| `HealthController` | `/health` | Health check del sistema |

---

## 🔧 COMANDOS ÚTILES

### Iniciar servidor
```bash
cd C:\Users\Francisco\Desktop\CineArchive
mvn jetty:run
```

### Detener servidor
```
Ctrl + C
```

### Compilar proyecto
```bash
mvn clean compile
```

### Empaquetar proyecto
```bash
mvn clean package
```

### Ver logs en tiempo real
Los logs aparecen en la consola donde ejecutaste `mvn jetty:run`.

---

## 📝 NOTAS IMPORTANTES

### ⚠️ Cambios Recientes
1. **Puerto cambiado:** De `8080` a `8080` (para evitar conflictos)
2. **Contexto configurado:** `/cinearchive` (obligatorio en todas las URLs)
3. **Mapeo ambiguo resuelto:** Eliminado `/` de `CatalogoController`

### ✅ Estado Actual del Proyecto
- ✅ Sistema de autenticación funcionando
- ✅ Registro de usuarios funcionando
- ✅ Control de acceso por roles funcionando
- ✅ Encriptación de contraseñas (BCrypt)
- ✅ Catálogo de contenidos básico
- ⏳ Panels de admin/gestor/analista (pendientes)
- ⏳ Vista de perfil (pendiente)
- ⏳ Vista de acceso denegado (pendiente)

### 🐛 Problemas Conocidos
- Algunos endpoints como `/admin/panel` devuelven 404 porque las vistas JSP no están creadas
- Las rutas de testing (`/test/**`) deberían deshabilitarse en producción

---

**Última actualización:** 2025-11-02  
**Versión:** 1.0  
**Proyecto:** CineArchive V2

