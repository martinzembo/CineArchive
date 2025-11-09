-- Tabla alquileres
CREATE TABLE IF NOT EXISTS alquileres (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id BIGINT NOT NULL,
  contenido_id BIGINT NOT NULL,
  fecha_inicio DATETIME,
  fecha_fin DATETIME,
  periodo_alquiler INT,
  precio DECIMAL(10,2),
  estado VARCHAR(30),
  visto BOOLEAN DEFAULT FALSE,
  fecha_vista DATETIME,
  CONSTRAINT fk_alquileres_contenido FOREIGN KEY (contenido_id) REFERENCES contenido(id)
  -- CONSTRAINT fk_alquileres_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)  -- asumir tabla usuarios existe
);

CREATE INDEX IF NOT EXISTS idx_alquileres_usuario ON alquileres(usuario_id);
CREATE INDEX IF NOT EXISTS idx_alquileres_contenido ON alquileres(contenido_id);

