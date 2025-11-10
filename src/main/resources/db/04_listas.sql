-- Tablas para listas y relación lista_contenido
CREATE TABLE IF NOT EXISTS listas (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuarioId BIGINT NOT NULL,
  nombre VARCHAR(150) NOT NULL,
  descripcion VARCHAR(500),
  publica BOOLEAN DEFAULT FALSE,
  fechaCreacion DATETIME,
  fechaModificacion DATETIME,
  CONSTRAINT fk_listas_usuario FOREIGN KEY (usuarioId) REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS lista_contenido (
  lista_id BIGINT NOT NULL,
  contenido_id BIGINT NOT NULL,
  orden INT,
  fecha_agregado DATETIME,
  PRIMARY KEY (lista_id, contenido_id),
  CONSTRAINT fk_lista_contenido_lista FOREIGN KEY (lista_id) REFERENCES listas(id),
  CONSTRAINT fk_lista_contenido_contenido FOREIGN KEY (contenido_id) REFERENCES contenido(id)
);

CREATE INDEX IF NOT EXISTS idx_listas_usuario ON listas(usuarioId);
CREATE INDEX IF NOT EXISTS idx_lista_contenido_lista ON lista_contenido(lista_id);
CREATE INDEX IF NOT EXISTS idx_lista_contenido_contenido ON lista_contenido(contenido_id);
