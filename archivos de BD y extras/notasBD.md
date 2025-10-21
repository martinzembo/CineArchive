# Base de Datos CineArchive V2

## Análisis de Cambios desde V1

La nueva versión de CineArchive introduce cambios significativos que requieren una reestructuración completa de la base de datos:

### Cambios Principales:
- **Sistema de roles múltiples**: Ya no solo USER/ADMIN, ahora incluye Gestor de Inventario y Analista de Datos
- **Sistema de alquiler temporal**: Los usuarios alquilan contenido por períodos limitados (antes solo registraban lo visto)
- **Gestión de inventario**: Control de stock de licencias digitales y disponibilidad
- **Sistema de pagos**: Transacciones y historial de pagos
- **Reseñas independientes**: Las calificaciones ahora son reseñas completas con texto
- **Analytics y reportes**: Sistema de generación de reportes para análisis de datos

---

## Entidades Principales

### 1. Entidad Usuario
Almacena todos los usuarios del sistema con sus diferentes roles y permisos.

**Atributos:**
- `id` (Long): Identificador único (clave primaria)
- `nombre` (String): Nombre completo del usuario
- `email` (String): Correo electrónico único
- `contraseña` (String): Contraseña encriptada
- `rol` (ENUM): Rol del usuario
  - USUARIO_REGULAR → Cliente final que alquila contenido
  - ADMINISTRADOR → Gestión de usuarios y políticas
  - GESTOR_INVENTARIO → Gestión del catálogo
  - ANALISTA_DATOS → Generación de reportes (solo lectura)
- `fechaRegistro` (TIMESTAMP): Fecha de creación de la cuenta
- `activo` (BOOLEAN): Estado de la cuenta (activo/suspendido)
- `fechaNacimiento` (DATE): Fecha de nacimiento (para análisis demográfico)

### 2. Entidad Contenido
Almacena películas y series del catálogo. Utiliza estrategia SINGLE_TABLE para unificar ambos tipos.

**Atributos:**
- `id` (Long): Identificador único (clave primaria)
- `titulo` (VARCHAR[255]): Título del contenido
- `genero` (VARCHAR[100]): Género principal
- `año` (INT): Año de lanzamiento
- `descripcion` (TEXT): Sinopsis detallada
- `imagenUrl` (VARCHAR[500]): URL del poster
- `trailerUrl` (VARCHAR[500]): URL del trailer
- `tipo` (ENUM): PELICULA o SERIE
- `disponibleParaAlquiler` (BOOLEAN): Indica si está disponible actualmente
- `precioAlquiler` (DECIMAL[10,2]): Precio de alquiler
- `copiasDisponibles` (INT): Número de licencias digitales disponibles simultáneamente
- `copiasTotales` (INT): Total de licencias adquiridas
- `fechaVencimientoLicencia` (DATE): Fecha de vencimiento de la licencia del contenido
- `idApiExterna` (VARCHAR[100]): ID del contenido en API externa (TMDb, OMDb)
- `gestorInventarioId` (Long): FK del usuario que agregó el contenido

**Atributos específicos de Películas:**
- `duracion` (INT): Duración en minutos
- `director` (VARCHAR[150]): Nombre del director

**Atributos específicos de Series:**
- `temporadas` (INT): Número de temporadas
- `capitulosTotales` (INT): Número total de capítulos
- `enEmision` (BOOLEAN): Si está en emisión actualmente

### 3. Entidad Alquiler
Nueva entidad que gestiona los alquileres temporales de contenido por parte de usuarios.

**Atributos:**
- `id` (Long): Identificador único (clave primaria)
- `usuarioId` (Long): FK que referencia al usuario que alquila
- `contenidoId` (Long): FK que referencia al contenido alquilado
- `fechaInicio` (TIMESTAMP): Fecha y hora de inicio del alquiler
- `fechaFin` (TIMESTAMP): Fecha y hora de finalización del alquiler
- `periodoAlquiler` (INT): Duración del alquiler en días (ej: 3, 7, 14)
- `precio` (DECIMAL[10,2]): Precio pagado por el alquiler
- `estado` (ENUM): Estado del alquiler
  - ACTIVO → Alquiler vigente
  - FINALIZADO → Período terminado
  - CANCELADO → Cancelado antes de tiempo
- `visto` (BOOLEAN): Si el usuario marcó el contenido como visto
- `fechaVista` (DATE): Fecha en que el usuario lo vio

### 4. Entidad Reseña
Nueva entidad que permite a los usuarios escribir reseñas detalladas del contenido.

**Atributos:**
- `id` (Long): Identificador único (clave primaria)
- `usuarioId` (Long): FK del usuario que escribe la reseña
- `contenidoId` (Long): FK del contenido reseñado
- `calificacion` (DOUBLE): Calificación numérica (1.0 a 5.0)
- `titulo` (VARCHAR[255]): Título de la reseña
- `texto` (TEXT): Contenido de la reseña
- `fechaCreacion` (TIMESTAMP): Fecha de publicación
- `fechaModificacion` (TIMESTAMP): Última modificación

**Restricción**: Un usuario solo puede crear una reseña por contenido.

### 5. Entidad Lista
Listas personalizadas de contenido creadas por usuarios.

**Atributos:**
- `id` (Long): Identificador único (clave primaria)
- `nombre` (VARCHAR[255]): Nombre de la lista
- `descripcion` (TEXT): Descripción opcional
- `usuarioId` (Long): FK del usuario creador
- `publica` (BOOLEAN): Si es visible para otros usuarios
- `fechaCreacion` (TIMESTAMP): Fecha de creación

### 6. Tabla Intermedia ListaContenido
Relación muchos a muchos entre Listas y Contenido.

**Atributos:**
- `listaId` (Long): FK que referencia a Lista
- `contenidoId` (Long): FK que referencia a Contenido
- `orden` (INT): Orden del contenido en la lista
- `fechaAgregado` (TIMESTAMP): Cuándo se agregó a la lista

**Clave primaria compuesta**: (listaId, contenidoId)

### 7. Entidad Transaccion
Nueva entidad para historial de pagos y transacciones.

**Atributos:**
- `id` (Long): Identificador único (clave primaria)
- `usuarioId` (Long): FK del usuario que realiza el pago
- `alquilerId` (Long): FK del alquiler asociado
- `monto` (DECIMAL[10,2]): Monto pagado
- `metodoPago` (VARCHAR[50]): Método de pago utilizado
- `fechaTransaccion` (TIMESTAMP): Fecha y hora del pago
- `estado` (ENUM): COMPLETADA, PENDIENTE, FALLIDA, REEMBOLSADA
- `referenciaExterna` (VARCHAR[255]): ID de transacción del procesador de pagos

### 8. Entidad Reporte
Nueva entidad para almacenar reportes generados por Analistas de Datos.

**Atributos:**
- `id` (Long): Identificador único (clave primaria)
- `analistaId` (Long): FK del analista que generó el reporte
- `titulo` (VARCHAR[255]): Título del reporte
- `descripcion` (TEXT): Descripción del análisis
- `tipoReporte` (ENUM):
  - MAS_ALQUILADOS
  - ANALISIS_DEMOGRAFICO
  - RENDIMIENTO_GENEROS
  - TENDENCIAS_TEMPORALES
  - COMPORTAMIENTO_USUARIOS
- `parametros` (JSON/TEXT): Parámetros utilizados (fechas, filtros)
- `resultados` (JSON/TEXT): Datos del reporte
- `fechaGeneracion` (TIMESTAMP): Fecha de generación
- `periodoInicio` (DATE): Inicio del período analizado
- `periodoFin` (DATE): Fin del período analizado

### 9. Entidad Categoria
Nueva entidad para mejorar la gestión de géneros y categorías.

**Atributos:**
- `id` (Long): Identificador único (clave primaria)
- `nombre` (VARCHAR[100]): Nombre de la categoría/género
- `tipo` (ENUM): GENERO, TAG, CLASIFICACION
- `descripcion` (TEXT): Descripción de la categoría

### 10. Tabla Intermedia ContenidoCategoria
Relación muchos a muchos entre Contenido y Categorías.

**Atributos:**
- `contenidoId` (Long): FK que referencia a Contenido
- `categoriaId` (Long): FK que referencia a Categoria

**Clave primaria compuesta**: (contenidoId, categoriaId)

---

## Resumen de Relaciones

- **Usuario ↔ Alquiler**: Uno a muchos (Un usuario puede tener muchos alquileres activos o históricos)
- **Contenido ↔ Alquiler**: Uno a muchos (Un contenido puede ser alquilado por muchos usuarios según copias disponibles)
- **Usuario ↔ Reseña**: Uno a muchos (Un usuario puede escribir muchas reseñas, una por contenido)
- **Contenido ↔ Reseña**: Uno a muchos (Un contenido puede tener muchas reseñas de diferentes usuarios)
- **Usuario ↔ Lista**: Uno a muchos (Un usuario puede crear muchas listas)
- **Lista ↔ Contenido**: Muchos a muchos (tabla intermedia ListaContenido)
- **Usuario ↔ Transaccion**: Uno a muchos (Un usuario puede tener muchas transacciones)
- **Alquiler ↔ Transaccion**: Uno a uno (Cada alquiler tiene una transacción asociada)
- **Usuario (Analista) ↔ Reporte**: Uno a muchos (Un analista puede generar muchos reportes)
- **Contenido ↔ Categoria**: Muchos a muchos (tabla intermedia ContenidoCategoria)
- **Usuario (Gestor) ↔ Contenido**: Uno a muchos (Un gestor de inventario puede agregar múltiples contenidos al catálogo)

---

## Cambios Clave vs Versión Anterior

⚠️ **Principales diferencias con la BD anterior:**
- Sistema de roles expandido: De 2 roles (USER/ADMIN) a 4 roles especializados
- Nueva tabla Alquiler: Gestión temporal de contenido (antes solo registraban lo visto)
- Nueva tabla Transaccion: Historial de pagos y facturación
- Nueva tabla Reseña: Separada del Contenido para múltiples usuarios
- Nueva tabla Reporte: Almacenamiento de análisis de datos
- Nueva tabla Categoria: Gestión flexible de géneros y tags
- Gestión de stock: Campos `copiasDisponibles` y `copiasTotales` en Contenido
- Disponibilidad temporal: Control de licencias con fechas de vencimiento
- Integración con APIs: Campo `idApiExterna` para sincronización
- Análisis demográfico: Campo `fechaNacimiento` en Usuario

---

## Índices Recomendados

**Índices en Usuario:**
```sql
CREATE INDEX idx_usuario_email ON Usuario(email);
CREATE INDEX idx_usuario_rol ON Usuario(rol);
```

**Índices en Contenido:**
```sql
CREATE INDEX idx_contenido_tipo ON Contenido(tipo);
CREATE INDEX idx_contenido_disponible ON Contenido(disponibleParaAlquiler);
CREATE INDEX idx_contenido_genero ON Contenido(genero);
```

**Índices en Alquiler:**
```sql
CREATE INDEX idx_alquiler_usuario ON Alquiler(usuarioId);
CREATE INDEX idx_alquiler_contenido ON Alquiler(contenidoId);
CREATE INDEX idx_alquiler_estado ON Alquiler(estado);
CREATE INDEX idx_alquiler_fechas ON Alquiler(fechaInicio, fechaFin);
```

**Índices en Reseña:**
```sql
CREATE INDEX idx_resena_contenido ON Reseña(contenidoId);
CREATE INDEX idx_resena_usuario ON Reseña(usuarioId);
```

**Índices en Transaccion:**
```sql
CREATE INDEX idx_transaccion_usuario ON Transaccion(usuarioId);
CREATE INDEX idx_transaccion_fecha ON Transaccion(fechaTransaccion);
```

---

## Consideraciones de Implementación

### 1. Control de Concurrencia
El campo `copiasDisponibles` debe manejarse con transacciones ACID para evitar sobre-alquiler:

```java
@Transactional(isolation = Isolation.SERIALIZABLE)
public Alquiler crearAlquiler(Usuario usuario, Contenido contenido) {
    if (contenido.getCopiasDisponibles() > 0) {
        contenido.setCopiasDisponibles(contenido.getCopiasDisponibles() - 1);
        // Crear alquiler
    } else {
        throw new NoDisponibleException();
    }
}
```

### 2. Gestión de Políticas
Los Administradores definen políticas que afectan toda la aplicación:
- Períodos de alquiler disponibles (3, 7, 14 días)
- Precios base por tipo de contenido
- Límite de alquileres simultáneos por usuario

Estas políticas pueden almacenarse en una tabla `ConfiguracionSistema` o gestionarse mediante configuración de la aplicación.

### 3. Liberación Automática
Implementar un proceso programado (cron job) que:
- Finalice alquileres vencidos (`fechaFin` < now)
- Incremente `copiasDisponibles` del contenido
- Envíe notificaciones de vencimiento

### 4. Soft Delete
Considerar agregar campos `eliminado` (BOOLEAN) y `fechaEliminacion` para eliminación lógica en lugar de física.

---

## Ejemplos de Datos

**Usuario Regular:**
```json
{
  "id": 1,
  "nombre": "María García",
  "email": "maria@example.com",
  "rol": "USUARIO_REGULAR",
  "activo": true,
  "fechaNacimiento": "1995-03-15"
}
```

**Contenido (Película):**
```json
{
  "id": 101,
  "titulo": "Inception",
  "genero": "Ciencia Ficción",
  "año": 2010,
  "tipo": "PELICULA",
  "disponibleParaAlquiler": true,
  "precioAlquiler": 4.99,
  "copiasDisponibles": 8,
  "copiasTotales": 10,
  "duracion": 148,
  "director": "Christopher Nolan"
}
```

**Alquiler:**
```json
{
  "id": 501,
  "usuarioId": 1,
  "contenidoId": 101,
  "fechaInicio": "2025-10-21T16:00:00",
  "fechaFin": "2025-10-28T16:00:00",
  "periodoAlquiler": 7,
  "precio": 4.99,
  "estado": "ACTIVO",
  "visto": false
}
```

**Reseña:**
```json
{
  "id": 301,
  "usuarioId": 1,
  "contenidoId": 101,
  "calificacion": 5.0,
  "titulo": "¡Una obra maestra!",
  "texto": "Increíble película que te hace pensar...",
  "fechaCreacion": "2025-10-28T18:30:00"
}
```

**Transacción:**
```json
{
  "id": 701,
  "usuarioId": 1,
  "alquilerId": 501,
  "monto": 4.99,
  "metodoPago": "Tarjeta de Crédito",
  "estado": "COMPLETADA",
  "fechaTransaccion": "2025-10-21T16:00:00"
}
```