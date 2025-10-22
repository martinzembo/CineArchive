# 📋 Plan de Delegación de Desarrollo - CineArchive V2
## ⚡ RÉGIMEN INTENSIVO - 4 SEMANAS

## 🏗️ Arquitectura Frontend/Backend (Corrección del Profesor)

Tu profesor pidió **separar claramente el FRONT del BACK**, eliminando la mezcla de responsabilidades. Esta es la arquitectura que seguiremos:

### Estructura de Capas:
```
┌─────────────────────────────────────────────────────────┐
│  FRONTEND (Presentación HTTP)                           │
│  └─ Servlets (ej: LoginServlet.java)                   │
│     • Reciben requests HTTP                             │
│     • Validan entrada básica                            │
│     • Llaman a Services del BACKEND                     │
│     • Manejan sesiones y redirecciones                  │
│     • Renderizan JSP o retornan JSON                    │
└─────────────────────────────────────────────────────────┘
                            ↓ Llama a
┌─────────────────────────────────────────────────────────┐
│  BACKEND (Lógica de Negocio Pura)                       │
│  ├─ Services (ej: UsuarioService.java)                │
│  │  • Validaciones complejas de negocio                 │
│  │  • Orquestación de operaciones                       │
│  │  • **NO manejan HTTP**                               │
│  │  • Llaman a Repositorios                             │
│  │                                                       │
│  └─ Repositorios (ej: UsuarioRepositorio.java)         │
│     • CRUD con la Base de Datos                         │
│     • Queries SQL                                       │
│     • Gestión de conexiones                             │
└─────────────────────────────────────────────────────────┘
                            ↓ Accede a
┌─────────────────────────────────────────────────────────┐
│  BASE DE DATOS (MySQL)                                  │
│  └─ Tablas, Views, Stored Procedures                   │
└─────────────────────────────────────────────────────────┘
```

### Flujo de una Petición HTTP:
```
1. Usuario entra en login.jsp
2. Llena formulario y hace POST a /LoginServlet
3. LoginServlet recibe la petición
   └─ Valida que no esté vacío
   └─ Llama: usuarioService.autenticar(email, password)
4. UsuarioService (BACKEND)
   └─ Valida formato de email
   └─ Llama: usuarioRepositorio.buscarPorEmail(email)
5. UsuarioRepositorio (BACKEND)
   └─ Ejecuta: SELECT * FROM usuarios WHERE email = ?
   └─ Retorna: Usuario object (o null)
6. UsuarioService valida password
   └─ Retorna: Usuario autenticado
7. LoginServlet recibe Usuario
   └─ Crea sesión
   └─ Redirige a index.jsp
```

### ✅ Lo Correcto:
- ✅ LoginServlet → Llama a UsuarioService → Llama a UsuarioRepositorio → BD
- ✅ Services NO manejan HTTP
- ✅ Repositorios SOLO acceden a BD
- ✅ Servlets SOLO hacen HTTP handling

### ❌ Lo Incorrecto:
- ❌ Servlet accediendo directamente a BD
- ❌ Lógica de negocio esparcida en Servlets
- ❌ Repositorio siendo llamado desde múltiples Servlets sin orquestar por Service

---

## 🎯 Resumen del Proyecto
**CineArchive** es una aplicación web Java (Maven + JSP) para alquilar y gestionar películas/series con 4 tipos de usuarios:
- Usuario Regular (alquila contenido)
- Administrador (gestiona usuarios y políticas)
- Gestor de Inventario (gestiona catálogo)
- Analista de Datos (genera reportes)

---

## 👥 División de Trabajo por Desarrollador

### 🔵 **DEVELOPER 1 (CHAMA) - Backend: Gestión de Usuarios y Autenticación**

#### Responsabilidades Core:
1. **Capa de Modelo - Usuarios y Seguridad** ✅ BACKEND
   - Completar clase `Usuario.java` con validaciones
   - Implementar sistema de encriptación de contraseñas
   - Crear DTOs para diferentes roles

2. **Capa de Repositorio - Acceso a Datos** ✅ BACKEND
   - Crear Repositorio para Usuario (`UsuarioRepositorio.java`)
   - Implementar CRUD completo de usuarios
   - Gestión de roles y permisos en BD
   - Conexión con `ConexionBD.java` (clase compartida)

3. **Capa de Servicio - Lógica de Negocio** ✅ BACKEND
   - Crear Servicio para Usuario (`UsuarioService.java`)
   - Validaciones de negocio (email único, password fuerte, etc.)
   - Orquestación de operaciones (encriptación, registro, búsqueda)
   - **NO maneja HTTP ni sesiones**

4. **Controladores - Frontend** 🌐 FRONTEND
   - Servlet de Login (`LoginServlet.java`)
   - Servlet de Registro (`RegistroServlet.java`)
   - Servlet de gestión de usuarios para Admin (`AdminUsuariosServlet.java`)
   - Sistema de sesiones y filtros de seguridad (`SecurityFilter.java`)
   - **Solo reciben HTTP, llaman a Services, manejan sesiones**

5. **Base de Datos - Tabla Usuarios**
   - Script SQL para crear tabla `usuarios`
   - Datos de prueba (seeders) - mínimo 10 usuarios de cada rol
   - Stored procedures si es necesario

6. **Frontend - Vistas de Autenticación**
   - Conectar `login.html` con backend
   - Conectar `registro.html` con backend
   - Validaciones JavaScript del lado del cliente
   - Panel de gestión de usuarios en `admin-panel.html`

#### Archivos a crear/modificar:
```
✅ src/main/java/edu/utn/inspt/cinearchive/backend/modelo/Usuario.java (completar)
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/repositorio/UsuarioRepositorio.java
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/servicio/UsuarioService.java
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/repositorio/ConexionBD.java (clase compartida)
🆕 src/main/java/edu/utn/inspt/cinearchive/frontend/controlador/LoginServlet.java
🆕 src/main/java/edu/utn/inspt/cinearchive/frontend/controlador/RegistroServlet.java
🆕 src/main/java/edu/utn/inspt/cinearchive/frontend/controlador/AdminUsuariosServlet.java
🆕 src/main/java/edu/utn/inspt/cinearchive/frontend/filtro/SecurityFilter.java
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/util/PasswordUtil.java
🆕 src/main/resources/db/01_usuarios.sql
📝 src/main/webapp/disenio/login.html (integrar con JSP)
📝 src/main/webapp/disenio/registro.html (integrar con JSP)
📝 src/main/webapp/disenio/admin-panel.html (integrar con JSP)
🆕 src/main/webapp/js/auth.js
```

#### Prioridad: 🔴 CRÍTICA (bloquea a otros developers)
#### Estimación: ~33% del proyecto

---

### 🟢 **DEVELOPER 2 (Franco) - Backend: Gestión de Contenido, Alquileres y Listas**

#### Responsabilidades Core:
1. **Capa de Modelo - Contenido y Alquileres** ✅ BACKEND
   - Completar clases `Contenido.java`, `Alquiler.java`
   - Completar `Lista.java`, `ListaContenido.java`
   - Implementar `Transaccion.java`
   - Validaciones de negocio (disponibilidad, fechas, precios)

2. **Capa de Repositorio - Acceso a Datos** ✅ BACKEND
   - Crear `ContenidoRepositorio.java`
   - Crear `AlquilerRepositorio.java`
   - Crear `ListaRepositorio.java`
   - Crear `TransaccionRepositorio.java`
   - Queries complejas para búsquedas y filtros

3. **Capa de Servicio - Lógica de Negocio** ✅ BACKEND
   - Crear `ContenidoService.java` (búsquedas, filtros)
   - Crear `AlquilerService.java` (validar disponibilidad, crear alquiler)
   - Crear `ListaService.java` (CRUD de listas)
   - **NO manejan HTTP**

4. **Controladores - Frontend** 🌐 FRONTEND
   - Servlet de catálogo (`CatalogoServlet.java`)
   - Servlet de alquileres (`AlquilerServlet.java`)
   - Servlet de listas personalizadas (`ListaServlet.java`)
   - Servlet de detalle de contenido (`DetalleContenidoServlet.java`)
   - **Solo reciben HTTP y llaman a Services**

5. **Base de Datos - Tablas de Contenido**
   - Scripts SQL para tablas: `contenido`, `alquileres`, `listas`, `lista_contenido`, `transacciones`
   - Relaciones y constraints
   - Índices para optimización
   - Datos de prueba (mínimo 50 películas/series)

6. **Frontend - Vistas de Contenido**
   - Conectar `Index.html` (catálogo principal)
   - Conectar `detalle.html` con backend
   - Conectar `miLista.html` con backend
   - Conectar `paraVer.html` con backend
   - Sistema de búsqueda y filtros
   - Sistema de alquiler con validaciones

#### Archivos a crear/modificar:
```
✅ src/main/java/edu/utn/inspt/cinearchive/backend/modelo/Contenido.java (completar)
✅ src/main/java/edu/utn/inspt/cinearchive/backend/modelo/Alquiler.java (completar)
✅ src/main/java/edu/utn/inspt/cinearchive/backend/modelo/Lista.java (completar)
✅ src/main/java/edu/utn/inspt/cinearchive/backend/modelo/ListaContenido.java (completar)
✅ src/main/java/edu/utn/inspt/cinearchive/backend/modelo/Transaccion.java (completar)
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/repositorio/ContenidoRepositorio.java
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/repositorio/AlquilerRepositorio.java
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/repositorio/ListaRepositorio.java
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/repositorio/TransaccionRepositorio.java
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/servicio/ContenidoService.java
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/servicio/AlquilerService.java
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/servicio/ListaService.java
🆕 src/main/java/edu/utn/inspt/cinearchive/frontend/controlador/CatalogoServlet.java
🆕 src/main/java/edu/utn/inspt/cinearchive/frontend/controlador/AlquilerServlet.java
🆕 src/main/java/edu/utn/inspt/cinearchive/frontend/controlador/ListaServlet.java
🆕 src/main/java/edu/utn/inspt/cinearchive/frontend/controlador/DetalleContenidoServlet.java
🆕 src/main/resources/db/02_contenido.sql
🆕 src/main/resources/db/03_alquileres.sql
🆕 src/main/resources/db/04_listas.sql
📝 src/main/webapp/disenio/Index.html (integrar con JSP)
📝 src/main/webapp/disenio/detalle.html (integrar con JSP)
📝 src/main/webapp/disenio/miLista.html (integrar con JSP)
📝 src/main/webapp/disenio/paraVer.html (integrar con JSP)
🆕 src/main/webapp/js/catalogo.js
🆕 src/main/webapp/js/alquiler.js
🆕 src/main/webapp/js/listas.js
```

#### Prioridad: 🟡 ALTA (depende de autenticación)
#### Estimación: ~33% del proyecto

---

### 🟠 **DEVELOPER 3 (Martin) - Backend: Gestión de Inventario, Reseñas y Reportes**

#### Responsabilidades Core:
1. **Capa de Modelo - Inventario y Analytics** ✅ BACKEND
   - Completar `Categoria.java`, `ContenidoCategoria.java`
   - Completar `Resena.java`
   - Completar `Reporte.java`
   - Lógica de negocio para reportes y analytics

2. **Capa de Repositorio - Acceso a Datos** ✅ BACKEND
   - Crear `CategoriaRepositorio.java`
   - Crear `ResenaRepositorio.java`
   - Crear `ReporteRepositorio.java`
   - Queries complejas para analytics (TOP contenidos, demografía, tendencias)

3. **Capa de Servicio - Lógica de Negocio** ✅ BACKEND
   - Crear `CategoriaService.java` (gestión de categorías)
   - Crear `ResenaService.java` (validar reseñas)
   - Crear `ReporteService.java` (generar reportes)
   - Crear `ApiExternaService.java` (integración TMDb, OMDb)
   - **NO manejan HTTP**

4. **Controladores - Frontend** 🌐 FRONTEND
   - Servlet de gestión de inventario (`GestorInventarioServlet.java`)
   - Servlet de reseñas (`ResenaServlet.java`)
   - Servlet de reportes (`ReporteServlet.java`)
   - Servlet de integración con APIs (`ApiIntegracionServlet.java`)
   - **Solo reciben HTTP y llaman a Services**

5. **Base de Datos - Tablas de Soporte**
   - Scripts SQL para: `categorias`, `contenido_categorias`, `resenas`
   - Views para reportes complejos
   - Stored procedures para analytics
   - Datos de prueba (10 categorías, 100+ reseñas)

6. **Frontend - Vistas de Gestión**
   - Conectar `gestor-inventario.html` con backend
   - Conectar `analista-datos.html` con backend
   - Sistema de reseñas en `detalle.html`
   - Dashboards y gráficos para reportes
   - Formularios de importación de contenido

#### Archivos a crear/modificar:
```
✅ src/main/java/edu/utn/inspt/cinearchive/backend/modelo/Categoria.java (completar)
✅ src/main/java/edu/utn/inspt/cinearchive/backend/modelo/ContenidoCategoria.java (completar)
✅ src/main/java/edu/utn/inspt/cinearchive/backend/modelo/Resena.java (completar)
✅ src/main/java/edu/utn/inspt/cinearchive/backend/modelo/Reporte.java (completar)
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/repositorio/CategoriaRepositorio.java
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/repositorio/ResenaRepositorio.java
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/repositorio/ReporteRepositorio.java
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/servicio/CategoriaService.java
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/servicio/ResenaService.java
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/servicio/ReporteService.java
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/servicio/ApiExternaService.java
🆕 src/main/java/edu/utn/inspt/cinearchive/frontend/controlador/GestorInventarioServlet.java
🆕 src/main/java/edu/utn/inspt/cinearchive/frontend/controlador/ResenaServlet.java
🆕 src/main/java/edu/utn/inspt/cinearchive/frontend/controlador/ReporteServlet.java
🆕 src/main/java/edu/utn/inspt/cinearchive/frontend/controlador/ApiIntegracionServlet.java
🆕 src/main/java/edu/utn/inspt/cinearchive/backend/util/HttpClientUtil.java
🆕 src/main/resources/db/05_categorias_resenas.sql
🆕 src/main/resources/db/06_views_reportes.sql
📝 src/main/webapp/disenio/gestor-inventario.html (integrar con JSP)
📝 src/main/webapp/disenio/analista-datos.html (integrar con JSP)
🆕 src/main/webapp/js/inventario.js
🆕 src/main/webapp/js/reportes.js
🆕 src/main/webapp/js/charts.js
```

#### Prioridad: 🟡 ALTA (parcialmente paralelo)
#### Estimación: ~33% del proyecto

---

## ⚡ CRONOGRAMA INTENSIVO - 4 SEMANAS

### 📅 SEMANA 1 - FUNDAMENTOS Y BASE DE DATOS (25% completado)
**Objetivo:** Tener toda la estructura de BD y modelos base funcionando

#### Lunes-Martes (Días 1-2):
- **Dev 1**: 
  - ✅ Script SQL tabla `usuarios` completo
  - ✅ Modelo `Usuario.java` completado y validado
  - ✅ `ConexionBD.java` (clase compartida)
  - ✅ `PasswordUtil.java` (encriptación)
  
- **Dev 2**:
  - ✅ Scripts SQL: `contenido`, `alquileres`, `listas`, `transacciones`
  - ✅ Modelos: `Contenido.java`, `Alquiler.java`, `Lista.java`, `Transaccion.java`
  
- **Dev 3**:
  - ✅ Scripts SQL: `categorias`, `contenido_categorias`, `resenas`
  - ✅ Modelos: `Categoria.java`, `Resena.java`, `Reporte.java`

#### Miércoles-Viernes (Días 3-5):
- **Dev 1**:
  - ✅ `UsuarioRepositorio.java` completo con todos los métodos CRUD
  - ✅ `UsuarioService.java` con validaciones de negocio
  - ✅ Tests unitarios de Repositorio y Servicio
  - ✅ Datos de prueba (seeders)
  
- **Dev 2**:
  - ✅ `ContenidoRepositorio.java` completo
  - ✅ `AlquilerRepositorio.java` completo
  - ✅ `ListaRepositorio.java` y `TransaccionRepositorio.java`
  - ✅ `ContenidoService.java`, `AlquilerService.java`, `ListaService.java`
  - ✅ Datos de prueba (50+ películas/series)
  
- **Dev 3**:
  - ✅ `CategoriaRepositorio.java` y `ResenaRepositorio.java`
  - ✅ `ReporteRepositorio.java` con queries básicas
  - ✅ `CategoriaService.java`, `ResenaService.java`, `ReporteService.java`
  - ✅ Views SQL para reportes
  - ✅ Datos de prueba

**🎯 Entregable Semana 1:**
- Base de datos completa con todas las tablas y relaciones
- Todos los modelos Java completados
- Todos los Repositorios implementados y probados
- Todos los Servicios implementados con lógica de negocio
- Datos de prueba cargados
- `pom.xml` actualizado con todas las dependencias

---

### 📅 SEMANA 2 - BACKEND Y LÓGICA DE NEGOCIO (50% completado)
**Objetivo:** Tener todos los servlets y lógica de negocio funcionando

#### Lunes-Martes (Días 6-7):
- **Dev 1** 🔴 **PRIORIDAD CRÍTICA**:
  - ✅ `LoginServlet.java` completo (llama a `UsuarioService.autenticar()`)
  - ✅ `RegistroServlet.java` completo (llama a `UsuarioService.registrar()`)
  - ✅ `SecurityFilter.java` (filtro de autenticación)
  - ✅ Sistema de sesiones funcionando
  - ✅ **CHECKPOINT**: Login debe funcionar antes del miércoles
  
- **Dev 2**:
  - ⏸️ Espera a que Dev 1 termine login (trabajar en optimización de queries/tests mientras)
  - 🔧 Preparar: `CatalogoServlet.java` estructura base (llamará a `ContenidoService`)
  
- **Dev 3**:
  - ✅ `ApiExternaService.java` (cliente API TMDb y OMDb)
  - ✅ `HttpClientUtil.java` (utilidades HTTP)
  - ✅ Tests de integración con APIs

#### Miércoles-Viernes (Días 8-10):
- **Dev 1**:
  - ✅ `AdminUsuariosServlet.java` completo (llama a `UsuarioService`)
  - ✅ Validaciones y manejo de errores
  - ✅ **AYUDAR A DEV 2 y 3** con integración de seguridad
  
- **Dev 2** 🟢 **DESBLOQUEAR DESPUÉS DE LOGIN**:
  - ✅ `CatalogoServlet.java` completo (llama a `ContenidoService.buscar()`)
  - ✅ `AlquilerServlet.java` completo (llama a `AlquilerService.crearAlquiler()`)
  - ✅ `ListaServlet.java` completo (llama a `ListaService`)
  - ✅ `DetalleContenidoServlet.java` completo (llama a `ContenidoService.obtenerPorId()`)
  
- **Dev 3**:
  - ✅ `GestorInventarioServlet.java` completo (llama a `CategoriaService`)
  - ✅ `ResenaServlet.java` completo (llama a `ResenaService`)
  - ✅ `ReporteServlet.java` con reportes básicos (llama a `ReporteService`)
  - ✅ `ApiIntegracionServlet.java` para importar contenido

**🎯 Entregable Semana 2:**
- Sistema de autenticación 100% funcional
- Todos los Servlets implementados (llaman a Services, NO acceden a BD directamente)
- Todos los Services funcionando con lógica de negocio completa
- Integración con APIs externas funcionando
- Arquitectura de capas completa: Servlet → Service → Repositorio → BD

---

### 📅 SEMANA 3 - FRONTEND E INTEGRACIÓN (75% completado)
**Objetivo:** Conectar frontend con backend y hacer funcionar todas las vistas

#### Lunes-Martes (Días 11-12):
- **Dev 1**:
  - ✅ Convertir `login.html` a JSP funcional
  - ✅ Convertir `registro.html` a JSP funcional
  - ✅ Convertir `admin-panel.html` a JSP funcional
  - ✅ `auth.js` con validaciones cliente
  - ✅ Manejo de sesiones en todas las vistas
  
- **Dev 2**:
  - ✅ Convertir `Index.html` a JSP funcional (catálogo)
  - ✅ Convertir `detalle.html` a JSP funcional
  - ✅ `catalogo.js` con búsqueda AJAX
  - ✅ `alquiler.js` con proceso de alquiler
  
- **Dev 3**:
  - ✅ Convertir `gestor-inventario.html` a JSP funcional
  - ✅ `inventario.js` con formularios dinámicos
  - ✅ Integración visual con APIs externas

#### Miércoles-Viernes (Días 13-15):
- **Dev 1**:
  - ✅ Testing exhaustivo de autenticación
  - ✅ Permisos por rol en todas las páginas (mediante `SecurityFilter`)
  - ✅ Redirecciones según rol desde los Servlets
  - ✅ Manejo de errores y mensajes de usuario
  
- **Dev 2**:
  - ✅ Convertir `miLista.html` a JSP funcional
  - ✅ Convertir `paraVer.html` a JSP funcional
  - ✅ `listas.js` con gestión de listas (AJAX a `ListaServlet`)
  - ✅ Sistema de búsqueda y filtros avanzados (AJAX a `CatalogoServlet`)
  - ✅ Paginación de resultados
  - ✅ Validación del flujo: Servlet → Service → Repositorio → BD
  
- **Dev 3**:
  - ✅ Convertir `analista-datos.html` a JSP funcional
  - ✅ `reportes.js` con dashboards dinámicos (AJAX a `ReporteServlet`)
  - ✅ `charts.js` con gráficos (Chart.js o similar)
  - ✅ Exportación de reportes a PDF/Excel

**🎯 Entregable Semana 3:**
- Todas las vistas HTML convertidas a JSP
- Frontend completamente integrado con backend
- Flujo de capas verificado: JSP → Servlet → Service → Repositorio → BD
- JavaScript funcional con AJAX comunicándose con Servlets
- Sistema de búsqueda y filtros operativo
- Dashboards de reportes visuales

---

### 📅 SEMANA 4 - TESTING, PULIDO Y ENTREGA (100% completado)
**Objetivo:** Proyecto completo, testeado y documentado

#### Lunes-Martes (Días 16-17): TESTING INTENSIVO
- **TODOS LOS DEVELOPERS**:
  - ✅ Testing funcional de cada módulo
  - ✅ Testing de integración entre módulos
  - ✅ Testing de roles y permisos
  - ✅ Testing de casos extremos y errores
  - ✅ Corrección de bugs críticos
  - ✅ Validaciones cliente y servidor

#### Miércoles (Día 18): OPTIMIZACIÓN
- **Dev 1**:
  - ✅ Optimización de queries de usuarios
  - ✅ Caché de sesiones
  - ✅ Logs de seguridad
  
- **Dev 2**:
  - ✅ Optimización de queries de contenido
  - ✅ Índices de BD para búsquedas
  - ✅ Paginación eficiente
  
- **Dev 3**:
  - ✅ Optimización de queries de reportes
  - ✅ Caché de reportes frecuentes
  - ✅ Límites de API externas

#### Jueves (Día 19): DOCUMENTACIÓN
- **TODOS LOS DEVELOPERS**:
  - ✅ Javadoc en todas las clases públicas
  - ✅ README.md con instrucciones de instalación
  - ✅ Documentación de APIs REST (endpoints)
  - ✅ Guía de usuario básica
  - ✅ Diagrama de arquitectura actualizado
  - ✅ Manual de despliegue

#### Viernes (Día 20): DEMO Y ENTREGA FINAL
- **TODOS LOS DEVELOPERS**:
  - ✅ Demo funcional completa
  - ✅ Presentación del proyecto
  - ✅ Entrega de código fuente
  - ✅ Entrega de documentación
  - ✅ Script de BD completo para instalación
  - ✅ Video/guía de usuario final

**🎯 Entregable Semana 4:**
- Proyecto 100% funcional
- Todos los requisitos implementados
- Testing completo
- Documentación completa
- Demo preparada

---

## 📊 Métricas de Distribución

| Developer | Clases Modelo | Repositorios | Servicios | Servlets | Scripts SQL | Vistas JSP | JS Files |
|-----------|---------------|--------------|-----------|----------|-------------|------------|----------|
| Dev 1     | 1             | 1            | 1         | 3        | 1           | 3          | 1        |
| Dev 2     | 5             | 4            | 3         | 4        | 3           | 4          | 3        |
| Dev 3     | 4             | 3            | 4         | 4        | 2           | 2          | 3        |
| **TOTAL** | **10**        | **8**        | **8**     | **11**   | **6**       | **9**      | **7**    |

---

## 🔄 Dependencias Críticas

```
SEMANA 1: Todos en paralelo (sin bloqueos)
    ↓
SEMANA 2 (Días 6-7): Dev 1 DEBE terminar Login
    ↓
SEMANA 2 (Días 8-10): Dev 2 y 3 pueden continuar
    ↓
SEMANA 3: Todos en paralelo (integración)
    ↓
SEMANA 4: Todos colaborando (testing)
```

### ⚠️ Punto Crítico:
**El login de Dev 1 debe estar listo el MARTES de la Semana 2** para no bloquear a los demás.

---

## 🛠️ Configuración Compartida

### `pom.xml` - Dependencias Maven (Actualizar en Día 1)

```xml
<dependencies>
    <!-- Servlet API -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>4.0.1</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- JSP API -->
    <dependency>
        <groupId>javax.servlet.jsp</groupId>
        <artifactId>javax.servlet.jsp-api</artifactId>
        <version>2.3.3</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- JSTL -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>jstl</artifactId>
        <version>1.2</version>
    </dependency>

    <!-- JDBC MySQL -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>

    <!-- JSON (Gson) -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>

    <!-- HttpClient para APIs externas -->
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.14</version>
    </dependency>

    <!-- BCrypt para encriptación -->
    <dependency>
        <groupId>org.mindrot</groupId>
        <artifactId>jbcrypt</artifactId>
        <version>0.4</version>
    </dependency>

    <!-- JUnit para testing -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>

<build>
    <finalName>cinearchive</finalName>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.11.0</version>
            <configuration>
                <source>11</source>
                <target>11</target>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.eclipse.jetty</groupId>
            <artifactId>jetty-maven-plugin</artifactId>
            <version>9.4.51.v20230217</version>
            <configuration>
                <scanIntervalSeconds>10</scanIntervalSeconds>
                <stopKey>stop</stopKey>
                <stopPort>8081</stopPort>
            </configuration>
        </plugin>
    </plugins>
</build>
```

---

## 📝 Protocolo de Trabajo Diario (CRÍTICO EN 4 SEMANAS)

### Daily Standup (9:00 AM - 15 minutos EXACTOS)
Cada developer reporta:
1. ✅ Lo que completé ayer
2. 🎯 Lo que completaré hoy
3. 🚨 Bloqueadores (si existen)

### Code Review Express (Antes de merge)
- Cada PR debe ser revisado por AL MENOS 1 compañero
- Máximo 2 horas para aprobar/comentar
- Si no hay respuesta en 2 horas, se puede mergear

### Git Workflow (SIMPLE Y RÁPIDO)
```
main (protegida)
  ├── dev1/autenticacion (Dev 1 - Backend: UsuarioService + Repositorio | Frontend: LoginServlet)
  ├── dev2/catalogo (Dev 2 - Backend: ContenidoService + Repositorio | Frontend: CatalogoServlet)
  └── dev3/reportes (Dev 3 - Backend: ReporteService + Repositorio | Frontend: ReporteServlet)
```

**Reglas:**
1. Commits pequeños y frecuentes (cada feature/clase completa)
2. Pull de `main` cada mañana antes de empezar
3. Merge a `main` solo cuando la feature está 100% probada (Backend + Frontend integrados)
4. Resolver conflictos INMEDIATAMENTE
5. **IMPORTANTE**: Cada commit debe incluir cambios coherentes (ej: UsuarioService + UsuarioRepositorio juntos, NO separados)

---

## 🚨 Plan de Contingencia

### Si Dev 1 se atrasa con Login (Semana 2):
- **Dev 2**: Trabajar en queries SQL optimizadas y tests de DAOs
- **Dev 3**: Adelantar documentación y setup de APIs externas
- **Escalación**: Si no está listo el martes, **todos ayudan** el miércoles

### Si hay bloqueos técnicos:
1. Intentar resolver 1 hora solo
2. Consultar con el equipo (Slack/Discord)
3. Si no se resuelve en 2 horas: **pair programming**

### Si falta tiempo en Semana 4:
**Prioridades de recorte (en orden):**
1. ❌ Reportes avanzados (mantener solo básicos)
2. ❌ Integración con OMDb (solo TMDb)
3. ❌ Exportación de reportes a Excel
4. ❌ Gráficos complejos en dashboard
5. ✅ **NO RECORTAR**: Login, Catálogo, Alquiler, CRUD básico

---

## 🎯 Criterios de Éxito (Demo Final - Día 20)

### Must Have (Obligatorios):
✅ Los 4 tipos de usuarios pueden iniciar sesión  
✅ Los usuarios regulares pueden alquilar contenido  
✅ Los gestores pueden agregar/editar contenido manualmente  
✅ Los analistas pueden ver al menos 3 reportes básicos  
✅ Los admins pueden gestionar usuarios  
✅ La base de datos tiene datos de prueba realistas  
✅ El frontend está integrado y es usable  
✅ No hay errores críticos  

### Nice to Have (Deseables):
🎁 Importación desde TMDb funcionando  
🎁 Dashboards con gráficos visuales  
🎁 Sistema de búsqueda avanzada  
🎁 Exportación de reportes  
🎁 Responsive design  

---

## 💡 Tips para Sobrevivir a 4 Semanas

### Para Dev 1:
- 🔥 **Tu trabajo es el más crítico**: El login DEBE estar listo el martes de Semana 2
- 💪 Enfócate en lo esencial primero: Login → Registro → Filtro de seguridad
- 🤝 En Semana 2 (miércoles-viernes), ayuda activamente a Dev 2 y 3

### Para Dev 2:
- 📦 Tienes el módulo más grande: prioriza Catálogo y Alquiler sobre Listas
- ⏰ No te bloquees esperando login: prepara todo lo posible (DAOs, modelos, tests)
- 🎯 Semana 3 es tu momento más intenso: planifica bien

### Para Dev 3:
- 🌐 Las APIs externas pueden fallar: ten un plan B (datos mock)
- 📊 Los reportes pueden ser simples al inicio: queries básicas funcionan
- 🎨 Los gráficos pueden ser básicos: tabla HTML > nada

### Para TODOS:
- 💬 **Comunicación constante**: Slack/Discord siempre abierto
- 🐛 **Bug tracking**: Notion, Trello o GitHub Issues desde día 1
- 📸 **Screenshots**: Documentar progreso visualmente ayuda mucho
- ⏱️ **Timeboxing**: Si algo toma más de lo esperado, pedir ayuda
- 🎉 **Celebrar mini-logros**: Mantiene la moral alta

---

## 📈 Checklist Semanal

### ✅ Fin de Semana 1:
- [ ] Base de datos completa y poblada
- [ ] Todos los modelos funcionando
- [ ] Todos los DAOs testeados
- [ ] `pom.xml` con todas las dependencias
- [ ] Conexión a BD funcionando

### ✅ Fin de Semana 2:
- [ ] Login y registro funcionales
- [ ] Todos los servlets implementados
- [ ] Filtro de seguridad funcionando
- [ ] APIs REST documentadas
- [ ] Integración con APIs externas OK

### ✅ Fin de Semana 3:
- [ ] Todas las vistas HTML → JSP
- [ ] JavaScript funcional
- [ ] Frontend-backend integrado
- [ ] Búsquedas y filtros operativos
- [ ] Dashboards visuales básicos

### ✅ Fin de Semana 4:
- [ ] Testing completo
- [ ] Bugs críticos resueltos
- [ ] Documentación completa
- [ ] Demo preparada
- [ ] Código en repositorio limpio

---

## 🚀 Comando de Inicio Rápido

```bash
# Día 1 - Setup inicial (todos juntos)
git clone [repo]
cd CineArchive
mvn clean install
# Configurar BD en MySQL Workbench (usar archivo .mwb)
# Actualizar credenciales en ConexionBD.java
mvn jetty:run
# Abrir http://localhost:8080
```

---

## 🏆 Meta Final

**Viernes 20 (4:00 PM)**: Demo funcional de 15 minutos mostrando:
1. Login de cada tipo de usuario (2 min)
2. Usuario regular alquilando contenido (3 min)
3. Gestor agregando contenido (3 min)
4. Analista viendo reportes (3 min)
5. Admin gestionando usuarios (2 min)
6. Q&A (2 min)

---

## 📝 Resumen de Cambios en el Plan (Nueva Estructura Frontend/Backend)

### ❌ Antes (DAOs mezclados):
```
src/main/java/edu/utn/inspt/cinearchive/
├── modelo/
│   ├── Usuario.java
│   └── bd/
│       ├── UsuarioDAO.java       ← Mezcla acceso a BD
│       ├── ContenidoDAO.java
│       └── ...
└── controlador/
    ├── LoginServlet.java         ← Mezcla HTTP + Lógica
    ├── CatalogoServlet.java
    └── ...
```

**Problemas:**
- Servlets accedían directamente a DAOs
- Lógica de negocio esparcida
- Difícil de mantener y testear
- No separaba front del back

### ✅ Ahora (Frontend/Backend claro):
```
src/main/java/edu/utn/inspt/cinearchive/
├── frontend/
│   └── controlador/
│       ├── LoginServlet.java      ← SOLO HTTP handling
│       ├── CatalogoServlet.java
│       └── ...
└── backend/
    ├── modelo/
    │   ├── Usuario.java
    │   ├── Contenido.java
    │   └── ...
    ├── repositorio/
    │   ├── UsuarioRepositorio.java    ← SOLO BD
    │   ├── ContenidoRepositorio.java
    │   └── ConexionBD.java
    ├── servicio/
    │   ├── UsuarioService.java        ← SOLO lógica
    │   ├── ContenidoService.java
    │   └── ...
    └── util/
        ├── PasswordUtil.java
        └── HttpClientUtil.java
```

**Beneficios:**
- ✅ Separación clara de responsabilidades
- ✅ Frontend = HTTP handling (Servlets)
- ✅ Backend = Lógica pura (Services) + Acceso a BD (Repositorios)
- ✅ Fácil de testear (cada capa por separado)
- ✅ Cumple con la corrección del profesor
- ✅ Preparado para API REST futuro

### Cambios Clave:
1. **DAOs → Repositorios** (ubicados en `backend/repositorio/`)
2. **Lógica de negocio → Services** (nueva capa en `backend/servicio/`)
3. **Servlets → Frontend** (ahora en `frontend/controlador/`)
4. **Arquitectura de 3 capas**: Servlet → Service → Repositorio → BD

---

**¡El plan ahora refleja exactamente lo que tu profesor pidió!** 🎬✨

