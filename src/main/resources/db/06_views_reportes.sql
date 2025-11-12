-- =====================================================
-- Script: 06_views_reportes.sql
-- Desarrollador: Developer 3 (Martin)
-- Descripción: Views y stored procedures para reportes y analytics
-- Fecha: 2025-11-10
-- =====================================================

USE cinearchive_v2;

-- =====================================================
-- VIEWS PARA ANALYTICS Y REPORTES
-- =====================================================

-- =====================================================
-- VIEW: vista_contenido_estadisticas
-- Resumen estadístico de cada contenido
-- =====================================================
DROP VIEW IF EXISTS vista_contenido_estadisticas;

CREATE VIEW vista_contenido_estadisticas AS
SELECT
    c.id,
    c.titulo,
    c.tipo,
    c.genero,
    c.anio,
    c.disponible_para_alquiler,
    c.precio_alquiler,
    c.copias_disponibles,
    c.copias_totales,

    -- Estadísticas de alquileres
    COALESCE(alq.total_alquileres, 0) as total_alquileres,
    COALESCE(alq.ingresos_totales, 0.00) as ingresos_totales,
    COALESCE(alq.ultimo_alquiler, NULL) as ultimo_alquiler,

    -- Estadísticas de reseñas
    COALESCE(res.total_resenas, 0) as total_resenas,
    COALESCE(res.calificacion_promedio, 0.0) as calificacion_promedio,
    COALESCE(res.calificacion_maxima, 0.0) as calificacion_maxima,
    COALESCE(res.calificacion_minima, 0.0) as calificacion_minima,

    -- Popularidad (combinación de alquileres y reseñas)
    (COALESCE(alq.total_alquileres, 0) * 0.7 + COALESCE(res.calificacion_promedio, 0) * COALESCE(res.total_resenas, 0) * 0.3) as indice_popularidad

FROM contenido c

-- Subquery para estadísticas de alquileres
LEFT JOIN (
    SELECT
        contenido_id,
        COUNT(*) as total_alquileres,
        SUM(precio) as ingresos_totales,
        MAX(fecha_alquiler) as ultimo_alquiler
    FROM alquiler
    GROUP BY contenido_id
) alq ON c.id = alq.contenido_id

-- Subquery para estadísticas de reseñas
LEFT JOIN (
    SELECT
        contenido_id,
        COUNT(*) as total_resenas,
        AVG(calificacion) as calificacion_promedio,
        MAX(calificacion) as calificacion_maxima,
        MIN(calificacion) as calificacion_minima
    FROM resena
    GROUP BY contenido_id
) res ON c.id = res.contenido_id;

-- =====================================================
-- VIEW: vista_usuarios_comportamiento
-- Análisis de comportamiento de usuarios
-- =====================================================
DROP VIEW IF EXISTS vista_usuarios_comportamiento;

CREATE VIEW vista_usuarios_comportamiento AS
SELECT
    u.id,
    u.nombre,
    u.email,
    u.genero,
    FLOOR(DATEDIFF(CURDATE(), u.fecha_nacimiento) / 365.25) as edad,
    u.rol,

    -- Estadísticas de alquileres
    COALESCE(alq.total_alquileres, 0) as total_alquileres,
    COALESCE(alq.gasto_total, 0.00) as gasto_total,
    COALESCE(alq.gasto_promedio, 0.00) as gasto_promedio,
    COALESCE(alq.primer_alquiler, NULL) as primer_alquiler,
    COALESCE(alq.ultimo_alquiler, NULL) as ultimo_alquiler,

    -- Estadísticas de reseñas
    COALESCE(res.total_resenas, 0) as total_resenas,
    COALESCE(res.calificacion_promedio_dada, 0.0) as calificacion_promedio_dada,

    -- Clasificación de usuario
    CASE
        WHEN COALESCE(alq.total_alquileres, 0) >= 50 THEN 'VIP'
        WHEN COALESCE(alq.total_alquileres, 0) >= 20 THEN 'PREMIUM'
        WHEN COALESCE(alq.total_alquileres, 0) >= 5 THEN 'REGULAR'
        WHEN COALESCE(alq.total_alquileres, 0) > 0 THEN 'NUEVO'
        ELSE 'INACTIVO'
    END as clasificacion_usuario,

    -- Días desde último alquiler
    CASE
        WHEN alq.ultimo_alquiler IS NOT NULL THEN DATEDIFF(CURDATE(), alq.ultimo_alquiler)
        ELSE NULL
    END as dias_desde_ultimo_alquiler

FROM usuario u

-- Subquery para estadísticas de alquileres
LEFT JOIN (
    SELECT
        usuario_id,
        COUNT(*) as total_alquileres,
        SUM(precio) as gasto_total,
        AVG(precio) as gasto_promedio,
        MIN(fecha_alquiler) as primer_alquiler,
        MAX(fecha_alquiler) as ultimo_alquiler
    FROM alquiler
    GROUP BY usuario_id
) alq ON u.id = alq.usuario_id

-- Subquery para estadísticas de reseñas
LEFT JOIN (
    SELECT
        usuario_id,
        COUNT(*) as total_resenas,
        AVG(calificacion) as calificacion_promedio_dada
    FROM resena
    GROUP BY usuario_id
) res ON u.id = res.usuario_id;

-- =====================================================
-- VIEW: vista_categorias_populares
-- Popularidad de categorías basada en alquileres
-- =====================================================
DROP VIEW IF EXISTS vista_categorias_populares;

CREATE VIEW vista_categorias_populares AS
SELECT
    cat.id,
    cat.nombre,
    cat.tipo,
    cat.descripcion,

    -- Estadísticas de contenidos
    COUNT(DISTINCT cc.contenido_id) as contenidos_asociados,

    -- Estadísticas de alquileres
    COALESCE(SUM(alq_stats.total_alquileres), 0) as total_alquileres,
    COALESCE(SUM(alq_stats.ingresos_totales), 0.00) as ingresos_totales,

    -- Estadísticas de reseñas
    COALESCE(AVG(res_stats.calificacion_promedio), 0.0) as calificacion_promedio_categoria,
    COALESCE(SUM(res_stats.total_resenas), 0) as total_resenas_categoria,

    -- Índice de popularidad de la categoría
    (COALESCE(SUM(alq_stats.total_alquileres), 0) * 0.6 +
     COALESCE(AVG(res_stats.calificacion_promedio), 0) * COALESCE(SUM(res_stats.total_resenas), 0) * 0.4) as indice_popularidad_categoria

FROM categoria cat
LEFT JOIN contenido_categoria cc ON cat.id = cc.categoria_id
LEFT JOIN contenido c ON cc.contenido_id = c.id

-- Estadísticas de alquileres por contenido
LEFT JOIN (
    SELECT
        contenido_id,
        COUNT(*) as total_alquileres,
        SUM(precio) as ingresos_totales
    FROM alquiler
    GROUP BY contenido_id
) alq_stats ON c.id = alq_stats.contenido_id

-- Estadísticas de reseñas por contenido
LEFT JOIN (
    SELECT
        contenido_id,
        COUNT(*) as total_resenas,
        AVG(calificacion) as calificacion_promedio
    FROM resena
    GROUP BY contenido_id
) res_stats ON c.id = res_stats.contenido_id

GROUP BY cat.id, cat.nombre, cat.tipo, cat.descripcion
ORDER BY indice_popularidad_categoria DESC;

-- =====================================================
-- VIEW: vista_tendencias_temporales
-- Tendencias de alquileres por mes/año
-- =====================================================
DROP VIEW IF EXISTS vista_tendencias_temporales;

CREATE VIEW vista_tendencias_temporales AS
SELECT
    YEAR(a.fecha_alquiler) as anio,
    MONTH(a.fecha_alquiler) as mes,
    DATE_FORMAT(a.fecha_alquiler, '%Y-%m') as periodo,
    DATE_FORMAT(a.fecha_alquiler, '%M %Y') as periodo_texto,

    -- Estadísticas básicas
    COUNT(*) as total_alquileres,
    COUNT(DISTINCT a.usuario_id) as usuarios_unicos,
    COUNT(DISTINCT a.contenido_id) as contenidos_alquilados,

    -- Estadísticas financieras
    SUM(a.precio) as ingresos_totales,
    AVG(a.precio) as precio_promedio,
    MIN(a.precio) as precio_minimo,
    MAX(a.precio) as precio_maximo,

    -- Distribución por tipo de contenido
    SUM(CASE WHEN c.tipo = 'PELICULA' THEN 1 ELSE 0 END) as alquileres_peliculas,
    SUM(CASE WHEN c.tipo = 'SERIE' THEN 1 ELSE 0 END) as alquileres_series,

    -- Distribución por estado de alquiler
    SUM(CASE WHEN a.estado = 'ACTIVO' THEN 1 ELSE 0 END) as alquileres_activos,
    SUM(CASE WHEN a.estado = 'DEVUELTO' THEN 1 ELSE 0 END) as alquileres_devueltos,
    SUM(CASE WHEN a.estado = 'VENCIDO' THEN 1 ELSE 0 END) as alquileres_vencidos

FROM alquiler a
INNER JOIN contenido c ON a.contenido_id = c.id
GROUP BY YEAR(a.fecha_alquiler), MONTH(a.fecha_alquiler)
ORDER BY anio DESC, mes DESC;

-- =====================================================
-- VIEW: vista_dashboard_general
-- Vista consolidada para el dashboard principal
-- =====================================================
DROP VIEW IF EXISTS vista_dashboard_general;

CREATE VIEW vista_dashboard_general AS
SELECT
    -- Estadísticas de contenido
    (SELECT COUNT(*) FROM contenido) as total_contenidos,
    (SELECT COUNT(*) FROM contenido WHERE disponible_para_alquiler = true) as contenidos_disponibles,
    (SELECT COUNT(*) FROM contenido WHERE tipo = 'PELICULA') as total_peliculas,
    (SELECT COUNT(*) FROM contenido WHERE tipo = 'SERIE') as total_series,

    -- Estadísticas de usuarios
    (SELECT COUNT(*) FROM usuario) as total_usuarios,
    (SELECT COUNT(*) FROM usuario WHERE rol = 'CLIENTE') as total_clientes,
    (SELECT COUNT(*) FROM usuario WHERE rol = 'GESTOR_INVENTARIO') as total_gestores,
    (SELECT COUNT(*) FROM usuario WHERE rol = 'ANALISTA_DATOS') as total_analistas,

    -- Estadísticas de alquileres
    (SELECT COUNT(*) FROM alquiler) as total_alquileres,
    (SELECT COUNT(*) FROM alquiler WHERE estado = 'ACTIVO') as alquileres_activos,
    (SELECT SUM(precio) FROM alquiler) as ingresos_totales,
    (SELECT AVG(precio) FROM alquiler) as precio_promedio_alquiler,

    -- Estadísticas de reseñas
    (SELECT COUNT(*) FROM resena) as total_resenas,
    (SELECT AVG(calificacion) FROM resena) as calificacion_promedio_global,

    -- Estadísticas de categorías
    (SELECT COUNT(*) FROM categoria) as total_categorias,
    (SELECT COUNT(*) FROM categoria WHERE tipo = 'GENERO') as total_generos,
    (SELECT COUNT(*) FROM categoria WHERE tipo = 'TAG') as total_tags,

    -- Estadísticas del mes actual
    (SELECT COUNT(*) FROM alquiler WHERE MONTH(fecha_alquiler) = MONTH(CURDATE()) AND YEAR(fecha_alquiler) = YEAR(CURDATE())) as alquileres_mes_actual,
    (SELECT SUM(precio) FROM alquiler WHERE MONTH(fecha_alquiler) = MONTH(CURDATE()) AND YEAR(fecha_alquiler) = YEAR(CURDATE())) as ingresos_mes_actual,

    -- Última actualización
    NOW() as fecha_actualizacion;

-- =====================================================
-- STORED PROCEDURES PARA REPORTES COMPLEJOS
-- =====================================================

-- =====================================================
-- PROCEDURE: sp_reporte_contenidos_mas_alquilados
-- =====================================================
DROP PROCEDURE IF EXISTS sp_reporte_contenidos_mas_alquilados;

DELIMITER //
CREATE PROCEDURE sp_reporte_contenidos_mas_alquilados(
    IN p_fecha_inicio DATE,
    IN p_fecha_fin DATE,
    IN p_limite INT
)
BEGIN
    SELECT
        c.id,
        c.titulo,
        c.tipo,
        c.genero,
        c.anio,
        COUNT(a.id) as total_alquileres,
        SUM(a.precio) as ingresos_totales,
        AVG(a.precio) as precio_promedio,
        AVG(r.calificacion) as calificacion_promedio,
        COUNT(DISTINCT r.id) as total_resenas,
        MAX(a.fecha_alquiler) as ultimo_alquiler
    FROM contenido c
    INNER JOIN alquiler a ON c.id = a.contenido_id
    LEFT JOIN resena r ON c.id = r.contenido_id
    WHERE a.fecha_alquiler BETWEEN p_fecha_inicio AND p_fecha_fin
    GROUP BY c.id, c.titulo, c.tipo, c.genero, c.anio
    ORDER BY total_alquileres DESC, ingresos_totales DESC
    LIMIT p_limite;
END //
DELIMITER ;

-- =====================================================
-- PROCEDURE: sp_analisis_demografico
-- =====================================================
DROP PROCEDURE IF EXISTS sp_analisis_demografico;

DELIMITER //
CREATE PROCEDURE sp_analisis_demografico(
    IN p_fecha_inicio DATE,
    IN p_fecha_fin DATE
)
BEGIN
    SELECT
        u.genero,
        CASE
            WHEN FLOOR(DATEDIFF(CURDATE(), u.fecha_nacimiento) / 365.25) < 18 THEN 'Menor de 18'
            WHEN FLOOR(DATEDIFF(CURDATE(), u.fecha_nacimiento) / 365.25) BETWEEN 18 AND 25 THEN '18-25'
            WHEN FLOOR(DATEDIFF(CURDATE(), u.fecha_nacimiento) / 365.25) BETWEEN 26 AND 35 THEN '26-35'
            WHEN FLOOR(DATEDIFF(CURDATE(), u.fecha_nacimiento) / 365.25) BETWEEN 36 AND 45 THEN '36-45'
            WHEN FLOOR(DATEDIFF(CURDATE(), u.fecha_nacimiento) / 365.25) BETWEEN 46 AND 55 THEN '46-55'
            ELSE 'Mayor de 55'
        END as rango_edad,
        COUNT(DISTINCT u.id) as total_usuarios,
        COUNT(a.id) as total_alquileres,
        SUM(a.precio) as gasto_total,
        AVG(a.precio) as gasto_promedio,
        AVG(r.calificacion) as calificacion_promedio_dada
    FROM usuario u
    INNER JOIN alquiler a ON u.id = a.usuario_id
    LEFT JOIN resena r ON u.id = r.usuario_id
    WHERE a.fecha_alquiler BETWEEN p_fecha_inicio AND p_fecha_fin
    GROUP BY u.genero, rango_edad
    ORDER BY total_alquileres DESC;
END //
DELIMITER ;

-- =====================================================
-- PROCEDURE: sp_rendimiento_generos
-- =====================================================
DROP PROCEDURE IF EXISTS sp_rendimiento_generos;

DELIMITER //
CREATE PROCEDURE sp_rendimiento_generos(
    IN p_fecha_inicio DATE,
    IN p_fecha_fin DATE
)
BEGIN
    SELECT
        c.genero,
        COUNT(DISTINCT c.id) as contenidos_diferentes,
        COUNT(a.id) as total_alquileres,
        SUM(a.precio) as ingresos_totales,
        AVG(a.precio) as precio_promedio,
        AVG(r.calificacion) as calificacion_promedio,
        COUNT(DISTINCT r.id) as total_resenas,
        COUNT(DISTINCT a.usuario_id) as usuarios_unicos
    FROM contenido c
    INNER JOIN alquiler a ON c.id = a.contenido_id
    LEFT JOIN resena r ON c.id = r.contenido_id
    WHERE a.fecha_alquiler BETWEEN p_fecha_inicio AND p_fecha_fin
        AND c.genero IS NOT NULL
    GROUP BY c.genero
    ORDER BY ingresos_totales DESC;
END //
DELIMITER ;

-- =====================================================
-- FUNCTIONS AUXILIARES
-- =====================================================

-- =====================================================
-- FUNCTION: fn_calcular_popularidad_contenido
-- =====================================================
DROP FUNCTION IF EXISTS fn_calcular_popularidad_contenido;

DELIMITER //
CREATE FUNCTION fn_calcular_popularidad_contenido(p_contenido_id BIGINT)
RETURNS DECIMAL(10,2)
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE v_total_alquileres INT DEFAULT 0;
    DECLARE v_calificacion_promedio DECIMAL(3,1) DEFAULT 0.0;
    DECLARE v_total_resenas INT DEFAULT 0;
    DECLARE v_popularidad DECIMAL(10,2) DEFAULT 0.0;

    -- Obtener estadísticas de alquileres
    SELECT COUNT(*) INTO v_total_alquileres
    FROM alquiler
    WHERE contenido_id = p_contenido_id;

    -- Obtener estadísticas de reseñas
    SELECT COUNT(*), COALESCE(AVG(calificacion), 0)
    INTO v_total_resenas, v_calificacion_promedio
    FROM resena
    WHERE contenido_id = p_contenido_id;

    -- Calcular índice de popularidad
    SET v_popularidad = (v_total_alquileres * 0.7) + (v_calificacion_promedio * v_total_resenas * 0.3);

    RETURN v_popularidad;
END //
DELIMITER ;

-- =====================================================
-- TRIGGERS PARA AUDITORÍA
-- =====================================================

-- Trigger para auditar cambios en reportes
DROP TRIGGER IF EXISTS tr_reporte_audit;

DELIMITER //
CREATE TRIGGER tr_reporte_audit
    AFTER INSERT ON reporte
    FOR EACH ROW
BEGIN
    INSERT INTO auditoria (tabla, operacion, registro_id, usuario_id, fecha_operacion, detalles)
    VALUES ('reporte', 'INSERT', NEW.id, NEW.analista_id, NOW(),
            CONCAT('Nuevo reporte creado: ', NEW.titulo, ' - Tipo: ', NEW.tipo_reporte));
END //
DELIMITER ;

-- =====================================================
-- ÍNDICES ADICIONALES PARA OPTIMIZACIÓN
-- =====================================================

-- Índices compuestos para queries de reportes
CREATE INDEX idx_alquiler_fecha_contenido ON alquiler(fecha_alquiler, contenido_id);
CREATE INDEX idx_alquiler_fecha_usuario ON alquiler(fecha_alquiler, usuario_id);
CREATE INDEX idx_resena_contenido_calificacion ON resena(contenido_id, calificacion);
CREATE INDEX idx_usuario_demografia ON usuario(genero, fecha_nacimiento);

-- =====================================================
-- VERIFICACIONES FINALES
-- =====================================================

-- Verificar que las vistas se crearon correctamente
SELECT
    TABLE_NAME as Vista,
    TABLE_COMMENT as Descripcion
FROM
    INFORMATION_SCHEMA.TABLES
WHERE
    TABLE_SCHEMA = 'cinearchive_v2'
    AND TABLE_TYPE = 'VIEW'
    AND TABLE_NAME LIKE 'vista_%'
ORDER BY
    TABLE_NAME;

-- Verificar stored procedures
SELECT
    ROUTINE_NAME as Procedimiento,
    ROUTINE_TYPE as Tipo,
    CREATED as Fecha_Creacion
FROM
    INFORMATION_SCHEMA.ROUTINES
WHERE
    ROUTINE_SCHEMA = 'cinearchive_v2'
    AND ROUTINE_NAME LIKE 'sp_%'
ORDER BY
    ROUTINE_NAME;

-- Mostrar ejemplo de uso de las vistas
SELECT 'Ejemplo de uso de vistas:' as Mensaje;
SELECT 'SELECT * FROM vista_dashboard_general;' as Consulta_Dashboard;
SELECT 'SELECT * FROM vista_contenido_estadisticas ORDER BY indice_popularidad DESC LIMIT 10;' as Top_Contenidos;
SELECT 'SELECT * FROM vista_categorias_populares ORDER BY indice_popularidad_categoria DESC;' as Categorias_Populares;

-- =====================================================
-- COMENTARIOS FINALES
-- =====================================================

/*
VIEWS CREADAS:
1. vista_contenido_estadisticas - Estadísticas completas por contenido
2. vista_usuarios_comportamiento - Análisis de comportamiento de usuarios
3. vista_categorias_populares - Popularidad de categorías
4. vista_tendencias_temporales - Tendencias por mes/año
5. vista_dashboard_general - Resumen ejecutivo del sistema

STORED PROCEDURES:
1. sp_reporte_contenidos_mas_alquilados - Top contenidos por período
2. sp_analisis_demografico - Análisis por edad y género
3. sp_rendimiento_generos - Performance por géneros

FUNCTIONS:
1. fn_calcular_popularidad_contenido - Índice de popularidad

OPTIMIZACIONES:
- Índices compuestos para queries frecuentes
- Triggers de auditoría para reportes
- Vistas materializadas para consultas complejas

PRÓXIMOS PASOS:
- Ejecutar este script después de 05_categorias_resenas.sql
- Probar las vistas con datos reales
- Configurar jobs para actualizar estadísticas periódicamente
*/
