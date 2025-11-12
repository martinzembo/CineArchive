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
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Configuración de la base de datos: SOLO MySQL.
 * Se elimina fallback a H2 y ejecución de scripts.
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

    @Bean
    public DataSource dataSource() {
        System.out.println("[DB] Inicializando DataSource MySQL. url=" + url + ", driver=" + driver + ", user=" + username + ".");
        if (driver == null || !driver.contains("mysql") || url == null || !url.startsWith("jdbc:mysql") || username == null || username.trim().isEmpty()) {
            throw new RuntimeException("Configuración de MySQL inválida: verifique db.driver, db.url y db.username en application.properties");
        }
        DriverManagerDataSource mysql = new DriverManagerDataSource();
        mysql.setDriverClassName(driver);
        mysql.setUrl(url);
        mysql.setUsername(username);
        mysql.setPassword(password);
        try (Connection conn = mysql.getConnection()) {
            // Verificación mínima
            conn.getMetaData();
            System.out.println("[DB] Conexión a MySQL OK: " + conn.getMetaData().getURL());
        } catch (SQLException ex) {
            throw new RuntimeException("No se pudo conectar a MySQL en " + url + ": " + ex.getMessage(), ex);
        }
        return mysql;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
