-- Datos de prueba para poder probar reportes
USE cinearchive_v2;

-- Insertar algunos alquileres de prueba
INSERT INTO alquiler (usuario_id, contenido_id, fecha_inicio, fecha_fin, periodo_alquiler, precio, estado) VALUES
(4, 1, '2024-01-15 10:00:00', '2024-01-22 10:00:00', 7, 4.99, 'FINALIZADO'),
(4, 2, '2024-01-20 14:30:00', '2024-01-27 14:30:00', 7, 3.99, 'FINALIZADO'),
(5, 1, '2024-01-25 16:00:00', '2024-02-01 16:00:00', 7, 4.99, 'FINALIZADO'),
(5, 3, '2024-02-01 12:00:00', '2024-02-08 12:00:00', 7, 2.99, 'FINALIZADO'),
(4, 4, '2024-02-10 18:00:00', '2024-02-17 18:00:00', 7, 5.99, 'ACTIVO'),
(5, 5, '2024-02-12 20:00:00', '2024-02-19 20:00:00', 7, 4.99, 'ACTIVO');

-- Insertar algunas reseñas de prueba
INSERT INTO resena (usuario_id, contenido_id, calificacion, titulo, texto, fecha_creacion) VALUES
(4, 1, 4.5, 'Excelente película', 'Inception es una obra maestra del cine de ciencia ficción. La trama es compleja pero fascinante.', '2024-01-23'),
(4, 2, 5.0, 'Un clásico imperdible', 'El Padrino es simplemente perfecto. Actuaciones excepcionales y dirección magistral.', '2024-01-28'),
(5, 1, 4.0, 'Muy buena pero compleja', 'Me gustó mucho aunque algunas partes son difíciles de seguir. Gran cinematografía.', '2024-02-02'),
(5, 3, 4.8, 'Perfecta para toda la familia', 'Toy Story nunca pasa de moda. Entretenida para niños y adultos por igual.', '2024-02-09'),
(4, 4, 5.0, 'La mejor serie de todos los tiempos', 'Breaking Bad es una obra de arte televisiva. Cada episodio es mejor que el anterior.', '2024-02-15');

-- Insertar transacciones para los alquileres
INSERT INTO transaccion (usuario_id, alquiler_id, monto, metodo_pago, estado) VALUES
(4, 1, 4.99, 'Tarjeta de Crédito', 'COMPLETADA'),
(4, 2, 3.99, 'Tarjeta de Débito', 'COMPLETADA'),
(5, 3, 4.99, 'PayPal', 'COMPLETADA'),
(5, 4, 2.99, 'Tarjeta de Crédito', 'COMPLETADA'),
(4, 5, 5.99, 'Tarjeta de Crédito', 'COMPLETADA'),
(5, 6, 4.99, 'Tarjeta de Débito', 'COMPLETADA');

-- Asignar categorías a contenidos
INSERT INTO contenido_categoria (contenido_id, categoria_id) VALUES
(1, 4), -- Inception -> Ciencia Ficción
(1, 8), -- Inception -> ATP
(2, 3), -- El Padrino -> Drama
(2, 10), -- El Padrino -> +18
(3, 2), -- Toy Story -> Comedia
(3, 6), -- Toy Story -> Familiar
(3, 8), -- Toy Story -> ATP
(4, 3), -- Breaking Bad -> Drama
(4, 10), -- Breaking Bad -> +18
(5, 4), -- Stranger Things -> Ciencia Ficción
(5, 9); -- Stranger Things -> +13

SELECT 'Datos de prueba insertados correctamente' as Mensaje;
SELECT COUNT(*) as Total_Alquileres FROM alquiler;
SELECT COUNT(*) as Total_Resenas FROM resena;
SELECT COUNT(*) as Total_Transacciones FROM transaccion;
