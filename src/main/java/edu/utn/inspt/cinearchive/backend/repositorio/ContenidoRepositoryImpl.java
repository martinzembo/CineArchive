package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

/**
 * Implementación del repositorio para la entidad Contenido con JDBC Template
 */
@Repository
public class ContenidoRepositoryImpl implements ContenidoRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final RowMapper<Contenido> contenidoRowMapper = (rs, rowNum) -> {
        Contenido contenido = new Contenido();
        contenido.setId(rs.getLong("id"));
        contenido.setTitulo(rs.getString("titulo"));
        contenido.setGenero(rs.getString("genero"));
        contenido.setAnio(rs.getInt("anio"));
        contenido.setDescripcion(rs.getString("descripcion"));
        contenido.setImagenUrl(rs.getString("imagen_url"));
        contenido.setTrailerUrl(rs.getString("trailer_url"));
        contenido.setTipo(Contenido.Tipo.valueOf(rs.getString("tipo")));
        contenido.setDisponibleParaAlquiler(rs.getBoolean("disponible_para_alquiler"));
        contenido.setPrecioAlquiler(rs.getBigDecimal("precio_alquiler"));
        contenido.setCopiasDisponibles(rs.getInt("copias_disponibles"));
        contenido.setCopiasTotales(rs.getInt("copias_totales"));
        if (rs.getDate("fecha_vencimiento_licencia") != null) {
            contenido.setFechaVencimientoLicencia(rs.getDate("fecha_vencimiento_licencia").toLocalDate());
        }
        contenido.setIdApiExterna(rs.getString("id_api_externa"));
        contenido.setGestorInventarioId(rs.getLong("gestor_inventario_id"));
        contenido.setDuracion(rs.getInt("duracion"));
        contenido.setDirector(rs.getString("director"));
        contenido.setTemporadas(rs.getInt("temporadas"));
        contenido.setCapitulosTotales(rs.getInt("capitulos_totales"));
        contenido.setEnEmision(rs.getBoolean("en_emision"));
        return contenido;
    };

    public List<Contenido> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM contenido ORDER BY titulo",
                contenidoRowMapper
        );
    }

    public Optional<Contenido> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT * FROM contenido WHERE id = ?",
                    contenidoRowMapper,
                    id
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Contenido> findByTitulo(String titulo) {
        return jdbcTemplate.query(
                "SELECT * FROM contenido WHERE titulo = ?",
                contenidoRowMapper,
                titulo
        );
    }

    // Métodos específicos implementados una sola vez

    // Métodos existentes sin duplicar

    private Contenido insert(Contenido contenido) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO contenido (titulo, genero, anio, descripcion, imagen_url, trailer_url, " +
                    "tipo, disponible_para_alquiler, precio_alquiler, copias_disponibles, copias_totales, " +
                    "fecha_vencimiento_licencia, id_api_externa, gestor_inventario_id, duracion, director, " +
                    "temporadas, capitulos_totales, en_emision) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, contenido.getTitulo());
            ps.setString(2, contenido.getGenero());
            ps.setInt(3, contenido.getAnio());
            ps.setString(4, contenido.getDescripcion());
            ps.setString(5, contenido.getImagenUrl());
            ps.setString(6, contenido.getTrailerUrl());
            ps.setString(7, contenido.getTipo().name());
            ps.setBoolean(8, contenido.getDisponibleParaAlquiler());
            ps.setBigDecimal(9, contenido.getPrecioAlquiler());
            ps.setInt(10, contenido.getCopiasDisponibles());
            ps.setInt(11, contenido.getCopiasTotales());
            if (contenido.getFechaVencimientoLicencia() != null) {
                ps.setDate(12, java.sql.Date.valueOf(contenido.getFechaVencimientoLicencia()));
            } else {
                ps.setNull(12, java.sql.Types.DATE);
            }
            ps.setString(13, contenido.getIdApiExterna());
            if (contenido.getGestorInventarioId() != null) {
                ps.setLong(14, contenido.getGestorInventarioId());
            } else {
                ps.setNull(14, java.sql.Types.BIGINT);
            }
            if (contenido.getDuracion() != null) {
                ps.setInt(15, contenido.getDuracion());
            } else {
                ps.setNull(15, java.sql.Types.INTEGER);
            }
            ps.setString(16, contenido.getDirector());
            if (contenido.getTemporadas() != null) {
                ps.setInt(17, contenido.getTemporadas());
            } else {
                ps.setNull(17, java.sql.Types.INTEGER);
            }
            if (contenido.getCapitulosTotales() != null) {
                ps.setInt(18, contenido.getCapitulosTotales());
            } else {
                ps.setNull(18, java.sql.Types.INTEGER);
            }
            if (contenido.getEnEmision() != null) {
                ps.setBoolean(19, contenido.getEnEmision());
            } else {
                ps.setNull(19, java.sql.Types.BOOLEAN);
            }
            return ps;
        }, keyHolder);

        contenido.setId(keyHolder.getKey().longValue());
        return contenido;
    }

    private Contenido update(Contenido contenido) {
        jdbcTemplate.update(
                "UPDATE contenido SET titulo = ?, genero = ?, anio = ?, descripcion = ?, imagen_url = ?, " +
                "trailer_url = ?, tipo = ?, disponible_para_alquiler = ?, precio_alquiler = ?, " +
                "copias_disponibles = ?, copias_totales = ?, fecha_vencimiento_licencia = ?, " +
                "id_api_externa = ?, gestor_inventario_id = ?, duracion = ?, director = ?, " +
                "temporadas = ?, capitulos_totales = ?, en_emision = ? WHERE id = ?",
                contenido.getTitulo(),
                contenido.getGenero(),
                contenido.getAnio(),
                contenido.getDescripcion(),
                contenido.getImagenUrl(),
                contenido.getTrailerUrl(),
                contenido.getTipo().name(),
                contenido.getDisponibleParaAlquiler(),
                contenido.getPrecioAlquiler(),
                contenido.getCopiasDisponibles(),
                contenido.getCopiasTotales(),
                contenido.getFechaVencimientoLicencia(),
                contenido.getIdApiExterna(),
                contenido.getGestorInventarioId(),
                contenido.getDuracion(),
                contenido.getDirector(),
                contenido.getTemporadas(),
                contenido.getCapitulosTotales(),
                contenido.getEnEmision(),
                contenido.getId()
        );
        return contenido;
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM contenido WHERE id = ?", id);
    }

    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM contenido WHERE id = ?",
                Integer.class,
                id
        );
        return count != null && count > 0;
    }

    // Métodos auxiliares privados
    public List<Contenido> listarTodos() {
        return jdbcTemplate.query("SELECT * FROM contenido ORDER BY titulo", contenidoRowMapper);
    }

    public Contenido buscarPorId(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM contenido WHERE id = ?", contenidoRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Contenido crear(Contenido contenido) {
        return insert(contenido);
    }

    public void actualizar(Contenido contenido) {
        update(contenido);
    }

    public void eliminar(Long id) {
        deleteById(id);
    }

    // Implementación de métodos de la interfaz - versión corregida

    @Override
    public boolean existsByTitulo(String titulo) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM contenido WHERE titulo = ?",
                Integer.class,
                titulo
        );
        return count != null && count > 0;
    }

    @Override
    public List<Contenido> findByTituloContainingIgnoreCase(String titulo) {
        return jdbcTemplate.query(
                "SELECT * FROM contenido WHERE LOWER(titulo) LIKE LOWER(?)",
                contenidoRowMapper,
                "%" + titulo + "%"
        );
    }

    @Override
    public List<Contenido> findDisponiblesParaAlquiler() {
        return jdbcTemplate.query(
                "SELECT * FROM contenido WHERE disponible_para_alquiler = true AND copias_disponibles > 0",
                contenidoRowMapper
        );
    }

    @Override
    public List<Contenido> findByGestorInventarioId(Long gestorId) {
        return jdbcTemplate.query(
                "SELECT * FROM contenido WHERE gestor_inventario_id = ?",
                contenidoRowMapper,
                gestorId
        );
    }

    // Implementación completa de la interfaz ContenidoRepository

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM contenido";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    @Override
    public List<Contenido> findByAnio(Integer anio) {
        return jdbcTemplate.query(
                "SELECT * FROM contenido WHERE anio = ?",
                contenidoRowMapper,
                anio
        );
    }

    // Métodos faltantes de la interfaz ContenidoRepository

    @Override
    public Optional<Contenido> findById(Integer id) {
        return findById(id.longValue());
    }

    @Override
    public List<Contenido> findByGenero(String genero) {
        return jdbcTemplate.query(
                "SELECT * FROM contenido WHERE genero = ?",
                contenidoRowMapper,
                genero
        );
    }

    @Override
    public List<Contenido> findByTipo(Contenido.Tipo tipo) {
        return jdbcTemplate.query(
                "SELECT * FROM contenido WHERE tipo = ?",
                contenidoRowMapper,
                tipo.name()
        );
    }

    @Override
    public List<Contenido> findAvailable() {
        return findDisponiblesParaAlquiler();
    }

    @Override
    public List<Contenido> findByCategoria(Integer categoriaId) {
        String sql = "SELECT c.* FROM contenido c " +
                    "JOIN contenido_categoria cc ON c.id = cc.contenido_id " +
                    "WHERE cc.categoria_id = ? ORDER BY c.titulo ASC";
        return jdbcTemplate.query(sql, contenidoRowMapper, categoriaId);
    }

    @Override
    public Contenido save(Contenido contenido) {
        if (contenido.getId() == null) {
            return insert(contenido);
        }
        return update(contenido);
    }

    @Override
    public void deleteById(Integer id) {
        deleteById(id.longValue());
    }

    @Override
    public boolean existsById(Integer id) {
        return existsById(id.longValue());
    }

    @Override
    public int updateDisponibilidad(Integer id, Integer disponibilidad) {
        String sql = "UPDATE contenido SET copias_disponibles = ? WHERE id = ?";
        return jdbcTemplate.update(sql, disponibilidad, id);
    }
}
