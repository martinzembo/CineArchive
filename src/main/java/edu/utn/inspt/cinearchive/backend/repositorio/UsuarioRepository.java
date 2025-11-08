package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para gestionar el acceso a datos de la entidad Usuario
 * Implementa operaciones CRUD y consultas específicas usando JdbcTemplate
 */
@Repository
public class UsuarioRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * RowMapper para convertir ResultSet de la BD a objetos Usuario
     * Se reutiliza en todos los métodos de consulta
     */
    private static final RowMapper<Usuario> USUARIO_ROW_MAPPER = (rs, rowNum) -> {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setEmail(rs.getString("email"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setRol(Usuario.Rol.valueOf(rs.getString("rol")));

        // Manejar fechaRegistro (puede ser java.sql.Timestamp o java.sql.Date)
        Timestamp fechaRegistroTS = rs.getTimestamp("fecha_registro");
        if (fechaRegistroTS != null) {
            usuario.setFechaRegistro(fechaRegistroTS.toLocalDateTime().toLocalDate());
        }

        usuario.setActivo(rs.getBoolean("activo"));

        // fechaNacimiento puede ser NULL
        Date fechaNacimiento = rs.getDate("fecha_nacimiento");
        if (fechaNacimiento != null) {
            usuario.setFechaNacimiento(fechaNacimiento.toLocalDate());
        }

        return usuario;
    };

    /**
     * Crea un nuevo usuario en la base de datos
     * @param usuario Usuario a crear (sin ID)
     * @return Usuario creado con su ID generado
     * @throws RuntimeException si hay error en la inserción
     */
    public Usuario crear(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombre, email, contrasena, rol, fecha_registro, activo, fecha_nacimiento) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getContrasena());
            ps.setString(4, usuario.getRol().name());
            ps.setDate(5, Date.valueOf(usuario.getFechaRegistro() != null ? usuario.getFechaRegistro() : LocalDate.now()));
            ps.setBoolean(6, usuario.getActivo() != null ? usuario.getActivo() : true);

            // fechaNacimiento puede ser NULL
            if (usuario.getFechaNacimiento() != null) {
                ps.setDate(7, Date.valueOf(usuario.getFechaNacimiento()));
            } else {
                ps.setNull(7, Types.DATE);
            }

            return ps;
        }, keyHolder);

        // Asignar el ID generado al usuario
        usuario.setId(keyHolder.getKey().longValue());
        return usuario;
    }

    /**
     * Busca un usuario por su ID
     * @param id ID del usuario
     * @return Usuario encontrado o null si no existe
     */
    public Usuario buscarPorId(Long id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, USUARIO_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Busca un usuario por su email (usado en login)
     * @param email Email del usuario (único en BD)
     * @return Usuario encontrado o null si no existe
     */
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, USUARIO_ROW_MAPPER, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Actualiza los datos de un usuario existente
     * @param usuario Usuario con datos actualizados
     * @return true si se actualizó, false si no existe
     */
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nombre = ?, email = ?, contrasena = ?, " +
                     "rol = ?, activo = ?, fecha_nacimiento = ? WHERE id = ?";

        int filasActualizadas = jdbcTemplate.update(sql,
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getContrasena(),
                usuario.getRol().name(),
                usuario.getActivo(),
                usuario.getFechaNacimiento(),
                usuario.getId());

        return filasActualizadas > 0;
    }

    /**
     * Elimina (desactiva) un usuario por su ID
     * Implementa soft delete cambiando activo a false
     * @param id ID del usuario
     * @return true si se desactivó, false si no existe
     */
    public boolean eliminar(int id) {
        String sql = "UPDATE usuario SET activo = false WHERE id = ?";
        int filasActualizadas = jdbcTemplate.update(sql, id);
        return filasActualizadas > 0;
    }

    /**
     * Elimina permanentemente un usuario de la BD
     * Usar con precaución - elimina físicamente el registro
     * @param id ID del usuario
     * @return true si se eliminó, false si no existe
     */
    public boolean eliminarFisicamente(Long id) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        int filasEliminadas = jdbcTemplate.update(sql, id);
        return filasEliminadas > 0;
    }

    /**
     * Lista todos los usuarios registrados
     * @return Lista de todos los usuarios (activos e inactivos)
     */
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuario ORDER BY fecha_registro DESC";
        return jdbcTemplate.query(sql, USUARIO_ROW_MAPPER);
    }

    /**
     * Lista solo los usuarios activos
     * @return Lista de usuarios con activo = true
     */
    public List<Usuario> listarActivos() {
        String sql = "SELECT * FROM usuario WHERE activo = true ORDER BY nombre ASC";
        return jdbcTemplate.query(sql, USUARIO_ROW_MAPPER);
    }

    /**
     * Busca usuarios por rol específico
     * @param rol Rol a buscar (USUARIO_REGULAR, ADMINISTRADOR, etc.)
     * @return Lista de usuarios con ese rol
     */
    public List<Usuario> buscarPorRol(Usuario.Rol rol) {
        String sql = "SELECT * FROM usuario WHERE rol = ? ORDER BY nombre ASC";
        return jdbcTemplate.query(sql, USUARIO_ROW_MAPPER, rol.name());
    }

    /**
     * Verifica si existe un email en la base de datos
     * Útil para validar durante el registro
     * @param email Email a verificar
     * @return true si existe, false si no
     */
    public boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE email = ?";
        System.out.println("[DEBUG] existeEmail - SQL: " + sql + " | Email: " + email);
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    /**
     * Cuenta cuántos usuarios hay con un rol específico
     * Útil para reportes y estadísticas
     * @param rol Rol a contar
     * @return Número de usuarios con ese rol
     */
    public int contarPorRol(Usuario.Rol rol) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE rol = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, rol.name());
        return count != null ? count : 0;
    }

    /**
     * Cuenta el total de usuarios activos
     * @return Número de usuarios activos
     */
    public int contarActivos() {
        String sql = "SELECT COUNT(*) FROM usuario WHERE activo = true";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    /**
     * Busca usuarios por nombre (búsqueda parcial)
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de usuarios que coinciden
     */
    public List<Usuario> buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM usuario WHERE nombre LIKE ? ORDER BY nombre ASC";
        return jdbcTemplate.query(sql, USUARIO_ROW_MAPPER, "%" + nombre + "%");
    }

    /**
     * Cambia el estado activo de un usuario
     * @param id ID del usuario
     * @param activo Nuevo estado (true/false)
     * @return true si se actualizó, false si no existe
     */
    public boolean cambiarEstado(Long id, boolean activo) {
        String sql = "UPDATE usuario SET activo = ? WHERE id = ?";
        int filasActualizadas = jdbcTemplate.update(sql, activo, id);
        return filasActualizadas > 0;
    }

    /**
     * Actualiza solo la contraseña de un usuario
     * Útil para cambio de contraseña
     * @param id ID del usuario
     * @param nuevaContrasena Nueva contraseña (ya debe estar hasheada)
     * @return true si se actualizó, false si no existe
     */
    public boolean actualizarContrasena(Long usuarioId, String nuevaContrasena) {
        String sql = "UPDATE usuario SET contrasena = ? WHERE id = ?";
        int filasActualizadas = jdbcTemplate.update(sql, nuevaContrasena, usuarioId);
        return filasActualizadas > 0;
    }

    /**
     * Cuenta el total de usuarios registrados
     * @return Número total de usuarios (activos + inactivos)
     */
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM usuario";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }
}
