# 🧪 Guía de Testing - Developer 3 (Martín)

**Proyecto:** CineArchive V2  
**Módulos:** Gestión de Inventario, Reseñas y Reportes  
**Framework:** Spring MVC 5.3.30 + MySQL 8.0  
**Fecha:** 12 de Noviembre de 2025

---

## 📋 Índice

1. [Requisitos Previos](#requisitos-previos)
2. [Testing de Categorías](#testing-de-categorías)
3. [Testing de Reseñas](#testing-de-reseñas)
4. [Testing de Reportes y Analytics](#testing-de-reportes-y-analytics)
5. [Testing de Vistas JSP](#testing-de-vistas-jsp)
6. [Testing de Integración](#testing-de-integración)
7. [Tests Automatizados](#tests-automatizados)
8. [Checklist Final](#checklist-final)

---

## 📌 Requisitos Previos

### 1. Verificar que la Aplicación está Corriendo

```bash
# Compilar y ejecutar con Maven
mvn clean package
mvn jetty:run

# O si usas Tomcat
mvn tomcat7:run

# La aplicación debe estar disponible en:
# http://localhost:8080/cinearchive
```

### 2. Verificar Base de Datos

```bash
# Conectarse a MySQL
mysql -u root -p

# Verificar que la base de datos existe
USE cinearchive_v2;

# Verificar que las tablas del Dev 3 existen
SHOW TABLES LIKE 'categoria';
SHOW TABLES LIKE 'contenido_categoria';
SHOW TABLES LIKE 'resena';
SHOW TABLES LIKE 'reporte';

# Verificar que las views existen
SHOW FULL TABLES WHERE Table_type = 'VIEW';
```

### 3. Herramientas Necesarias

- **Navegador:** Chrome, Firefox, Edge (con DevTools)
- **Cliente REST:** Postman, cURL, o extensión Thunder Client para VS Code
- **Editor JSON:** Para visualizar respuestas
- **Usuario de prueba:** Necesitas usuarios con diferentes roles

---

## 🏷️ Testing de Categorías

### 1. Testing del API REST de Categorías

#### 1.1. Listar Todas las Categorías

```bash
curl -X GET http://localhost:8080/cinearchive/api/categorias
```

**Resultado Esperado:**
- Código HTTP: 200 OK
- JSON con array de categorías
- Cada categoría debe tener: id, nombre, tipo, descripcion

**Ejemplo de respuesta:**
```json
[
  {
    "id": 1,
    "nombre": "Acción",
    "tipo": "GENERO",
    "descripcion": "Películas y series con secuencias de acción, persecuciones y aventuras"
  },
  {
    "id": 2,
    "nombre": "Drama",
    "tipo": "GENERO",
    "descripcion": "Historias con profundidad emocional y desarrollo de personajes"
  }
]
```

#### 1.2. Obtener Categoría por ID

```bash
curl -X GET http://localhost:8080/cinearchive/api/categorias/1
```

**Resultado Esperado:**
- Código HTTP: 200 OK si existe
- Código HTTP: 404 Not Found si no existe
- JSON con la categoría específica

#### 1.3. Filtrar por Tipo

```bash
# Obtener solo géneros
curl -X GET http://localhost:8080/cinearchive/api/categorias/tipo/GENERO

# Obtener solo tags
curl -X GET http://localhost:8080/cinearchive/api/categorias/tipo/TAG

# Obtener solo clasificaciones
curl -X GET http://localhost:8080/cinearchive/api/categorias/tipo/CLASIFICACION
```

**Resultado Esperado:**
- Código HTTP: 200 OK
- Array filtrado según el tipo solicitado

#### 1.4. Obtener Géneros, Tags y Clasificaciones (Endpoints Especializados)

```bash
# Obtener géneros
curl -X GET http://localhost:8080/cinearchive/api/categorias/generos

# Obtener tags
curl -X GET http://localhost:8080/cinearchive/api/categorias/tags

# Obtener clasificaciones
curl -X GET http://localhost:8080/cinearchive/api/categorias/clasificaciones
```

#### 1.5. Crear Nueva Categoría

```bash
curl -X POST http://localhost:8080/cinearchive/api/categorias \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Aventura",
    "tipo": "GENERO",
    "descripcion": "Películas de aventuras y exploración"
  }'
```

**Resultado Esperado:**
- Código HTTP: 201 Created
- JSON con la categoría creada (incluyendo ID asignado)

**Casos de Error a Probar:**
```bash
# Nombre duplicado
curl -X POST http://localhost:8080/cinearchive/api/categorias \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Acción",
    "tipo": "GENERO",
    "descripcion": "Duplicado"
  }'
# Esperado: 400 Bad Request

# Nombre vacío (validación)
curl -X POST http://localhost:8080/cinearchive/api/categorias \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "",
    "tipo": "GENERO",
    "descripcion": "Sin nombre"
  }'
# Esperado: 400 Bad Request

# Tipo inválido
curl -X POST http://localhost:8080/cinearchive/api/categorias \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test",
    "tipo": "INVALIDO",
    "descripcion": "Tipo incorrecto"
  }'
# Esperado: 400 Bad Request
```

#### 1.6. Actualizar Categoría

```bash
curl -X PUT http://localhost:8080/cinearchive/api/categorias/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Acción Actualizado",
    "tipo": "GENERO",
    "descripcion": "Descripción actualizada"
  }'
```

**Resultado Esperado:**
- Código HTTP: 200 OK
- JSON con la categoría actualizada

#### 1.7. Eliminar Categoría

```bash
curl -X DELETE http://localhost:8080/cinearchive/api/categorias/99
```

**Resultado Esperado:**
- Código HTTP: 204 No Content si existe
- Código HTTP: 404 Not Found si no existe

⚠️ **NOTA:** Si la categoría está asignada a contenidos, puede fallar por FK constraint.

#### 1.8. Buscar por Nombre

```bash
curl -X GET http://localhost:8080/cinearchive/api/categorias/nombre/Acción
```

**Resultado Esperado:**
- Código HTTP: 200 OK si existe
- Código HTTP: 404 Not Found si no existe
- JSON con la categoría encontrada

---

### 2. Testing de la Vista de Categorías (JSP)

#### 2.1. Acceso a la Vista

1. **Login como Gestor de Inventario:**
   - URL: http://localhost:8080/cinearchive/login
   - Usar credenciales de un usuario con rol GESTOR_INVENTARIO
   - Email: `gestor@cinearchive.com`
   - Password: (según datos de prueba)

2. **Navegar a Categorías:**
   - URL directa: http://localhost:8080/cinearchive/categorias
   - O desde el menú de navegación

**Resultado Esperado:**
- Página carga correctamente
- Se muestran tres secciones: Géneros, Tags, Clasificaciones
- Cada categoría se muestra en un card
- Botones de acción visibles: Editar, Eliminar

#### 2.2. Probar Funcionalidades de la Vista

**Test 1: Visualización de Categorías**
- ✅ Se muestran todas las categorías agrupadas por tipo
- ✅ Los cards tienen diseño visual atractivo
- ✅ La descripción se muestra completa o truncada

**Test 2: Crear Nueva Categoría**
1. Hacer clic en "Nueva Categoría"
2. Llenar formulario:
   - Nombre: "Fantasía"
   - Tipo: Seleccionar "GENERO"
   - Descripción: "Mundos imaginarios y magia"
3. Hacer clic en "Guardar"

**Resultado Esperado:**
- Modal se cierra
- Nueva categoría aparece en la sección correspondiente
- Mensaje de éxito se muestra

**Test 3: Editar Categoría**
1. Hacer clic en "Editar" en una categoría existente
2. Modificar el nombre o descripción
3. Guardar cambios

**Resultado Esperado:**
- Los cambios se reflejan inmediatamente
- Mensaje de confirmación se muestra

**Test 4: Eliminar Categoría**
1. Hacer clic en "Eliminar" en una categoría
2. Confirmar eliminación en el diálogo

**Resultado Esperado:**
- La categoría desaparece de la lista
- Mensaje de confirmación se muestra

**Test 5: Validaciones del Formulario**
- Intentar crear sin nombre → Error
- Intentar crear con nombre duplicado → Error
- Intentar crear con nombre muy corto (< 2 caracteres) → Error
- Intentar crear con nombre muy largo (> 100 caracteres) → Error

#### 2.3. Testing de Control de Acceso

**Test con Usuario No Autorizado:**
1. Cerrar sesión
2. Login como USUARIO_REGULAR
3. Intentar acceder a: http://localhost:8080/cinearchive/categorias

**Resultado Esperado:**
- Redirección a página de acceso denegado
- O redirección al login

---

## ⭐ Testing de Reseñas

### 1. Testing del API REST de Reseñas

#### 1.1. Listar Todas las Reseñas

```bash
curl -X GET http://localhost:8080/cinearchive/api/resenas
```

**Resultado Esperado:**
- Código HTTP: 200 OK
- JSON con array de reseñas
- Cada reseña debe incluir: id, usuario, contenido, calificacion, titulo, texto, fechas

#### 1.2. Obtener Reseña por ID

```bash
curl -X GET http://localhost:8080/cinearchive/api/resenas/1
```

**Resultado Esperado:**
- Código HTTP: 200 OK si existe
- Código HTTP: 404 Not Found si no existe

#### 1.3. Obtener Reseñas por Usuario

```bash
curl -X GET http://localhost:8080/cinearchive/api/resenas/usuario/1
```

**Resultado Esperado:**
- Código HTTP: 200 OK
- Array de todas las reseñas del usuario con ID 1

#### 1.4. Obtener Reseñas por Contenido

```bash
curl -X GET http://localhost:8080/cinearchive/api/resenas/contenido/5
```

**Resultado Esperado:**
- Código HTTP: 200 OK
- Array de todas las reseñas del contenido con ID 5
- Útil para mostrar en la página de detalle

#### 1.5. Obtener Calificación Promedio

```bash
curl -X GET http://localhost:8080/cinearchive/api/resenas/contenido/5/promedio
```

**Resultado Esperado:**
- Código HTTP: 200 OK
- Número decimal con el promedio (ej: 4.2)
- Código HTTP: 404 si el contenido no tiene reseñas

#### 1.6. Filtrar por Calificación Mínima

```bash
curl -X GET http://localhost:8080/cinearchive/api/resenas/calificacion/4.0
```

**Resultado Esperado:**
- Código HTTP: 200 OK
- Array de reseñas con calificación >= 4.0

#### 1.7. Crear Nueva Reseña

```bash
curl -X POST http://localhost:8080/cinearchive/api/resenas \
  -H "Content-Type: application/json" \
  -d '{
    "usuario": {"id": 1},
    "contenido": {"id": 5},
    "calificacion": 4.5,
    "titulo": "Excelente película",
    "texto": "Me encantó la trama y las actuaciones. Muy recomendable para toda la familia."
  }'
```

**Resultado Esperado:**
- Código HTTP: 201 Created
- JSON con la reseña creada (incluyendo ID y fechas)

**Casos de Error a Probar:**

```bash
# Usuario ya reseñó este contenido
curl -X POST http://localhost:8080/cinearchive/api/resenas \
  -H "Content-Type: application/json" \
  -d '{
    "usuario": {"id": 1},
    "contenido": {"id": 5},
    "calificacion": 3.0,
    "titulo": "Duplicado",
    "texto": "Intentando duplicar"
  }'
# Esperado: 409 Conflict

# Calificación inválida (< 0)
curl -X POST http://localhost:8080/cinearchive/api/resenas \
  -H "Content-Type: application/json" \
  -d '{
    "usuario": {"id": 1},
    "contenido": {"id": 10},
    "calificacion": -1.0,
    "titulo": "Inválido",
    "texto": "Calificación negativa"
  }'
# Esperado: 400 Bad Request

# Calificación inválida (> 5)
curl -X POST http://localhost:8080/cinearchive/api/resenas \
  -H "Content-Type: application/json" \
  -d '{
    "usuario": {"id": 1},
    "contenido": {"id": 10},
    "calificacion": 6.0,
    "titulo": "Inválido",
    "texto": "Calificación muy alta"
  }'
# Esperado: 400 Bad Request

# Título muy corto (< 3 caracteres)
curl -X POST http://localhost:8080/cinearchive/api/resenas \
  -H "Content-Type: application/json" \
  -d '{
    "usuario": {"id": 1},
    "contenido": {"id": 10},
    "calificacion": 4.0,
    "titulo": "AB",
    "texto": "Título muy corto"
  }'
# Esperado: 400 Bad Request

# Texto muy largo (> 2000 caracteres)
curl -X POST http://localhost:8080/cinearchive/api/resenas \
  -H "Content-Type: application/json" \
  -d '{
    "usuario": {"id": 1},
    "contenido": {"id": 10},
    "calificacion": 4.0,
    "titulo": "Texto largo",
    "texto": "[texto de más de 2000 caracteres]"
  }'
# Esperado: 400 Bad Request
```

#### 1.8. Actualizar Reseña

```bash
curl -X PUT http://localhost:8080/cinearchive/api/resenas/1 \
  -H "Content-Type: application/json" \
  -d '{
    "usuario": {"id": 1},
    "contenido": {"id": 5},
    "calificacion": 5.0,
    "titulo": "Excelente película (actualizada)",
    "texto": "Después de verla de nuevo, merece 5 estrellas."
  }'
```

**Resultado Esperado:**
- Código HTTP: 200 OK
- JSON con la reseña actualizada
- fecha_modificacion debe ser diferente a fecha_creacion

#### 1.9. Eliminar Reseña

```bash
curl -X DELETE http://localhost:8080/cinearchive/api/resenas/1
```

**Resultado Esperado:**
- Código HTTP: 204 No Content
- Código HTTP: 404 Not Found si no existe

#### 1.10. Verificar si Usuario ya Reseñó

```bash
curl -X GET "http://localhost:8080/cinearchive/api/resenas/usuario/1/contenido/5/existe"
```

**Resultado Esperado:**
- Código HTTP: 200 OK
- JSON: `true` o `false`

---

### 2. Testing de Reseñas en la Vista de Detalle

#### 2.1. Ver Reseñas de un Contenido

1. **Login como cualquier usuario**
2. **Navegar a detalle de contenido:**
   - URL: http://localhost:8080/cinearchive/detalle/5
   - Scroll hacia abajo a la sección de reseñas

**Resultado Esperado:**
- Se muestra el promedio de calificación (ej: 4.5 ⭐)
- Se muestran todas las reseñas del contenido
- Cada reseña muestra: usuario, calificación (estrellas), título, texto, fecha

#### 2.2. Crear Reseña desde la Vista

1. **En la página de detalle de un contenido que NO has reseñado**
2. **Llenar formulario de reseña:**
   - Seleccionar calificación (1-5 estrellas)
   - Ingresar título
   - Ingresar texto
3. **Hacer clic en "Enviar Reseña"**

**Resultado Esperado:**
- La reseña aparece inmediatamente en la lista
- El promedio de calificación se actualiza
- Mensaje de éxito se muestra
- El formulario se limpia o desaparece

#### 2.3. Editar Reseña Propia

1. **Ver tu propia reseña en la lista**
2. **Hacer clic en "Editar"**
3. **Modificar calificación o texto**
4. **Guardar cambios**

**Resultado Esperado:**
- Los cambios se reflejan inmediatamente
- Se muestra "Editado el [fecha]" debajo de la reseña

#### 2.4. Eliminar Reseña Propia

1. **Hacer clic en "Eliminar" en tu propia reseña**
2. **Confirmar eliminación**

**Resultado Esperado:**
- La reseña desaparece de la lista
- El promedio de calificación se recalcula

#### 2.5. Restricciones de Seguridad

**Test: Usuario no puede editar reseñas de otros**
- Verificar que botones "Editar" y "Eliminar" solo aparecen en TUS reseñas
- Intentar hacer request directo de PUT/DELETE a reseña ajena → Debe fallar

**Test: Solo se puede hacer una reseña por contenido**
- Intentar crear segunda reseña en el mismo contenido → Error o mensaje

---

## 📊 Testing de Reportes y Analytics

### 1. Testing del API REST de Reportes

#### 1.1. Obtener Estadísticas Generales (Dashboard)

```bash
curl -X GET http://localhost:8080/cinearchive/reportes/api/dashboard
```

**Resultado Esperado:**
- Código HTTP: 200 OK
- JSON con KPIs:
  ```json
  {
    "totalUsuarios": 25,
    "totalContenidos": 72,
    "totalAlquileres": 145,
    "ingresosTotales": 2450.50,
    "alquileresActivos": 38,
    "usuariosActivos": 18,
    "contenidosMasAlquilados": 15,
    "tasaRetencion": 72.5
  }
  ```

#### 1.2. Obtener Top Contenidos

```bash
curl -X GET "http://localhost:8080/cinearchive/reportes/api/top-contenidos?limite=10"
```

**Resultado Esperado:**
- Array de 10 contenidos más alquilados
- Cada elemento con: id, titulo, totalAlquileres, ingresos

**Con Periodo de Fechas:**
```bash
curl -X GET "http://localhost:8080/cinearchive/reportes/api/top-contenidos?fechaInicio=2025-01-01&fechaFin=2025-12-31&limite=10"
```

#### 1.3. Obtener Categorías Populares

```bash
curl -X GET "http://localhost:8080/cinearchive/reportes/api/categorias-populares?limite=5"
```

**Resultado Esperado:**
- Array de 5 categorías más populares
- Cada elemento con: categoria, totalContenidos, totalAlquileres

#### 1.4. Obtener Contenidos Mejor Calificados

```bash
curl -X GET "http://localhost:8080/cinearchive/reportes/api/mejor-calificados?limite=10"
```

**Resultado Esperado:**
- Array de 10 contenidos con mejor rating
- Cada elemento con: id, titulo, calificacionPromedio, totalResenas

#### 1.5. Obtener Análisis Demográfico

```bash
curl -X GET "http://localhost:8080/cinearchive/reportes/api/analisis-demografico?fechaInicio=2025-01-01&fechaFin=2025-12-31"
```

**Resultado Esperado:**
- Array con datos por rango de edad y género:
  ```json
  [
    {
      "rangoEdad": "18-25",
      "genero": "MASCULINO",
      "totalAlquileres": 45,
      "gastoPromedio": 15.50
    },
    {
      "rangoEdad": "26-35",
      "genero": "FEMENINO",
      "totalAlquileres": 67,
      "gastoPromedio": 18.20
    }
  ]
  ```

#### 1.6. Obtener Rendimiento de Géneros

```bash
curl -X GET "http://localhost:8080/cinearchive/reportes/api/rendimiento-generos?fechaInicio=2025-01-01&fechaFin=2025-12-31"
```

**Resultado Esperado:**
- Array con datos por género de contenido:
  ```json
  [
    {
      "genero": "Acción",
      "totalAlquileres": 85,
      "ingresos": 1275.00,
      "calificacionPromedio": 4.3
    }
  ]
  ```

#### 1.7. Obtener Tendencias Temporales

```bash
curl -X GET "http://localhost:8080/cinearchive/reportes/api/tendencias-temporales?fechaInicio=2025-01-01&fechaFin=2025-12-31"
```

**Resultado Esperado:**
- Array con datos por mes:
  ```json
  [
    {
      "anio": 2025,
      "mes": 1,
      "totalAlquileres": 45,
      "ingresos": 675.00,
      "usuariosActivos": 28
    }
  ]
  ```

#### 1.8. Obtener Comportamiento de Usuarios

```bash
curl -X GET "http://localhost:8080/cinearchive/reportes/api/comportamiento-usuarios?fechaInicio=2025-01-01&fechaFin=2025-12-31"
```

**Resultado Esperado:**
- Array con patrones de uso:
  ```json
  [
    {
      "clasificacion": "VIP",
      "totalUsuarios": 5,
      "alquileresPromedio": 12.4,
      "gastoPromedio": 185.50
    }
  ]
  ```

#### 1.9. Generar Reporte

```bash
# Generar reporte de contenidos más alquilados
curl -X POST "http://localhost:8080/cinearchive/reportes/api/generar/MAS_ALQUILADOS?analistaId=3&fechaInicio=2025-01-01&fechaFin=2025-12-31&limite=10"
```

**Resultado Esperado:**
- Código HTTP: 201 Created
- JSON con el reporte generado y guardado
- ID del reporte creado

**Tipos de Reporte Disponibles:**
- `MAS_ALQUILADOS`
- `ANALISIS_DEMOGRAFICO`
- `RENDIMIENTO_GENEROS`
- `TENDENCIAS_TEMPORALES`
- `COMPORTAMIENTO_USUARIOS`

#### 1.10. Listar Reportes Guardados

```bash
# Todos los reportes
curl -X GET http://localhost:8080/cinearchive/reportes/api

# Por analista
curl -X GET http://localhost:8080/cinearchive/reportes/api/analista/3

# Por tipo
curl -X GET http://localhost:8080/cinearchive/reportes/api/tipo/MAS_ALQUILADOS
```

#### 1.11. Obtener Reporte por ID

```bash
curl -X GET http://localhost:8080/cinearchive/reportes/api/1
```

**Resultado Esperado:**
- Código HTTP: 200 OK
- JSON con detalles completos del reporte incluyendo resultados

#### 1.12. Eliminar Reporte

```bash
curl -X DELETE http://localhost:8080/cinearchive/reportes/api/1
```

**Resultado Esperado:**
- Código HTTP: 204 No Content

---

### 2. Testing de la Vista de Analista de Datos

#### 2.1. Acceso al Dashboard

1. **Login como Analista de Datos:**
   - URL: http://localhost:8080/cinearchive/login
   - Email: `analista@cinearchive.com`
   - Password: (según datos de prueba)

2. **Navegar al Dashboard:**
   - URL: http://localhost:8080/cinearchive/analista-datos
   - O desde el menú de navegación

**Resultado Esperado:**
- Página carga correctamente
- Se muestran 4-6 KPIs principales en la parte superior
- Se muestran gráficos interactivos con Chart.js

#### 2.2. Probar Funcionalidades del Dashboard

**Test 1: KPIs Principales**
- ✅ Total Usuarios
- ✅ Total Contenidos
- ✅ Total Alquileres
- ✅ Ingresos Totales
- ✅ Alquileres Activos
- ✅ Usuarios Activos

**Resultado Esperado:**
- Números se cargan dinámicamente (no hardcoded)
- Formato de moneda correcto para ingresos
- Íconos apropiados para cada KPI

**Test 2: Gráfico de Top Contenidos**
- ✅ Se muestra gráfico de barras horizontales
- ✅ Muestra 10 contenidos más alquilados
- ✅ Datos corresponden con la BD
- ✅ Tooltips funcionan al pasar el mouse
- ✅ Colores visualmente atractivos

**Test 3: Gráfico de Rendimiento de Géneros**
- ✅ Se muestra gráfico de pastel (pie chart)
- ✅ Muestra distribución de alquileres por género
- ✅ Porcentajes suman 100%
- ✅ Leyenda visible y clara

**Test 4: Gráfico de Tendencias Temporales**
- ✅ Se muestra gráfico de línea
- ✅ Eje X muestra meses
- ✅ Eje Y muestra cantidad de alquileres
- ✅ Línea suave y continua

**Test 5: Gráfico de Análisis Demográfico**
- ✅ Se muestra gráfico de barras agrupadas
- ✅ Grupos por rango de edad
- ✅ Subgrupos por género
- ✅ Datos legibles

#### 2.3. Filtros de Periodo

**Test 1: Aplicar Filtro de Fechas**
1. Seleccionar fecha de inicio: 2025-01-01
2. Seleccionar fecha de fin: 2025-06-30
3. Hacer clic en "Actualizar"

**Resultado Esperado:**
- Los gráficos se actualizan con datos del periodo
- URL se actualiza con query params
- Spinner de carga se muestra durante la actualización

**Test 2: Filtros Inválidos**
- Fecha inicio > fecha fin → Mostrar error
- Fechas vacías → Usar valores por defecto

#### 2.4. Generación de Reportes

**Test 1: Generar Reporte de Contenidos Más Alquilados**
1. Seleccionar tipo: "Contenidos Más Alquilados"
2. Seleccionar periodo
3. Ingresar límite: 10
4. Hacer clic en "Generar Reporte"

**Resultado Esperado:**
- Modal o spinner de carga
- Reporte aparece en "Historial de Reportes"
- Mensaje de éxito
- Datos del reporte se muestran

**Test 2: Generar Todos los Tipos de Reportes**
- Repetir para cada uno de los 5 tipos
- Verificar que cada uno genera datos diferentes

**Test 3: Validaciones**
- Sin fecha de inicio → Error
- Sin fecha de fin → Error
- Límite <= 0 → Error

#### 2.5. Historial de Reportes

**Test 1: Ver Historial**
- Scroll a sección "Historial de Reportes"
- Verificar que aparecen reportes generados
- Cada reporte muestra: título, tipo, fecha, periodo

**Test 2: Ver Detalles de Reporte**
- Hacer clic en "Ver Detalles" de un reporte
- Se muestra modal con información completa
- Se muestran los resultados en formato legible

**Test 3: Eliminar Reporte**
- Hacer clic en "Eliminar" en un reporte
- Confirmar eliminación
- Reporte desaparece del historial

#### 2.6. Exportación

**Test 1: Exportar a PDF**
1. Hacer clic en "Exportar PDF"
2. Se descarga archivo PDF

**Resultado Esperado:**
- PDF contiene KPIs
- PDF contiene gráficos (como imágenes)
- Formato profesional

**Test 2: Exportar a Excel**
1. Hacer clic en "Exportar Excel"
2. Se descarga archivo .xlsx

**Resultado Esperado:**
- Excel contiene múltiples hojas
- Cada hoja con datos de un tipo de análisis
- Datos formateados correctamente

**Test 3: Exportar a CSV**
1. Hacer clic en "Exportar CSV"
2. Se descarga archivo .csv

**Resultado Esperado:**
- CSV con datos tabulares
- Headers correctos
- Separadores apropiados

#### 2.7. Control de Acceso

**Test con Usuario No Autorizado:**
1. Cerrar sesión
2. Login como USUARIO_REGULAR
3. Intentar acceder a: http://localhost:8080/cinearchive/analista-datos

**Resultado Esperado:**
- Redirección a acceso denegado
- O redirección al index

---

### 3. Testing de la Vista de Gestor de Inventario

#### 3.1. Acceso al Panel

1. **Login como Gestor de Inventario:**
   - Email: `gestor@cinearchive.com`
   - Password: (según datos de prueba)

2. **Navegar al Panel:**
   - URL: http://localhost:8080/cinearchive/gestor-inventario

**Resultado Esperado:**
- Página carga correctamente
- Se muestra listado de contenidos
- Opciones de gestión visibles

#### 3.2. Gestión de Contenidos

**Test 1: Ver Listado**
- Se muestran todos los contenidos
- Cada contenido muestra: título, tipo, género, precio, disponibilidad

**Test 2: Editar Contenido**
1. Hacer clic en "Editar" en un contenido
2. Modificar precio de alquiler
3. Modificar copias disponibles
4. Guardar cambios

**Resultado Esperado:**
- Cambios se reflejan en BD
- Mensaje de confirmación

**Test 3: Cambiar Disponibilidad**
- Toggle de "Disponible para Alquiler"
- Verificar que usuarios no pueden alquilar si está deshabilitado

#### 3.3. Gestión de Categorías desde el Panel

**Test: Link a Categorías**
1. Hacer clic en "Gestionar Categorías"
2. Se redirige a /categorias
3. Funcionalidades de categorías disponibles

---

## 🔗 Testing de Integración

### 1. Flujo Completo: Usuario Reseña un Contenido

**Escenario:**
1. Usuario alquila una película
2. Ve la película (marca como vista)
3. Va a detalles de la película
4. Deja una reseña con calificación 5 estrellas
5. La calificación promedio se actualiza
6. Analista genera reporte de contenidos mejor calificados
7. La película aparece en el top por su nueva reseña

**Pasos de Testing:**
1. Login como usuario regular
2. Buscar contenido en catálogo
3. Alquilar contenido
4. Ir a "Mis Alquileres"
5. Marcar como visto
6. Ir a detalle del contenido
7. Crear reseña
8. Logout
9. Login como analista
10. Ir a dashboard
11. Verificar que contenido aparece en "Mejor Calificados"

**Resultado Esperado:**
- Todo el flujo funciona sin errores
- Datos se propagan correctamente
- Analytics reflejan la nueva reseña

### 2. Flujo Completo: Gestor Crea Categoría y la Asigna

**Escenario:**
1. Gestor crea nueva categoría "Ciencia Ficción"
2. Asigna categoría a varios contenidos
3. Usuario busca por esa categoría
4. Analista genera reporte de rendimiento por género
5. Nueva categoría aparece en analytics

**Pasos de Testing:**
1. Login como gestor
2. Ir a /categorias
3. Crear nueva categoría tipo GENERO
4. Asignar a 3-5 contenidos (puede requerir otra vista)
5. Logout
6. Login como usuario regular
7. Buscar contenidos de "Ciencia Ficción"
8. Verificar que aparecen
9. Login como analista
10. Generar reporte de rendimiento de géneros
11. Verificar que "Ciencia Ficción" aparece

**Resultado Esperado:**
- Categoría se crea correctamente
- Asignaciones funcionan
- Búsqueda filtra correctamente
- Analytics incluyen nueva categoría

### 3. Flujo Completo: Analytics en Tiempo Real

**Escenario:**
1. Estado inicial: Dashboard muestra KPIs
2. Usuario realiza 3 alquileres nuevos
3. Analista refresca dashboard
4. KPIs se actualizan automáticamente
5. Gráficos se actualizan

**Pasos de Testing:**
1. Login como analista → Anotar valores de KPIs
2. Abrir nueva pestaña → Login como usuario
3. Realizar 3 alquileres
4. Volver a pestaña de analista → Refrescar
5. Comparar KPIs nuevos con anteriores

**Resultado Esperado:**
- Total Alquileres aumenta en 3
- Ingresos aumentan según precios
- Gráficos se actualizan con nuevos datos

---

## 🤖 Tests Automatizados

### 1. Tests Unitarios de Repositories

Crear archivo: `src/test/java/edu/utn/inspt/cinearchive/backend/repositorio/CategoriaRepositoryTest.java`

```java
package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Categoria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {/* tu AppConfig */})
public class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    public void testFindAll() {
        List<Categoria> categorias = categoriaRepository.findAll();
        assertNotNull(categorias);
        assertTrue(categorias.size() > 0);
    }

    @Test
    public void testFindById() {
        Optional<Categoria> categoria = categoriaRepository.findById(1L);
        assertTrue(categoria.isPresent());
        assertEquals("Acción", categoria.get().getNombre());
    }

    @Test
    public void testFindByTipo() {
        List<Categoria> generos = categoriaRepository.findByTipo(Categoria.Tipo.GENERO);
        assertNotNull(generos);
        for (Categoria c : generos) {
            assertEquals(Categoria.Tipo.GENERO, c.getTipo());
        }
    }

    @Test
    public void testSaveCategoria() {
        Categoria nueva = new Categoria();
        nueva.setNombre("Test Categoría");
        nueva.setTipo(Categoria.Tipo.TAG);
        nueva.setDescripcion("Descripción de prueba");

        Categoria guardada = categoriaRepository.save(nueva);
        assertNotNull(guardada.getId());
        assertEquals("Test Categoría", guardada.getNombre());

        // Limpiar
        categoriaRepository.deleteById(guardada.getId());
    }

    @Test
    public void testExistsByNombre() {
        boolean existe = categoriaRepository.existsByNombre("Acción");
        assertTrue(existe);

        boolean noExiste = categoriaRepository.existsByNombre("NoExiste123");
        assertFalse(noExiste);
    }
}
```

### 2. Tests de Services

Crear archivo: `src/test/java/edu/utn/inspt/cinearchive/backend/servicio/ResenaServiceTest.java`

```java
package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Resena;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {/* tu AppConfig */})
public class ResenaServiceTest {

    @Autowired
    private ResenaService resenaService;

    @Test
    public void testObtenerPorContenido() {
        List<Resena> resenas = resenaService.obtenerPorContenido(5L);
        assertNotNull(resenas);
    }

    @Test
    public void testCalificacionPromedio() {
        Double promedio = resenaService.obtenerCalificacionPromedio(5L);
        assertNotNull(promedio);
        assertTrue(promedio >= 0.0 && promedio <= 5.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCrearResenaDuplicada() {
        // Asumiendo que ya existe una reseña del usuario 1 en contenido 5
        Resena resena = new Resena();
        resena.setUsuario(new Usuario(1L));
        resena.setContenido(new Contenido(5L));
        resena.setCalificacion(4.0);
        resena.setTitulo("Duplicado");
        resena.setTexto("Intentando duplicar");

        resenaService.crear(resena); // Debe lanzar excepción
    }
}
```

### 3. Tests de Controllers (API REST)

Crear archivo: `src/test/java/edu/utn/inspt/cinearchive/frontend/controlador/CategoriaControllerTest.java`

```java
package edu.utn.inspt.cinearchive.frontend.controlador;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {/* tu AppConfig */})
@WebAppConfiguration
public class CategoriaControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testListarCategorias() throws Exception {
        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testObtenerCategoriaPorId() throws Exception {
        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").exists());
    }

    @Test
    public void testCrearCategoria() throws Exception {
        String categoriaJson = "{\"nombre\":\"Test\",\"tipo\":\"GENERO\",\"descripcion\":\"Prueba\"}";

        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoriaJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }
}
```

### 4. Ejecutar Tests

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar tests de una clase específica
mvn test -Dtest=CategoriaRepositoryTest

# Ver reporte de coverage
mvn clean test jacoco:report
```

---

## ✅ Checklist Final de Testing

### Categorías

- [ ] API REST: Listar todas
- [ ] API REST: Obtener por ID
- [ ] API REST: Filtrar por tipo
- [ ] API REST: Crear nueva
- [ ] API REST: Actualizar existente
- [ ] API REST: Eliminar
- [ ] API REST: Validaciones (nombre único, tipo válido)
- [ ] Vista JSP: Cargar categorías
- [ ] Vista JSP: Crear desde formulario
- [ ] Vista JSP: Editar desde formulario
- [ ] Vista JSP: Eliminar con confirmación
- [ ] Vista JSP: Control de acceso (solo gestor)

### Reseñas

- [ ] API REST: Listar todas
- [ ] API REST: Obtener por usuario
- [ ] API REST: Obtener por contenido
- [ ] API REST: Obtener promedio de calificación
- [ ] API REST: Crear nueva reseña
- [ ] API REST: Actualizar reseña
- [ ] API REST: Eliminar reseña
- [ ] API REST: Validaciones (calificación 0-5, una por usuario/contenido)
- [ ] Vista: Mostrar reseñas en detalle de contenido
- [ ] Vista: Crear reseña desde detalle
- [ ] Vista: Editar reseña propia
- [ ] Vista: Eliminar reseña propia
- [ ] Vista: Restricción (no editar reseñas ajenas)

### Reportes y Analytics

- [ ] API REST: Dashboard (KPIs generales)
- [ ] API REST: Top contenidos
- [ ] API REST: Categorías populares
- [ ] API REST: Mejor calificados
- [ ] API REST: Análisis demográfico
- [ ] API REST: Rendimiento géneros
- [ ] API REST: Tendencias temporales
- [ ] API REST: Comportamiento usuarios
- [ ] API REST: Generar reporte (5 tipos)
- [ ] API REST: Listar reportes guardados
- [ ] API REST: Eliminar reporte
- [ ] Vista: Dashboard con KPIs
- [ ] Vista: Gráfico de top contenidos
- [ ] Vista: Gráfico de rendimiento géneros
- [ ] Vista: Gráfico de tendencias temporales
- [ ] Vista: Gráfico de análisis demográfico
- [ ] Vista: Filtros de periodo funcionan
- [ ] Vista: Generar reporte desde formulario
- [ ] Vista: Historial de reportes
- [ ] Vista: Exportar PDF
- [ ] Vista: Exportar Excel
- [ ] Vista: Exportar CSV
- [ ] Vista: Control de acceso (solo analista)

### Base de Datos

- [ ] Tabla categoria creada correctamente
- [ ] Tabla contenido_categoria creada
- [ ] Tabla resena creada con constraint único
- [ ] Tabla reporte creada
- [ ] Datos de prueba insertados
- [ ] Views creadas (4 views)
- [ ] Stored procedures creados (2 SPs)
- [ ] Índices optimizados

### Integración

- [ ] Usuario puede reseñar contenido alquilado
- [ ] Calificación promedio se calcula correctamente
- [ ] Analytics reflejan reseñas nuevas
- [ ] Categorías se integran con búsqueda de catálogo
- [ ] Reportes usan datos de todas las tablas
- [ ] Control de acceso por roles funciona
- [ ] Cambios en BD se reflejan en analytics inmediatamente

### Tests Automatizados

- [ ] Tests unitarios de CategoriaRepository
- [ ] Tests unitarios de ResenaRepository
- [ ] Tests unitarios de ReporteRepository
- [ ] Tests de CategoriaService
- [ ] Tests de ResenaService
- [ ] Tests de ReporteService
- [ ] Tests de Controllers (MockMvc)
- [ ] Todos los tests pasan

---

## 🐛 Problemas Comunes y Soluciones

### Problema 1: Error 404 en endpoints REST

**Síntoma:** `curl` retorna 404 Not Found

**Soluciones:**
1. Verificar que la aplicación está corriendo
2. Verificar la URL base: `/cinearchive/api/...`
3. Verificar `@RequestMapping` en controller
4. Verificar que Spring está escaneando el paquete

### Problema 2: Error 500 en creación de categoría

**Síntoma:** Error al insertar en BD

**Soluciones:**
1. Verificar que la tabla `categoria` existe
2. Verificar conexión a BD en `application.properties`
3. Ver logs de consola para detalles del error
4. Verificar que no hay nombre duplicado

### Problema 3: Gráficos no cargan en dashboard

**Síntoma:** Divs vacíos donde deberían ir gráficos

**Soluciones:**
1. Abrir DevTools Console → Ver errores de JavaScript
2. Verificar que Chart.js está cargado
3. Verificar que endpoints de analytics retornan datos
4. Verificar que `reportes.js` está ejecutándose

### Problema 4: Reseña duplicada no muestra error

**Síntoma:** Usuario puede crear múltiples reseñas del mismo contenido

**Soluciones:**
1. Verificar constraint UNIQUE en tabla `resena`
2. Verificar validación en `ResenaService`
3. Verificar que controller maneja código 409 Conflict

### Problema 5: Control de acceso no funciona

**Síntoma:** Usuario regular puede acceder a vistas de gestor/analista

**Soluciones:**
1. Verificar que `SecurityInterceptor` está registrado
2. Verificar que rutas están protegidas en interceptor
3. Verificar que sesión tiene el rol correcto
4. Ver logs para debug

---

## 📞 Contacto y Soporte

Si encuentras problemas durante el testing:

1. **Revisar logs de consola** (Maven/Jetty/Tomcat)
2. **Revisar logs de MySQL** (errores de queries)
3. **Usar DevTools** (Network tab, Console)
4. **Consultar documentación** del Plan de Delegación

---

## 🎉 Finalización del Testing

Una vez completado este checklist:

✅ Módulos del Developer 3 están 100% funcionales  
✅ APIs REST probadas y documentadas  
✅ Vistas JSP funcionando correctamente  
✅ Integración con otros módulos verificada  
✅ Base de datos optimizada y probada  
✅ Tests automatizados pasando  

**¡El Developer 3 ha completado exitosamente su parte del proyecto CineArchive!** 🎬⭐

---

**Elaborado por:** Developer 3 (Martín)  
**Fecha:** 12 de Noviembre de 2025  
**Versión:** 1.0

