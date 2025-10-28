package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ContenidoRepositoryImpl implements ContenidoRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ContenidoRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Contenido> mapper = new RowMapper<Contenido>() {
        @Override
        public Contenido mapRow(ResultSet rs, int rowNum) throws SQLException {
            Contenido c = new Contenido();
            c.setId(rs.getLong("id"));
            c.setTitulo(rs.getString("titulo"));
            c.setGenero(rs.getString("genero"));
            c.setAnio(rs.getInt("anio"));
            c.setDescripcion(rs.getString("descripcion"));
            // mapear otros campos según necesidad
            return c;
        }
    };

    @Override
    public Contenido findById(Long id) {
        String sql = "SELECT * FROM contenido WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, mapper);
    }

    @Override
    public List<Contenido> findAll() {
        String sql = "SELECT * FROM contenido";
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public int save(Contenido contenido) {
        String sql = "INSERT INTO contenido (titulo, genero, anio, descripcion) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, contenido.getTitulo(), contenido.getGenero(), contenido.getAnio(), contenido.getDescripcion());
    }

    @Override
    public int update(Contenido contenido) {
        String sql = "UPDATE contenido SET titulo = ?, genero = ?, anio = ?, descripcion = ? WHERE id = ?";
        return jdbcTemplate.update(sql, contenido.getTitulo(), contenido.getGenero(), contenido.getAnio(), contenido.getDescripcion(), contenido.getId());
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM contenido WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
