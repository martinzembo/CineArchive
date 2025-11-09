package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Alquiler;
import edu.utn.inspt.cinearchive.backend.modelo.AlquilerDetalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;

@Repository
public class AlquilerRepositoryImpl implements AlquilerRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AlquilerRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Alquiler> mapper = new RowMapper<Alquiler>() {
        @Override
        public Alquiler mapRow(ResultSet rs, int rowNum) throws SQLException {
            Alquiler a = new Alquiler();
            a.setId(rs.getLong("id"));
            a.setUsuarioId(rs.getLong("usuario_id"));
            a.setContenidoId(rs.getLong("contenido_id"));
            a.setFechaInicio(rs.getTimestamp("fecha_inicio") != null ? rs.getTimestamp("fecha_inicio").toLocalDateTime() : null);
            a.setFechaFin(rs.getTimestamp("fecha_fin") != null ? rs.getTimestamp("fecha_fin").toLocalDateTime() : null);
            a.setPeriodoAlquiler((Integer) rs.getObject("periodo_alquiler"));
            a.setPrecio(rs.getBigDecimal("precio"));
            String estado = rs.getString("estado");
            if (estado != null) {
                try { a.setEstado(Alquiler.Estado.valueOf(estado)); } catch (IllegalArgumentException ignored) {}
            }
            a.setVisto((Boolean) rs.getObject("visto"));
            a.setFechaVista(rs.getTimestamp("fecha_vista") != null ? rs.getTimestamp("fecha_vista").toLocalDateTime() : null);
            return a;
        }
    };

    @Override
    public Alquiler findById(Long id) {
        String sql = "SELECT * FROM alquileres WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, mapper);
    }

    @Override
    public List<Alquiler> findByUsuarioId(Long usuarioId) {
        String sql = "SELECT * FROM alquileres WHERE usuario_id = ?";
        return jdbcTemplate.query(sql, new Object[]{usuarioId}, mapper);
    }

    @Override
    public int save(Alquiler alquiler) {
        String sql = "INSERT INTO alquileres (usuario_id, contenido_id, fecha_inicio, fecha_fin, periodo_alquiler, precio, estado, visto, fecha_vista) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                alquiler.getUsuarioId(),
                alquiler.getContenidoId(),
                alquiler.getFechaInicio(),
                alquiler.getFechaFin(),
                alquiler.getPeriodoAlquiler(),
                alquiler.getPrecio(),
                alquiler.getEstado() != null ? alquiler.getEstado().name() : null,
                alquiler.getVisto(),
                alquiler.getFechaVista());
    }

    @Override
    public int update(Alquiler alquiler) {
        String sql = "UPDATE alquileres SET fecha_fin = ?, estado = ?, visto = ?, fecha_vista = ? WHERE id = ?";
        return jdbcTemplate.update(sql, alquiler.getFechaFin(), alquiler.getEstado() != null ? alquiler.getEstado().name() : null, alquiler.getVisto(), alquiler.getFechaVista(), alquiler.getId());
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM alquileres WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public java.util.List<AlquilerDetalle> findByUsuarioIdWithContenido(Long usuarioId) {
        String sql = "SELECT a.*, c.titulo, c.imagen_url FROM alquileres a JOIN contenido c ON c.id = a.contenido_id WHERE a.usuario_id = ? ORDER BY a.fecha_inicio DESC";
        return jdbcTemplate.query(sql, new Object[]{usuarioId}, (rs, rowNum) -> {
            AlquilerDetalle d = new AlquilerDetalle();
            d.setId(rs.getLong("id"));
            d.setUsuarioId(rs.getLong("usuario_id"));
            d.setContenidoId(rs.getLong("contenido_id"));
            d.setTituloContenido(rs.getString("titulo"));
            d.setImagenUrlContenido(rs.getString("imagen_url"));
            java.sql.Timestamp fi = rs.getTimestamp("fecha_inicio");
            java.sql.Timestamp ff = rs.getTimestamp("fecha_fin");
            java.time.LocalDateTime lfi = fi != null ? fi.toLocalDateTime() : null;
            java.time.LocalDateTime lff = ff != null ? ff.toLocalDateTime() : null;
            d.setFechaInicio(lfi);
            d.setFechaFin(lff);
            d.setPeriodoAlquiler((Integer) rs.getObject("periodo_alquiler"));
            d.setPrecio(rs.getBigDecimal("precio"));
            String estado = rs.getString("estado");
            if (estado != null) {
                try { d.setEstado(edu.utn.inspt.cinearchive.backend.modelo.Alquiler.Estado.valueOf(estado)); } catch (IllegalArgumentException ignored) {}
            }
            d.setVisto(rs.getBoolean("visto"));
            long diasRestantes = 0;
            if (lff != null) {
                java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
                if (lff.isAfter(ahora)) {
                    diasRestantes = Duration.between(ahora, lff).toDays();
                }
            }
            d.setDiasRestantes(diasRestantes);
            return d;
        });
    }
}
