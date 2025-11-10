package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Lista;
import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class ListaRepositoryImpl implements ListaRepository {

    private static final Logger logger = Logger.getLogger(ListaRepositoryImpl.class.getName());

    private final JdbcTemplate jdbcTemplate;

    @Autowired
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
            l.setPublica(rs.getBoolean("publica"));
            l.setFechaCreacion(rs.getTimestamp("fechaCreacion") != null ? rs.getTimestamp("fechaCreacion").toLocalDateTime() : null);
            l.setFechaModificacion(rs.getTimestamp("fechaModificacion") != null ? rs.getTimestamp("fechaModificacion").toLocalDateTime() : null);
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
        String sql = "INSERT INTO listas (usuarioId, nombre, descripcion, publica, fechaCreacion) VALUES (?, ?, ?, ?, NOW())";
        int res = jdbcTemplate.update(sql, lista.getUsuarioId(), lista.getNombre(), lista.getDescripcion(), lista.getPublica());
        logger.log(Level.INFO, "Lista creada: {0} (usuario {1})", new Object[]{lista.getNombre(), lista.getUsuarioId()});
        return res;
    }

    @Override
    public int update(Lista lista) {
        String sql = "UPDATE listas SET nombre = ?, descripcion = ?, publica = ?, fechaModificacion = NOW() WHERE id = ?";
        int res = jdbcTemplate.update(sql, lista.getNombre(), lista.getDescripcion(), lista.getPublica(), lista.getId());
        logger.log(Level.INFO, "Lista actualizada: {0} (id {1})", new Object[]{lista.getNombre(), lista.getId()});
        return res;
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM listas WHERE id = ?";
        int res = jdbcTemplate.update(sql, id);
        logger.log(Level.INFO, "Lista eliminada: id {0}", id);
        return res;
    }

    @Override
    public int addContenido(Long listaId, Long contenidoId) {
        String sql = "INSERT INTO lista_contenido (lista_id, contenido_id, fecha_agregado) VALUES (?, ?, NOW())";
        int res = jdbcTemplate.update(sql, listaId, contenidoId);
        logger.log(Level.INFO, "Contenido {0} agregado a lista {1}", new Object[]{contenidoId, listaId});
        return res;
    }

    @Override
    public int removeContenido(Long listaId, Long contenidoId) {
        String sql = "DELETE FROM lista_contenido WHERE lista_id = ? AND contenido_id = ?";
        int res = jdbcTemplate.update(sql, listaId, contenidoId);
        logger.log(Level.INFO, "Contenido {0} removido de lista {1}", new Object[]{contenidoId, listaId});
        return res;
    }

    @Override
    public boolean existeContenido(Long listaId, Long contenidoId) {
        String sql = "SELECT COUNT(*) FROM lista_contenido WHERE lista_id = ? AND contenido_id = ?";
        Integer cnt = jdbcTemplate.queryForObject(sql, new Object[]{listaId, contenidoId}, Integer.class);
        return cnt != null && cnt > 0;
    }

    @Override
    public List<Contenido> findContenidoByLista(Long listaId) {
        String sql = "SELECT c.* FROM lista_contenido lc JOIN contenido c ON c.id = lc.contenido_id WHERE lc.lista_id = ? ORDER BY COALESCE(lc.orden,999999), lc.fecha_agregado DESC";
        return jdbcTemplate.query(sql, new Object[]{listaId}, (rs, rowNum) -> {
            Contenido c = new Contenido();
            c.setId(rs.getLong("id"));
            c.setTitulo(rs.getString("titulo"));
            c.setImagenUrl(rs.getString("imagen_url"));
            c.setGenero(rs.getString("genero"));
            c.setAnio((Integer) rs.getObject("anio"));
            c.setPrecioAlquiler(rs.getBigDecimal("precio_alquiler"));
            return c; // mapeo ligero suficiente para vista
        });
    }
}
