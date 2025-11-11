-- Tabla transacciones
CREATE TABLE IF NOT EXISTS transacciones (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id BIGINT NOT NULL,
  alquiler_id BIGINT NOT NULL,
  monto DECIMAL(10,2),
  metodo_pago VARCHAR(100),
  fecha_transaccion DATETIME,
  estado VARCHAR(50),
  referencia_externa VARCHAR(255),
  CONSTRAINT fk_transacciones_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
  CONSTRAINT fk_transacciones_alquiler FOREIGN KEY (alquiler_id) REFERENCES alquileres(id)
);

CREATE INDEX IF NOT EXISTS idx_transacciones_usuario ON transacciones(usuario_id);
CREATE INDEX IF NOT EXISTS idx_transacciones_alquiler ON transacciones(alquiler_id);
