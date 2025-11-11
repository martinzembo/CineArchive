-- Tabla usuario (coherente con repositorio UsuarioRepository)
CREATE TABLE IF NOT EXISTS usuario (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  contrasena VARCHAR(255) NOT NULL,
  rol VARCHAR(30) NOT NULL DEFAULT 'USUARIO_REGULAR',
  fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
  activo BOOLEAN DEFAULT TRUE,
  fecha_nacimiento DATE
);

-- Índices auxiliares
CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuario(email);
CREATE INDEX IF NOT EXISTS idx_usuario_rol ON usuario(rol);

