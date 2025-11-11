package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Transaccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TransaccionRepositoryImpl implements TransaccionRepository {

    private static final String T_TRANS = "transaccion";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TransaccionRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Transaccion> mapper = (rs, rowNum) -> {
        Transaccion t = new Transaccion();
        t.setId(rs.getLong("id"));
        t.setUsuarioId(rs.getLong("usuario_id"));
        t.setAlquilerId(rs.getLong("alquiler_id"));
        t.setMonto(rs.getBigDecimal("monto"));
        t.setMetodoPago(rs.getString("metodo_pago"));
        java.sql.Timestamp ft = rs.getTimestamp("fecha_transaccion");
        t.setFechaTransaccion(ft != null ? ft.toLocalDateTime() : null);
        String estado = rs.getString("estado");
        if (estado != null) {
            try { t.setEstado(Transaccion.Estado.valueOf(estado)); } catch (IllegalArgumentException ignored) {}
        }
        t.setReferenciaExterna(rs.getString("referencia_externa"));
        return t;
    };

    @Override
    public Transaccion findById(Long id) {
        String sql = "SELECT * FROM " + T_TRANS + " WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, mapper, id);
    }

    @Override
    public List<Transaccion> findByUsuarioId(Long usuarioId) {
        String sql = "SELECT * FROM " + T_TRANS + " WHERE usuario_id = ? ORDER BY fecha_transaccion DESC";
        return jdbcTemplate.query(sql, mapper, usuarioId);
    }

    @Override
    public int save(Transaccion transaccion) {
        if (transaccion.getFechaTransaccion() == null) {
            transaccion.setFechaTransaccion(LocalDateTime.now());
        }
        String sql = "INSERT INTO " + T_TRANS + " (usuario_id, alquiler_id, monto, metodo_pago, fecha_transaccion, estado, referencia_externa) VALUES (?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                transaccion.getUsuarioId(),
                transaccion.getAlquilerId(),
                transaccion.getMonto(),
                transaccion.getMetodoPago(),
                transaccion.getFechaTransaccion(),
                transaccion.getEstado() != null ? transaccion.getEstado().name() : null,
                transaccion.getReferenciaExterna());
    }

    @Override
    public int update(Transaccion transaccion) {
        String sql = "UPDATE " + T_TRANS + " SET estado = ?, referencia_externa = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                transaccion.getEstado() != null ? transaccion.getEstado().name() : null,
                transaccion.getReferenciaExterna(),
                transaccion.getId());
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM " + T_TRANS + " WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
