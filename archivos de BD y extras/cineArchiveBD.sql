-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: cinearchive_v2
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `alquiler`
--

DROP TABLE IF EXISTS `alquiler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alquiler` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint NOT NULL,
  `contenido_id` bigint NOT NULL,
  `fecha_inicio` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_fin` timestamp NOT NULL,
  `periodo_alquiler` int NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `estado` enum('ACTIVO','FINALIZADO','CANCELADO') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVO',
  `visto` tinyint(1) DEFAULT '0',
  `fecha_vista` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_alquiler_usuario` (`usuario_id`),
  KEY `idx_alquiler_contenido` (`contenido_id`),
  KEY `idx_alquiler_estado` (`estado`),
  KEY `idx_alquiler_fechas` (`fecha_inicio`,`fecha_fin`),
  CONSTRAINT `fk_alquiler_contenido` FOREIGN KEY (`contenido_id`) REFERENCES `contenido` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_alquiler_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alquiler`
--

LOCK TABLES `alquiler` WRITE;
/*!40000 ALTER TABLE `alquiler` DISABLE KEYS */;
/*!40000 ALTER TABLE `alquiler` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo` enum('GENERO','TAG','CLASIFICACION') COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`),
  KEY `idx_categoria_tipo` (`tipo`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (1,'Acción','GENERO','Películas y series de acción'),(2,'Comedia','GENERO','Contenido de comedia'),(3,'Drama','GENERO','Contenido dramático'),(4,'Ciencia Ficción','GENERO','Contenido de ciencia ficción'),(5,'Terror','GENERO','Películas y series de terror'),(6,'Familiar','TAG','Contenido apto para toda la familia'),(7,'Premio Oscar','TAG','Contenido galardonado'),(8,'ATP','CLASIFICACION','Apto para todo público'),(9,'+13','CLASIFICACION','Mayores de 13 años'),(10,'+18','CLASIFICACION','Solo mayores de edad');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contenido`
--

DROP TABLE IF EXISTS `contenido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contenido` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `titulo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `genero` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `anio` int DEFAULT NULL,
  `descripcion` text COLLATE utf8mb4_unicode_ci,
  `imagen_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `trailer_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tipo` enum('PELICULA','SERIE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `disponible_para_alquiler` tinyint(1) DEFAULT '1',
  `precio_alquiler` decimal(10,2) NOT NULL,
  `copias_disponibles` int DEFAULT '0',
  `copias_totales` int DEFAULT '0',
  `fecha_vencimiento_licencia` date DEFAULT NULL,
  `id_api_externa` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gestor_inventario_id` bigint DEFAULT NULL,
  `duracion` int DEFAULT NULL,
  `director` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `temporadas` int DEFAULT NULL,
  `capitulos_totales` int DEFAULT NULL,
  `en_emision` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_contenido_gestor` (`gestor_inventario_id`),
  KEY `idx_contenido_tipo` (`tipo`),
  KEY `idx_contenido_disponible` (`disponible_para_alquiler`),
  KEY `idx_contenido_genero` (`genero`),
  KEY `idx_contenido_titulo` (`titulo`),
  CONSTRAINT `fk_contenido_gestor` FOREIGN KEY (`gestor_inventario_id`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contenido`
--

LOCK TABLES `contenido` WRITE;
/*!40000 ALTER TABLE `contenido` DISABLE KEYS */;
INSERT INTO `contenido` VALUES (1,'Inception','Ciencia Ficción',2010,'Un ladrón que roba secretos corporativos a través del uso de tecnología de compartir sueños.',NULL,NULL,'PELICULA',1,4.99,10,10,NULL,NULL,2,148,'Christopher Nolan',NULL,NULL,NULL),(2,'El Padrino','Drama',1972,'La historia de una familia de la mafia italiana.',NULL,NULL,'PELICULA',1,3.99,15,15,NULL,NULL,2,175,'Francis Ford Coppola',NULL,NULL,NULL),(3,'Toy Story','Comedia',1995,'Las aventuras de un grupo de juguetes que cobran vida.',NULL,NULL,'PELICULA',1,2.99,20,20,NULL,NULL,2,81,'John Lasseter',NULL,NULL,NULL),(4,'Breaking Bad','Drama',2008,'Un profesor de química se convierte en fabricante de metanfetaminas.',NULL,NULL,'SERIE',1,5.99,12,12,NULL,NULL,2,NULL,NULL,5,62,0),(5,'Stranger Things','Ciencia Ficción',2016,'Un grupo de niños enfrenta fenómenos sobrenaturales en su pueblo.',NULL,NULL,'SERIE',1,4.99,15,15,NULL,NULL,2,NULL,NULL,4,34,1);
/*!40000 ALTER TABLE `contenido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contenido_categoria`
--

DROP TABLE IF EXISTS `contenido_categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contenido_categoria` (
  `contenido_id` bigint NOT NULL,
  `categoria_id` bigint NOT NULL,
  PRIMARY KEY (`contenido_id`,`categoria_id`),
  KEY `fk_cc_categoria` (`categoria_id`),
  CONSTRAINT `fk_cc_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_cc_contenido` FOREIGN KEY (`contenido_id`) REFERENCES `contenido` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contenido_categoria`
--

LOCK TABLES `contenido_categoria` WRITE;
/*!40000 ALTER TABLE `contenido_categoria` DISABLE KEYS */;
/*!40000 ALTER TABLE `contenido_categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lista`
--

DROP TABLE IF EXISTS `lista`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lista` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text COLLATE utf8mb4_unicode_ci,
  `usuario_id` bigint NOT NULL,
  `publica` tinyint(1) DEFAULT '0',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_lista_usuario` (`usuario_id`),
  KEY `idx_lista_publica` (`publica`),
  CONSTRAINT `fk_lista_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lista`
--

LOCK TABLES `lista` WRITE;
/*!40000 ALTER TABLE `lista` DISABLE KEYS */;
/*!40000 ALTER TABLE `lista` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lista_contenido`
--

DROP TABLE IF EXISTS `lista_contenido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lista_contenido` (
  `lista_id` bigint NOT NULL,
  `contenido_id` bigint NOT NULL,
  `orden` int DEFAULT '0',
  `fecha_agregado` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`lista_id`,`contenido_id`),
  KEY `fk_lc_contenido` (`contenido_id`),
  KEY `idx_lista_contenido_orden` (`lista_id`,`orden`),
  CONSTRAINT `fk_lc_contenido` FOREIGN KEY (`contenido_id`) REFERENCES `contenido` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_lc_lista` FOREIGN KEY (`lista_id`) REFERENCES `lista` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lista_contenido`
--

LOCK TABLES `lista_contenido` WRITE;
/*!40000 ALTER TABLE `lista_contenido` DISABLE KEYS */;
/*!40000 ALTER TABLE `lista_contenido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reporte`
--

DROP TABLE IF EXISTS `reporte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reporte` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `analista_id` bigint NOT NULL,
  `titulo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text COLLATE utf8mb4_unicode_ci,
  `tipo_reporte` enum('MAS_ALQUILADOS','ANALISIS_DEMOGRAFICO','RENDIMIENTO_GENEROS','TENDENCIAS_TEMPORALES','COMPORTAMIENTO_USUARIOS') COLLATE utf8mb4_unicode_ci NOT NULL,
  `parametros` text COLLATE utf8mb4_unicode_ci,
  `resultados` text COLLATE utf8mb4_unicode_ci,
  `fecha_generacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `periodo_inicio` date DEFAULT NULL,
  `periodo_fin` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_reporte_analista` (`analista_id`),
  KEY `idx_reporte_tipo` (`tipo_reporte`),
  KEY `idx_reporte_fecha` (`fecha_generacion`),
  CONSTRAINT `fk_reporte_analista` FOREIGN KEY (`analista_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reporte`
--

LOCK TABLES `reporte` WRITE;
/*!40000 ALTER TABLE `reporte` DISABLE KEYS */;
/*!40000 ALTER TABLE `reporte` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resena`
--

DROP TABLE IF EXISTS `resena`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resena` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint NOT NULL,
  `contenido_id` bigint NOT NULL,
  `calificacion` double NOT NULL,
  `titulo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `texto` text COLLATE utf8mb4_unicode_ci,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_modificacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_resena_usuario_contenido` (`usuario_id`,`contenido_id`),
  KEY `idx_resena_contenido` (`contenido_id`),
  KEY `idx_resena_usuario` (`usuario_id`),
  KEY `idx_resena_calificacion` (`calificacion`),
  CONSTRAINT `fk_resena_contenido` FOREIGN KEY (`contenido_id`) REFERENCES `contenido` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_resena_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chk_calificacion` CHECK (((`calificacion` >= 1.0) and (`calificacion` <= 5.0)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resena`
--

LOCK TABLES `resena` WRITE;
/*!40000 ALTER TABLE `resena` DISABLE KEYS */;
/*!40000 ALTER TABLE `resena` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaccion`
--

DROP TABLE IF EXISTS `transaccion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaccion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint NOT NULL,
  `alquiler_id` bigint NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `metodo_pago` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_transaccion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `estado` enum('COMPLETADA','PENDIENTE','FALLIDA','REEMBOLSADA') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDIENTE',
  `referencia_externa` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_transaccion_alquiler` (`alquiler_id`),
  KEY `idx_transaccion_usuario` (`usuario_id`),
  KEY `idx_transaccion_fecha` (`fecha_transaccion`),
  KEY `idx_transaccion_estado` (`estado`),
  CONSTRAINT `fk_transaccion_alquiler` FOREIGN KEY (`alquiler_id`) REFERENCES `alquiler` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_transaccion_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaccion`
--

LOCK TABLES `transaccion` WRITE;
/*!40000 ALTER TABLE `transaccion` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaccion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contrasena` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rol` enum('USUARIO_REGULAR','ADMINISTRADOR','GESTOR_INVENTARIO','ANALISTA_DATOS') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'USUARIO_REGULAR',
  `fecha_registro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `activo` tinyint(1) DEFAULT '1',
  `fecha_nacimiento` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_usuario_email` (`email`),
  KEY `idx_usuario_rol` (`rol`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
-- CONTRASEÑAS DE PRUEBA:
-- admin@cinearchive.com     → Admin123
-- gestor@cinearchive.com    → Gestor123
-- analista@cinearchive.com  → Analista123
-- maria@example.com         → User123
-- juan@example.com          → User123
INSERT INTO `usuario` VALUES (1,'Admin Sistema','admin@cinearchive.com','$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5koQKeWcrmyS6','ADMINISTRADOR','2025-10-21 19:54:15',1,'1985-05-15'),(2,'Gestor Principal','gestor@cinearchive.com','$2a$12$EXRgnTTeKp4J2YpnJjKZ1.jJ5k7RUxVYvCJvwJz8rLXPLw3Xqm5oO','GESTOR_INVENTARIO','2025-10-21 19:54:15',1,'1990-08-22'),(3,'Analista Datos','analista@cinearchive.com','$2a$12$K5v7kp0FGKuKvJ7j4Xz4WOz8JL9mRVZL1XYkJ5j4K5v7kp0FGKOK6','ANALISTA_DATOS','2025-10-21 19:54:15',1,'1988-12-03'),(4,'María García','maria@example.com','$2a$12$RXuYvC0k8YpnJjKZ1EXRgnT5j4K5v7kp0FGKOKvJz8rLXPLw3Xqm5','USUARIO_REGULAR','2025-10-21 19:54:15',1,'1995-03-15'),(5,'Juan Pérez','juan@example.com','$2a$12$RXuYvC0k8YpnJjKZ1EXRgnT5j4K5v7kp0FGKOKvJz8rLXPLw3Xqm5','USUARIO_REGULAR','2025-10-21 19:54:15',1,'1992-07-20');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `vista_alquileres_activos`
--

DROP TABLE IF EXISTS `vista_alquileres_activos`;
/*!50001 DROP VIEW IF EXISTS `vista_alquileres_activos`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vista_alquileres_activos` AS SELECT 
 1 AS `alquiler_id`,
 1 AS `usuario_nombre`,
 1 AS `usuario_email`,
 1 AS `contenido_titulo`,
 1 AS `contenido_tipo`,
 1 AS `fecha_inicio`,
 1 AS `fecha_fin`,
 1 AS `precio`,
 1 AS `dias_restantes`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vista_contenido_popular`
--

DROP TABLE IF EXISTS `vista_contenido_popular`;
/*!50001 DROP VIEW IF EXISTS `vista_contenido_popular`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vista_contenido_popular` AS SELECT 
 1 AS `id`,
 1 AS `titulo`,
 1 AS `tipo`,
 1 AS `genero`,
 1 AS `total_alquileres`,
 1 AS `calificacion_promedio`,
 1 AS `total_resenas`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vista_ingresos_por_mes`
--

DROP TABLE IF EXISTS `vista_ingresos_por_mes`;
/*!50001 DROP VIEW IF EXISTS `vista_ingresos_por_mes`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vista_ingresos_por_mes` AS SELECT 
 1 AS `mes`,
 1 AS `total_transacciones`,
 1 AS `ingresos_totales`,
 1 AS `ticket_promedio`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `vista_alquileres_activos`
--

/*!50001 DROP VIEW IF EXISTS `vista_alquileres_activos`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vista_alquileres_activos` AS select `a`.`id` AS `alquiler_id`,`u`.`nombre` AS `usuario_nombre`,`u`.`email` AS `usuario_email`,`c`.`titulo` AS `contenido_titulo`,`c`.`tipo` AS `contenido_tipo`,`a`.`fecha_inicio` AS `fecha_inicio`,`a`.`fecha_fin` AS `fecha_fin`,`a`.`precio` AS `precio`,(to_days(`a`.`fecha_fin`) - to_days(now())) AS `dias_restantes` from ((`alquiler` `a` join `usuario` `u` on((`a`.`usuario_id` = `u`.`id`))) join `contenido` `c` on((`a`.`contenido_id` = `c`.`id`))) where ((`a`.`estado` = 'ACTIVO') and (`a`.`fecha_fin` > now())) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vista_contenido_popular`
--

/*!50001 DROP VIEW IF EXISTS `vista_contenido_popular`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vista_contenido_popular` AS select `c`.`id` AS `id`,`c`.`titulo` AS `titulo`,`c`.`tipo` AS `tipo`,`c`.`genero` AS `genero`,count(`a`.`id`) AS `total_alquileres`,avg(`r`.`calificacion`) AS `calificacion_promedio`,count(distinct `r`.`id`) AS `total_resenas` from ((`contenido` `c` left join `alquiler` `a` on((`c`.`id` = `a`.`contenido_id`))) left join `resena` `r` on((`c`.`id` = `r`.`contenido_id`))) group by `c`.`id`,`c`.`titulo`,`c`.`tipo`,`c`.`genero` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vista_ingresos_por_mes`
--

/*!50001 DROP VIEW IF EXISTS `vista_ingresos_por_mes`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vista_ingresos_por_mes` AS select date_format(`t`.`fecha_transaccion`,'%Y-%m') AS `mes`,count(`t`.`id`) AS `total_transacciones`,sum(`t`.`monto`) AS `ingresos_totales`,avg(`t`.`monto`) AS `ticket_promedio` from `transaccion` `t` where (`t`.`estado` = 'COMPLETADA') group by date_format(`t`.`fecha_transaccion`,'%Y-%m') order by `mes` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-01 17:46:09
