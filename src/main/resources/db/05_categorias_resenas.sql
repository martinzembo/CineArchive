-- =====================================================
-- Script: 05_categorias_resenas.sql
-- Desarrollador: Developer 3 (Martin)
-- Descripción: Creación de tablas para categorías, reseñas y reportes
-- Fecha: 2025-11-10
-- =====================================================

USE cinearchive_v2;

-- =====================================================
-- TABLA: categoria
-- =====================================================
DROP TABLE IF EXISTS categoria;

CREATE TABLE categoria (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    tipo ENUM('GENERO', 'TAG', 'CLASIFICACION') NOT NULL,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_categoria_tipo (tipo),
    INDEX idx_categoria_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: contenido_categoria (relación muchos a muchos)
-- =====================================================
DROP TABLE IF EXISTS contenido_categoria;

CREATE TABLE contenido_categoria (
    contenido_id BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (contenido_id, categoria_id),
    FOREIGN KEY (contenido_id) REFERENCES contenido(id) ON DELETE CASCADE,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id) ON DELETE CASCADE,

    INDEX idx_contenido_categoria_contenido (contenido_id),
    INDEX idx_contenido_categoria_categoria (categoria_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: resena
-- =====================================================
DROP TABLE IF EXISTS resena;

CREATE TABLE resena (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    contenido_id BIGINT NOT NULL,
    calificacion DECIMAL(2,1) NOT NULL CHECK (calificacion >= 0.0 AND calificacion <= 5.0),
    titulo VARCHAR(100) NOT NULL,
    texto TEXT NOT NULL,
    fecha_creacion DATE NOT NULL,
    fecha_modificacion DATE NULL,

    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (contenido_id) REFERENCES contenido(id) ON DELETE CASCADE,

    UNIQUE KEY unique_usuario_contenido (usuario_id, contenido_id),
    INDEX idx_resena_usuario (usuario_id),
    INDEX idx_resena_contenido (contenido_id),
    INDEX idx_resena_calificacion (calificacion),
    INDEX idx_resena_fecha_creacion (fecha_creacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: reporte
-- =====================================================
DROP TABLE IF EXISTS reporte;

CREATE TABLE reporte (
    id INT PRIMARY KEY AUTO_INCREMENT,
    analista_id INT NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    descripcion TEXT,
    tipo_reporte ENUM('MAS_ALQUILADOS', 'ANALISIS_DEMOGRAFICO', 'RENDIMIENTO_GENEROS', 'TENDENCIAS_TEMPORALES', 'COMPORTAMIENTO_USUARIOS') NOT NULL,
    parametros JSON,
    resultados LONGTEXT,
    fecha_generacion DATE NOT NULL,
    periodo_inicio DATE,
    periodo_fin DATE,

    FOREIGN KEY (analista_id) REFERENCES usuario(id) ON DELETE CASCADE,

    INDEX idx_reporte_analista (analista_id),
    INDEX idx_reporte_tipo (tipo_reporte),
    INDEX idx_reporte_fecha_generacion (fecha_generacion),
    INDEX idx_reporte_periodo (periodo_inicio, periodo_fin)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- DATOS DE PRUEBA: Categorías
-- =====================================================

-- Géneros cinematográficos
INSERT INTO categoria (nombre, tipo, descripcion) VALUES
('Acción', 'GENERO', 'Películas y series con secuencias de acción, persecuciones y aventuras'),
('Drama', 'GENERO', 'Contenido centrado en conflictos humanos y emocionales'),
('Comedia', 'GENERO', 'Entretenimiento diseñado para provocar risa y diversión'),
('Terror', 'GENERO', 'Contenido diseñado para generar miedo y suspenso'),
('Ciencia Ficción', 'GENERO', 'Historias basadas en conceptos científicos futuristas'),
('Romance', 'GENERO', 'Contenido centrado en relaciones amorosas'),
('Thriller', 'GENERO', 'Contenido de suspenso y tensión psicológica'),
('Fantasía', 'GENERO', 'Historias con elementos mágicos y sobrenaturales'),
('Documentales', 'GENERO', 'Contenido educativo y de no ficción'),
('Animación', 'GENERO', 'Contenido creado mediante técnicas de animación'),
('Crimen', 'GENERO', 'Historias relacionadas con actividades criminales'),
('Aventura', 'GENERO', 'Contenido con exploraciones y viajes épicos'),
('Musical', 'GENERO', 'Contenido donde la música es elemento central'),
('Western', 'GENERO', 'Historias ambientadas en el Oeste americano'),
('Guerra', 'GENERO', 'Contenido sobre conflictos bélicos');

-- Tags temáticos
INSERT INTO categoria (nombre, tipo, descripcion) VALUES
('Superhéroes', 'TAG', 'Contenido sobre personajes con superpoderes'),
('Zombies', 'TAG', 'Historias que involucran muertos vivientes'),
('Aliens', 'TAG', 'Contenido sobre extraterrestres'),
('Espías', 'TAG', 'Historias de espionaje y agentes secretos'),
('Vampiros', 'TAG', 'Contenido sobre vampiros y criaturas nocturnas'),
('Robots', 'TAG', 'Historias que involucran inteligencia artificial'),
('Viajes en el Tiempo', 'TAG', 'Contenido sobre viajes temporales'),
('Apocalipsis', 'TAG', 'Historias sobre el fin del mundo'),
('Magia', 'TAG', 'Contenido con elementos mágicos'),
('Deportes', 'TAG', 'Historias centradas en actividades deportivas'),
('Medicina', 'TAG', 'Contenido sobre hospitales y médicos'),
('Policíaco', 'TAG', 'Historias sobre investigación criminal'),
('Familia', 'TAG', 'Contenido apto para toda la familia'),
('Histórico', 'TAG', 'Basado en eventos históricos reales'),
('Biografía', 'TAG', 'Historias de vida de personas reales');

-- Clasificaciones de edad
INSERT INTO categoria (nombre, tipo, descripcion) VALUES
('ATP', 'CLASIFICACION', 'Apto para todo público'),
('PG-13', 'CLASIFICACION', 'Se sugiere orientación parental para menores de 13 años'),
('+16', 'CLASIFICACION', 'Solo para mayores de 16 años'),
('+18', 'CLASIFICACION', 'Solo para mayores de 18 años'),
('SAM 13', 'CLASIFICACION', 'Solo para mayores de 13 años'),
('R', 'CLASIFICACION', 'Contenido restringido');

-- =====================================================
-- DATOS DE PRUEBA: Reseñas (requiere contenido existente)
-- =====================================================

-- Nota: Estas reseñas se insertarán después de que existan contenidos
-- Por ahora creamos la estructura, los datos se pueden insertar posteriormente

-- Ejemplo de estructura de inserción (comentado para evitar errores):
/*
INSERT INTO resena (usuario_id, contenido_id, calificacion, titulo, texto, fecha_creacion) VALUES
(1, 1, 4.5, 'Excelente película', 'Una obra maestra del cine moderno con actuaciones excepcionales.', '2024-01-15'),
(2, 1, 4.0, 'Muy buena', 'Entretenida y bien dirigida, aunque predecible en algunos momentos.', '2024-01-16'),
(3, 2, 5.0, 'Perfecta', 'No puedo encontrar ningún defecto. Una serie que marca época.', '2024-01-20'),
(1, 3, 3.5, 'Regular', 'Tiene buenos momentos pero le falta profundidad en los personajes.', '2024-01-25'),
(4, 2, 4.8, 'Casi perfecta', 'Increíble desarrollo de personajes y narrativa envolvente.', '2024-02-01');
*/

-- =====================================================
-- DATOS DE PRUEBA: Reportes (ejemplos)
-- =====================================================

-- Ejemplo de reporte de contenidos más alquilados
INSERT INTO reporte (analista_id, titulo, descripcion, tipo_reporte, parametros, resultados, fecha_generacion, periodo_inicio, periodo_fin) VALUES
(1, 'Top 10 Contenidos Más Alquilados - Enero 2024', 'Reporte mensual de los contenidos con mayor demanda', 'MAS_ALQUILADOS',
'{"fechaInicio": "2024-01-01", "fechaFin": "2024-01-31", "limite": 10}',
'{"datos": [{"titulo": "Ejemplo Película", "total_alquileres": 150, "ingresos": 750.00}]}',
'2024-02-01', '2024-01-01', '2024-01-31');

-- Ejemplo de reporte demográfico
INSERT INTO reporte (analista_id, titulo, descripcion, tipo_reporte, parametros, resultados, fecha_generacion, periodo_inicio, periodo_fin) VALUES
(1, 'Análisis Demográfico Q1 2024', 'Análisis de comportamiento por demografía de usuarios', 'ANALISIS_DEMOGRAFICO',
'{"fechaInicio": "2024-01-01", "fechaFin": "2024-03-31"}',
'{"datos": [{"genero": "M", "rango_edad": 25, "total_alquileres": 200}]}',
'2024-04-01', '2024-01-01', '2024-03-31');

-- =====================================================
-- VERIFICACIÓN DE INTEGRIDAD
-- =====================================================

-- Verificar que las tablas se crearon correctamente
SELECT
    TABLE_NAME,
    TABLE_ROWS,
    CREATE_TIME
FROM
    INFORMATION_SCHEMA.TABLES
WHERE
    TABLE_SCHEMA = 'cinearchive_v2'
    AND TABLE_NAME IN ('categoria', 'contenido_categoria', 'resena', 'reporte')
ORDER BY
    TABLE_NAME;

-- Mostrar estadísticas de datos insertados
SELECT 'Categorías creadas' as Tabla, COUNT(*) as Total FROM categoria
UNION ALL
SELECT 'Géneros' as Tabla, COUNT(*) as Total FROM categoria WHERE tipo = 'GENERO'
UNION ALL
SELECT 'Tags' as Tabla, COUNT(*) as Total FROM categoria WHERE tipo = 'TAG'
UNION ALL
SELECT 'Clasificaciones' as Tabla, COUNT(*) as Total FROM categoria WHERE tipo = 'CLASIFICACION'
UNION ALL
SELECT 'Reseñas' as Tabla, COUNT(*) as Total FROM resena
UNION ALL
SELECT 'Reportes' as Tabla, COUNT(*) as Total FROM reporte;

-- =====================================================
-- COMENTARIOS FINALES
-- =====================================================

/*
INSTRUCCIONES DE USO:

1. Este script crea las tablas necesarias para el módulo de Developer 3
2. Incluye datos de prueba para categorías (45 categorías en total)
3. Las reseñas se pueden agregar después de tener contenidos
4. Los reportes incluyen ejemplos de estructura JSON
5. Todas las tablas tienen índices optimizados para consultas

DEPENDENCIAS:
- Requiere que existan las tablas: usuario, contenido
- Se ejecuta después de los scripts de Developer 1 y Developer 2

PRÓXIMOS PASOS:
- Ejecutar 06_views_reportes.sql para crear vistas de analytics
- Insertar contenidos reales para poder agregar más reseñas
- Configurar API keys para importación desde TMDb/OMDb
*/
