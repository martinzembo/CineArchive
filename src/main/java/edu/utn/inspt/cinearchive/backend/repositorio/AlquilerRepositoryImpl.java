package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Alquiler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AlquilerRepositoryImpl implements AlquilerRepository {

    private final JdbcTemplate jdbcTemplate;

    public AlquilerRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Alquiler> mapper = new RowMapper<Alquiler>() {
        @Override
        public Alquiler mapRow(ResultSet rs, int rowNum) throws SQLException {
            Alquiler a = new Alquiler();
            a.setId(rs.getLong("id"));
            a.setUsuarioId(rs.getLong("usuarioId"));
            a.setContenidoId(rs.getLong("contenidoId"));
            // mapear fechas y demás
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
        String sql = "SELECT * FROM alquileres WHERE usuarioId = ?";
        return jdbcTemplate.query(sql, new Object[]{usuarioId}, mapper);
    }

    @Override
    public int save(Alquiler alquiler) {
        String sql = "INSERT INTO alquileres (usuarioId, contenidoId, fechaInicio, fechaFin, periodoAlquiler, precio, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, alquiler.getUsuarioId(), alquiler.getContenidoId(), alquiler.getFechaInicio(), alquiler.getFechaFin(), alquiler.getPeriodoAlquiler(), alquiler.getPrecio(), alquiler.getEstado().name());
    }

    @Override
    public int update(Alquiler alquiler) {
        String sql = "UPDATE alquileres SET fechaFin = ?, estado = ?, visto = ? WHERE id = ?";
        return jdbcTemplate.update(sql, alquiler.getFechaFin(), alquiler.getEstado().name(), alquiler.getVisto(), alquiler.getId());
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM alquileres WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}

