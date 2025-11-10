package edu.utn.inspt.cinearchive.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

import org.h2.tools.RunScript;
import org.springframework.core.io.ClassPathResource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;
import java.util.stream.Collectors;

/**
 * Configuración de la base de datos MySQL/H2
 * - Si hay MySQL configurado y accesible: usa MySQL, y si db.seed=true, ejecuta esquema + seed allí.
 * - Si falla la conexión o se fuerza entorno de test: usa H2 en memoria y ejecuta scripts (opcionalmente seed).
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Value("${db.driver}")
    private String driver;

    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${db.seed:false}")
    private boolean seedProperty; // configurable desde application.properties o -Ddb.seed=true

    private static final String[] DB_SCRIPTS = {
            "src/main/resources/db/02_contenido.sql",
            "src/main/resources/db/03_alquileres.sql",
            "src/main/resources/db/04_listas.sql",
            "src/main/resources/db/05_transacciones.sql"
            // seed_contenido.sql se maneja aparte
    };

    private static final String[] SEED_SCRIPTS = {
            "src/main/resources/db/seed_contenido.sql"
    };

    private boolean isTestEnv() {
        String envProp = System.getProperty("env", "");
        String envVar = System.getenv("APP_ENV");
        return "test".equalsIgnoreCase(envProp) || "test".equalsIgnoreCase(envVar);
    }

    private boolean isSeedEnabled() {
        boolean explicitSeedSystemProp = Boolean.parseBoolean(System.getProperty("db.seed", "false"));
        boolean altSeedSystemProp = Boolean.parseBoolean(System.getProperty("dbSeed", "false")); // alternativa sin punto
        boolean envSeed = Boolean.parseBoolean(System.getenv().getOrDefault("DB_SEED", "false"));
        return seedProperty || explicitSeedSystemProp || altSeedSystemProp || envSeed;
    }

    private boolean isMySqlConfigured() {
        return driver != null && driver.contains("mysql") && url != null && url.startsWith("jdbc:mysql")
                && username != null && !username.trim().isEmpty();
    }

    /**
     * Configura el DataSource. Intenta conectar a MySQL; si falla, crea una H2 en memoria
     * y ejecuta los scripts SQL de la carpeta resources/db para poblar datos.
     */
    @Bean
    public DataSource dataSource() {
        boolean seedEnabled = isSeedEnabled();
        boolean testEnv = isTestEnv();
        System.out.println("[DB] Inicializando DataSource. url=" + url + ", driver=" + driver + ", user=" + username
                + ", seed=" + seedEnabled + ", testEnv=" + testEnv + ".");

        // Si se fuerza entorno de test (APP_ENV=test o -Denv=test), usar H2 embebida
        if (testEnv) {
            System.out.println("[DB] Entorno de TEST detectado -> Usando H2 en memoria (MySQL ignorado).");
            return createH2DataSourceWithScripts(seedEnabled);
        }

        // Intentar con MySQL si está configurado
        if (isMySqlConfigured()) {
            DriverManagerDataSource mysql = new DriverManagerDataSource();
            mysql.setDriverClassName(driver);
            mysql.setUrl(url);
            mysql.setUsername(username);
            mysql.setPassword(password);
            try (Connection conn = mysql.getConnection()) {
                System.out.println("[DB] Conectado a MySQL correctamente en " + url + ".");
                // Ejecutar esquema si BD vacía y, si corresponde, el seed
                runSchemaAndSeedIfNeeded(conn, seedEnabled);
                return mysql;
            } catch (SQLException ex) {
                System.out.println("[DB] No se pudo conectar a MySQL (" + ex.getMessage() + "). Fallback a H2 en memoria.");
                return createH2DataSourceWithScripts(seedEnabled);
            }
        }

        // Si no hay MySQL configurado, usar H2
        System.out.println("[DB] MySQL no configurado correctamente -> Usando H2 en memoria.");
        return createH2DataSourceWithScripts(seedEnabled);
    }

    private void runSchemaAndSeedIfNeeded(Connection conn, boolean includeSeed) {
        try {
            // Ejecutar siempre scripts de esquema (son idempotentes por IF NOT EXISTS)
            System.out.println("[DB] Ejecutando scripts de esquema (modo idempotente)...");
            for (String script : DB_SCRIPTS) {
                executeSqlScriptFromClasspath(conn, script);
            }
            // Decidir seed sólo si habilitado y tabla vacía
            if (includeSeed) {
                if (isContenidoEmpty(conn)) {
                    System.out.println("[DB] Seed habilitado y tabla 'contenido' vacía -> ejecutando seed...");
                    for (String script : SEED_SCRIPTS) {
                        executeSqlScriptFromClasspath(conn, script);
                    }
                } else {
                    System.out.println("[DB] Seed habilitado pero tabla 'contenido' ya tiene datos -> se omite Seed (para forzar usar -Ddb.reseed=true).");
                    boolean forceReseed = Boolean.parseBoolean(System.getProperty("db.reseed", "false"));
                    if (forceReseed) {
                        System.out.println("[DB] db.reseed=true detectado -> intentando reseed (puede generar duplicados si no hay control).");
                        for (String script : SEED_SCRIPTS) {
                            executeSqlScriptFromClasspath(conn, script);
                        }
                    }
                }
            } else {
                System.out.println("[DB] Seed deshabilitado -> solo esquema.");
            }
        } catch (Exception e) {
            System.out.println("[DB] Error ejecutando esquema/seed: " + e.getMessage());
        }
    }

    private boolean isContenidoEmpty(Connection conn) {
        try (java.sql.Statement st = conn.createStatement();
             java.sql.ResultSet rs = st.executeQuery("SELECT COUNT(1) FROM contenido")) {
            if (rs.next()) {
                return rs.getLong(1) == 0L;
            }
        } catch (SQLException ex) {
            // Si falla (tabla no existe aún) lo tratamos como vacía
            System.out.println("[DB] Aviso: no se pudo contar filas de 'contenido' (" + ex.getMessage() + ") -> se asume vacía para permitir seed.");
            return true;
        }
        return true; // fallback conservador
    }

    private void executeSqlScriptFromClasspath(Connection conn, String path) {
        String classpathLocation = path.startsWith("db/") ? path : "db/" + new java.io.File(path).getName();
        try {
            ClassPathResource resource = new ClassPathResource(classpathLocation);
            if (!resource.exists()) {
                resource = new ClassPathResource(path);
            }
            if (!resource.exists()) {
                System.out.println("[DB] Script no encontrado en classpath: " + classpathLocation + " (omitido)");
                return;
            }
            System.out.println("[DB] Ejecutando script: classpath:" + classpathLocation);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String sql = reader.lines().collect(Collectors.joining("\n"));
                for (String statement : sql.split(";")) {
                    String trimmed = statement.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("--") || trimmed.startsWith("/*")) continue;
                    try (java.sql.Statement st = conn.createStatement()) {
                        st.execute(trimmed);
                    } catch (SQLException ex) {
                        String msg = ex.getMessage();
                        if (msg != null && (msg.toLowerCase().contains("already exists") || msg.toLowerCase().contains("duplicate"))) {
                            System.out.println("[DB] Aviso (idempotente): " + msg);
                        } else {
                            System.out.println("[DB] Error ejecutando statement: " + msg);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[DB] Error leyendo/ejecutando script " + path + ": " + e.getMessage());
        }
    }

    private DataSource createH2DataSourceWithScripts(boolean seedEnabled) {
        try {
            String h2Url = "jdbc:h2:mem:cinearchive;DB_CLOSE_DELAY=-1;MODE=MySQL";
            DriverManagerDataSource h2 = new DriverManagerDataSource();
            h2.setDriverClassName("org.h2.Driver");
            h2.setUrl(h2Url);
            h2.setUsername("sa");
            h2.setPassword("");

            try (Connection conn = h2.getConnection()) {
                System.out.println("[DB] Conectado a H2 en memoria (" + h2Url + "). Ejecutando scripts de esquema...");
                for (String script : DB_SCRIPTS) {
                    java.nio.file.Path path = Paths.get(script).toAbsolutePath();
                    if (path.toFile().exists()) {
                        System.out.println("[DB] Ejecutando: " + path);
                        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(path.toFile()), StandardCharsets.UTF_8)) {
                            RunScript.execute(conn, reader);
                        } catch (Exception ex) {
                            System.out.println("[DB] Error en script de esquema " + path.getFileName() + ": " + ex.getMessage());
                        }
                    } else {
                        System.out.println("[DB] Script no encontrado (omitido): " + path);
                    }
                }
                // Ejecutar seed si está habilitado o si la tabla está vacía (dev-friendly)
                boolean tableEmpty = isContenidoEmpty(conn);
                if (seedEnabled || tableEmpty) {
                    System.out.println("[DB] " + (seedEnabled ? "Seed habilitado" : "Tabla 'contenido' vacía en H2") + " -> Ejecutando scripts de seed...");
                    for (String script : SEED_SCRIPTS) {
                        java.nio.file.Path path = Paths.get(script).toAbsolutePath();
                        if (path.toFile().exists()) {
                            System.out.println("[DB] Ejecutando: " + path);
                            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(path.toFile()), StandardCharsets.UTF_8)) {
                                RunScript.execute(conn, reader);
                            } catch (Exception ex) {
                                System.out.println("[DB] Error en script de seed " + path.getFileName() + ": " + ex.getMessage());
                            }
                        } else {
                            System.out.println("[DB] Script de seed no encontrado (omitido): " + path);
                        }
                    }
                } else {
                    System.out.println("[DB] Seed deshabilitado y tabla 'contenido' no vacía. Solo esquema.");
                }
                System.out.println("[DB] Inicialización de H2 finalizada.");
            }
            return h2;
        } catch (Exception e) {
            System.out.println("[DB] ERROR crítico inicializando H2: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar la base de datos H2 embebida: " + e.getMessage(), e);
        }
    }

    /**
     * JdbcTemplate para ejecutar consultas SQL
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * Gestor de transacciones
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
