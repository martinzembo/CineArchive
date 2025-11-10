package edu.utn.inspt.cinearchive.backend.servicio;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DataSourceHealth {

    private final DataSource dataSource;

    public DataSourceHealth(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void logDataSourceInfo() {
        try (Connection c = dataSource.getConnection()) {
            System.out.println("[DB-HEALTH] URL=" + c.getMetaData().getURL());
            System.out.println("[DB-HEALTH] Driver=" + c.getMetaData().getDriverName());
            System.out.println("[DB-HEALTH] Product=" + c.getMetaData().getDatabaseProductName());
        } catch (Exception e) {
            System.err.println("[DB-HEALTH] Error al obtener metadata: " + e.getMessage());
        }
    }
}

