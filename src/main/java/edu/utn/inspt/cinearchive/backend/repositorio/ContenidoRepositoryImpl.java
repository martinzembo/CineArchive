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
            c.setAnio((Integer) rs.getObject("anio"));
            c.setDescripcion(rs.getString("descripcion"));
            c.setImagenUrl(rs.getString("imagen_url"));
            c.setTrailerUrl(rs.getString("trailer_url"));
            String tipo = rs.getString("tipo");
            if (tipo != null) {
                try { c.setTipo(Contenido.Tipo.valueOf(tipo)); } catch (IllegalArgumentException ignored) {}
            }
            c.setDisponibleParaAlquiler((Boolean) rs.getObject("disponible_para_alquiler"));
            c.setPrecioAlquiler(rs.getBigDecimal("precio_alquiler"));
            c.setCopiasDisponibles((Integer) rs.getObject("copias_disponibles"));
            c.setCopiasTotales((Integer) rs.getObject("copias_totales"));
            java.sql.Date fvl = rs.getDate("fecha_vencimiento_licencia");
            c.setFechaVencimientoLicencia(fvl != null ? fvl.toLocalDate() : null);
            c.setIdApiExterna(rs.getString("id_api_externa"));
            c.setGestorInventarioId((Long) rs.getObject("gestor_inventario_id"));
            c.setDuracion((Integer) rs.getObject("duracion"));
            c.setDirector(rs.getString("director"));
            c.setTemporadas((Integer) rs.getObject("temporadas"));
            c.setCapitulosTotales((Integer) rs.getObject("capitulos_totales"));
            c.setEnEmision((Boolean) rs.getObject("en_emision"));
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
    public List<Contenido> findByTituloLike(String tituloPattern) {
        String sql = "SELECT * FROM contenido WHERE titulo LIKE ?";
        return jdbcTemplate.query(sql, new Object[]{tituloPattern}, mapper);
    }

    @Override
    public int save(Contenido contenido) {
        String sql = "INSERT INTO contenido (titulo, genero, anio, descripcion, imagen_url, trailer_url, tipo, disponible_para_alquiler, precio_alquiler, copias_disponibles, copias_totales, fecha_vencimiento_licencia, id_api_externa, gestor_inventario_id, duracion, director, temporadas, capitulos_totales, en_emision) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                contenido.getTitulo(),
                contenido.getGenero(),
                contenido.getAnio(),
                contenido.getDescripcion(),
                contenido.getImagenUrl(),
                contenido.getTrailerUrl(),
                contenido.getTipo() != null ? contenido.getTipo().name() : null,
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
                contenido.getEnEmision());
    }

    @Override
    public int update(Contenido contenido) {
        String sql = "UPDATE contenido SET titulo=?, genero=?, anio=?, descripcion=?, imagen_url=?, trailer_url=?, tipo=?, disponible_para_alquiler=?, precio_alquiler=?, copias_disponibles=?, copias_totales=?, fecha_vencimiento_licencia=?, id_api_externa=?, gestor_inventario_id=?, duracion=?, director=?, temporadas=?, capitulos_totales=?, en_emision=? WHERE id = ?";
        return jdbcTemplate.update(sql,
                contenido.getTitulo(),
                contenido.getGenero(),
                contenido.getAnio(),
                contenido.getDescripcion(),
                contenido.getImagenUrl(),
                contenido.getTrailerUrl(),
                contenido.getTipo() != null ? contenido.getTipo().name() : null,
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
                contenido.getId());
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM contenido WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Contenido> search(String q, String genero, String tipo, String orden) {
        StringBuilder sql = new StringBuilder("SELECT * FROM contenido WHERE 1=1");
        java.util.List<Object> params = new java.util.ArrayList<>();
        if (q != null && !q.trim().isEmpty()) {
            sql.append(" AND titulo LIKE ?");
            params.add("%" + q.trim() + "%");
        }
        if (genero != null && !genero.trim().isEmpty()) {
            sql.append(" AND LOWER(genero) = LOWER(?)");
            params.add(genero.trim());
        }
        if (tipo != null && !tipo.trim().isEmpty()) {
            sql.append(" AND tipo = ?");
            params.add(tipo.trim());
        }
        // Orden permitido: fecha (anio desc), nombre (titulo asc)
        if (orden != null) {
            switch (orden) {
                case "fecha":
                    sql.append(" ORDER BY anio DESC, titulo ASC");
                    break;
                case "nombre":
                    sql.append(" ORDER BY titulo ASC");
                    break;
                default:
                    sql.append(" ORDER BY titulo ASC");
            }
        } else {
            sql.append(" ORDER BY titulo ASC");
        }
        return jdbcTemplate.query(sql.toString(), params.toArray(), mapper);
    }
}
