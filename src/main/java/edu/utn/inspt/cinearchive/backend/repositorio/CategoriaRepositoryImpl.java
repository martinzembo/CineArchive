package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoriaRepositoryImpl implements CategoriaRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final RowMapper<Categoria> categoriaRowMapper = (rs, rowNum) -> {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getLong("id"));
        categoria.setNombre(rs.getString("nombre"));
        categoria.setTipo(Categoria.Tipo.valueOf(rs.getString("tipo")));
        categoria.setDescripcion(rs.getString("descripcion"));
        return categoria;
    };

    @Override
    public List<Categoria> findAll() {
        return jdbcTemplate.query(
                "SELECT id, nombre, tipo, descripcion FROM categoria",
                categoriaRowMapper
        );
    }

    @Override
    public Optional<Categoria> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT id, nombre, tipo, descripcion FROM categoria WHERE id = ?",
                    categoriaRowMapper,
                    id
            ));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Categoria> findByTipo(Categoria.Tipo tipo) {
        return jdbcTemplate.query(
                "SELECT id, nombre, tipo, descripcion FROM categoria WHERE tipo = ?",
                categoriaRowMapper,
                tipo.name()
        );
    }

    @Override
    public Optional<Categoria> findByNombre(String nombre) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT id, nombre, tipo, descripcion FROM categoria WHERE nombre = ?",
                    categoriaRowMapper,
                    nombre
            ));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Categoria save(Categoria categoria) {
        if (categoria.getId() == null) {
            return insert(categoria);
        }
        return update(categoria);
    }

    private Categoria insert(Categoria categoria) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO categoria (nombre, tipo, descripcion) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getTipo().name());
            ps.setString(3, categoria.getDescripcion());
            return ps;
        }, keyHolder);

        categoria.setId(keyHolder.getKey().longValue());
        return categoria;
    }

    private Categoria update(Categoria categoria) {
        jdbcTemplate.update(
                "UPDATE categoria SET nombre = ?, tipo = ?, descripcion = ? WHERE id = ?",
                categoria.getNombre(),
                categoria.getTipo().name(),
                categoria.getDescripcion(),
                categoria.getId()
        );
        return categoria;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM categoria WHERE id = ?", id);
    }

    @Override
    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM categoria WHERE id = ?",
                Integer.class,
                id
        );
        return count != null && count > 0;
    }

    @Override
    public boolean existsByNombre(String nombre) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM categoria WHERE nombre = ?",
                Integer.class,
                nombre
        );
        return count != null && count > 0;
    }
}
