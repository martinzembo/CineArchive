package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
        java.util.List<Contenido> results = jdbcTemplate.query(sql, new Object[]{id}, mapper);
        return results.isEmpty() ? null : results.get(0);
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
        KeyHolder kh = new GeneratedKeyHolder();
        int updated = jdbcTemplate.update(con -> {
            java.sql.PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, contenido.getTitulo());
            ps.setString(2, contenido.getGenero());
            ps.setObject(3, contenido.getAnio());
            ps.setString(4, contenido.getDescripcion());
            ps.setString(5, contenido.getImagenUrl());
            ps.setString(6, contenido.getTrailerUrl());
            ps.setString(7, contenido.getTipo() != null ? contenido.getTipo().name() : null);
            ps.setObject(8, contenido.getDisponibleParaAlquiler());
            ps.setBigDecimal(9, contenido.getPrecioAlquiler());
            ps.setObject(10, contenido.getCopiasDisponibles());
            ps.setObject(11, contenido.getCopiasTotales());
            ps.setObject(12, contenido.getFechaVencimientoLicencia());
            ps.setString(13, contenido.getIdApiExterna());
            ps.setObject(14, contenido.getGestorInventarioId());
            ps.setObject(15, contenido.getDuracion());
            ps.setString(16, contenido.getDirector());
            ps.setObject(17, contenido.getTemporadas());
            ps.setObject(18, contenido.getCapitulosTotales());
            ps.setObject(19, contenido.getEnEmision());
            return ps;
        }, kh);
        if (kh.getKey() != null) {
            contenido.setId(kh.getKey().longValue());
        }
        return updated;
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
            sql.append(" AND (LOWER(titulo) LIKE LOWER(?) OR LOWER(COALESCE(descripcion,'')) LIKE LOWER(?))");
            String like = "%" + q.trim() + "%";
            params.add(like);
            params.add(like);
        }
        if (genero != null && !genero.trim().isEmpty()) {
            sql.append(" AND LOWER(REPLACE(REPLACE(genero,'-',''),' ','')) = LOWER(REPLACE(REPLACE(?,'-',''),' ',''))");
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

    @Override
    public int updateCopiasDisponibles(Long contenidoId, int delta) {
        String sql = "UPDATE contenido SET copias_disponibles = GREATEST(copias_disponibles + ?, 0) WHERE id = ?";
        return jdbcTemplate.update(sql, delta, contenidoId);
    }

    @Override
    public List<Contenido> searchPaged(String q, String genero, String tipo, String orden, int page, int size) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;
        StringBuilder sql = new StringBuilder("SELECT * FROM contenido WHERE 1=1");
        java.util.List<Object> params = new java.util.ArrayList<>();
        if (q != null && !q.trim().isEmpty()) {
            sql.append(" AND (LOWER(titulo) LIKE LOWER(?) OR LOWER(COALESCE(descripcion,'')) LIKE LOWER(?))");
            String like = "%" + q.trim() + "%";
            params.add(like);
            params.add(like);
        }
        if (genero != null && !genero.trim().isEmpty()) {
            sql.append(" AND LOWER(REPLACE(REPLACE(genero,'-',''),' ','')) = LOWER(REPLACE(REPLACE(?,'-',''),' ',''))");
            params.add(genero.trim());
        }
        if (tipo != null && !tipo.trim().isEmpty()) {
            sql.append(" AND tipo = ?");
            params.add(tipo.trim());
        }
        // Orden soportado real: fecha (anio desc), nombre (titulo asc)
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
        sql.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(offset);
        return jdbcTemplate.query(sql.toString(), params.toArray(), mapper);
    }

    @Override
    public long searchCount(String q, String genero, String tipo) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM contenido WHERE 1=1");
        java.util.List<Object> params = new java.util.ArrayList<>();
        if (q != null && !q.trim().isEmpty()) {
            sql.append(" AND (LOWER(titulo) LIKE LOWER(?) OR LOWER(COALESCE(descripcion,'')) LIKE LOWER(?))");
            String like = "%" + q.trim() + "%";
            params.add(like);
            params.add(like);
        }
        if (genero != null && !genero.trim().isEmpty()) {
            sql.append(" AND LOWER(REPLACE(REPLACE(genero,'-',''),' ','')) = LOWER(REPLACE(REPLACE(?,'-',''),' ',''))");
            params.add(genero.trim());
        }
        if (tipo != null && !tipo.trim().isEmpty()) {
            sql.append(" AND tipo = ?");
            params.add(tipo.trim());
        }
        Long cnt = jdbcTemplate.queryForObject(sql.toString(), params.toArray(), Long.class);
        return cnt != null ? cnt : 0L;
    }

    @Override
    public int reserveCopy(Long contenidoId) {
        // Decrementa si hay stock disponible (>0)
        String sql = "UPDATE contenido SET copias_disponibles = copias_disponibles - 1 WHERE id = ? AND copias_disponibles > 0";
        return jdbcTemplate.update(sql, contenidoId);
    }

    @Override
    public List<Contenido> searchPagedLight(String q, String genero, String tipo, String orden, int page, int size) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;
        StringBuilder sql = new StringBuilder("SELECT id, titulo, imagen_url, precio_alquiler FROM contenido WHERE 1=1");
        java.util.List<Object> params = new java.util.ArrayList<>();
        if (q != null && !q.trim().isEmpty()) {
            sql.append(" AND (LOWER(titulo) LIKE LOWER(?) OR LOWER(COALESCE(descripcion,'')) LIKE LOWER(?))");
            String like = "%" + q.trim() + "%";
            params.add(like);
            params.add(like);
        }
        if (genero != null && !genero.trim().isEmpty()) {
            sql.append(" AND LOWER(REPLACE(REPLACE(genero,'-',''),' ','')) = LOWER(REPLACE(REPLACE(?,'-',''),' ',''))");
            params.add(genero.trim());
        }
        if (tipo != null && !tipo.trim().isEmpty()) {
            sql.append(" AND tipo = ?");
            params.add(tipo.trim());
        }
        if (orden != null) {
            switch (orden) {
                case "fecha":
                    sql.append(" ORDER BY anio DESC, titulo ASC");
                    break;
                case "nombre":
                default:
                    sql.append(" ORDER BY titulo ASC");
            }
        } else {
            sql.append(" ORDER BY titulo ASC");
        }
        sql.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(offset);
        return jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) -> {
            edu.utn.inspt.cinearchive.backend.modelo.Contenido c = new edu.utn.inspt.cinearchive.backend.modelo.Contenido();
            c.setId(rs.getLong("id"));
            c.setTitulo(rs.getString("titulo"));
            c.setImagenUrl(rs.getString("imagen_url"));
            c.setPrecioAlquiler(rs.getBigDecimal("precio_alquiler"));
            return c;
        });
    }

    @Override
    public List<Contenido> findSeasonsByTitlePrefix(String titlePrefix) {
        if (titlePrefix == null) return java.util.Collections.emptyList();
        String base = titlePrefix.trim();
        if (base.isEmpty()) return java.util.Collections.emptyList();
        // Patrón estándar de nuestros seeds: "Base - Temporada X"
        String pattern = base + " - Temporada %"; // LIKE pattern
        String sql = "SELECT * FROM contenido WHERE tipo='SERIE' AND LOWER(titulo) LIKE LOWER(?) ORDER BY titulo ASC";
        java.util.List<Contenido> lista = jdbcTemplate.query(sql, new Object[]{ pattern }, mapper);
        if (lista.isEmpty()) {
            // Fallback: usar solo el prefijo base% y contener 'temporada'
            String sqlFallback = "SELECT * FROM contenido WHERE tipo='SERIE' AND LOWER(titulo) LIKE LOWER(?) AND LOWER(titulo) LIKE LOWER('%temporada%') ORDER BY titulo ASC";
            lista = jdbcTemplate.query(sqlFallback, new Object[]{ base + '%' }, mapper);
        }
        return lista;
    }
}
