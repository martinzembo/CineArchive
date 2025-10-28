-- Tabla contenido
CREATE TABLE IF NOT EXISTS contenido (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(255) NOT NULL,
  genero VARCHAR(100),
  anio INT,
  descripcion TEXT,
  imagen_url VARCHAR(500),
  trailer_url VARCHAR(500),
  tipo VARCHAR(20),
  disponible_para_alquiler BOOLEAN DEFAULT TRUE,
  precio_alquiler DECIMAL(10,2) DEFAULT 0.00,
  copias_disponibles INT DEFAULT 0,
  copias_totales INT DEFAULT 0,
  fecha_vencimiento_licencia DATE,
  id_api_externa VARCHAR(150),
  gestor_inventario_id BIGINT,
  duracion INT,
  director VARCHAR(150),
  temporadas INT,
  capitulos_totales INT,
  en_emision BOOLEAN DEFAULT FALSE
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_contenido_titulo ON contenido(titulo);
CREATE INDEX IF NOT EXISTS idx_contenido_genero ON contenido(genero);

