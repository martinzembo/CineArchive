-- Índices recomendados para optimizar búsquedas del catálogo y joins frecuentes

-- Índice por título para LIKE (prefijo) - considerar FULLTEXT si se requiere más adelante
CREATE INDEX idx_contenido_titulo ON contenido(titulo);

-- Índice por genero (para filtro exacto / equality)
CREATE INDEX idx_contenido_genero ON contenido(genero);

-- Índice por tipo
CREATE INDEX idx_contenido_tipo ON contenido(tipo);

-- Índice compuesto opcional si se combinan mucho genero+tipo
CREATE INDEX idx_contenido_genero_tipo ON contenido(genero, tipo);

-- Índice para ordenar por año (utilizado en orden fecha)
CREATE INDEX idx_contenido_anio ON contenido(anio);

-- Índice para alquileres por usuario y estado
CREATE INDEX idx_alquileres_usuario_estado ON alquileres(usuario_id, estado);

-- Índice para expiración rápida de alquileres activos
CREATE INDEX idx_alquileres_estado_fecha_fin ON alquileres(estado, fecha_fin);

