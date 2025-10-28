package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Lista;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ListaRepositoryImpl implements ListaRepository {

    private final JdbcTemplate jdbcTemplate;

    public ListaRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Lista> mapper = new RowMapper<Lista>() {
        @Override
        public Lista mapRow(ResultSet rs, int rowNum) throws SQLException {
            Lista l = new Lista();
            l.setId(rs.getLong("id"));
            l.setUsuarioId(rs.getLong("usuarioId"));
            l.setNombre(rs.getString("nombre"));
            l.setDescripcion(rs.getString("descripcion"));
            return l;
        }
    };

    @Override
    public Lista findById(Long id) {
        String sql = "SELECT * FROM listas WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, mapper);
    }

    @Override
    public List<Lista> findByUsuarioId(Long usuarioId) {
        String sql = "SELECT * FROM listas WHERE usuarioId = ?";
        return jdbcTemplate.query(sql, new Object[]{usuarioId}, mapper);
    }

    @Override
    public int save(Lista lista) {
        String sql = "INSERT INTO listas (usuarioId, nombre, descripcion, publica, fechaCreacion) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, lista.getUsuarioId(), lista.getNombre(), lista.getDescripcion(), lista.getPublica(), lista.getFechaCreacion());
    }

    @Override
    public int update(Lista lista) {
        String sql = "UPDATE listas SET nombre = ?, descripcion = ?, publica = ?, fechaModificacion = ? WHERE id = ?";
        return jdbcTemplate.update(sql, lista.getNombre(), lista.getDescripcion(), lista.getPublica(), lista.getFechaModificacion(), lista.getId());
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM listas WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}

