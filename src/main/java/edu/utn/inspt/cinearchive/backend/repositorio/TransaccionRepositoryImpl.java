package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Transaccion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TransaccionRepositoryImpl implements TransaccionRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransaccionRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Transaccion> mapper = new RowMapper<Transaccion>() {
        @Override
        public Transaccion mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transaccion t = new Transaccion();
            t.setId(rs.getLong("id"));
            t.setUsuarioId(rs.getLong("usuarioId"));
            t.setAlquilerId(rs.getLong("alquilerId"));
            t.setMonto(rs.getBigDecimal("monto"));
            t.setMetodoPago(rs.getString("metodoPago"));
            return t;
        }
    };

    @Override
    public Transaccion findById(Long id) {
        String sql = "SELECT * FROM transacciones WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, mapper);
    }

    @Override
    public List<Transaccion> findByUsuarioId(Long usuarioId) {
        String sql = "SELECT * FROM transacciones WHERE usuarioId = ?";
        return jdbcTemplate.query(sql, new Object[]{usuarioId}, mapper);
    }

    @Override
    public int save(Transaccion transaccion) {
        String sql = "INSERT INTO transacciones (usuarioId, alquilerId, monto, metodoPago, fechaTransaccion, estado, referenciaExterna) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, transaccion.getUsuarioId(), transaccion.getAlquilerId(), transaccion.getMonto(), transaccion.getMetodoPago(), transaccion.getFechaTransaccion(), transaccion.getEstado() != null ? transaccion.getEstado().name() : null, transaccion.getReferenciaExterna());
    }

    @Override
    public int update(Transaccion transaccion) {
        String sql = "UPDATE transacciones SET estado = ?, referenciaExterna = ? WHERE id = ?";
        return jdbcTemplate.update(sql, transaccion.getEstado() != null ? transaccion.getEstado().name() : null, transaccion.getReferenciaExterna(), transaccion.getId());
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM transacciones WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}

