package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Usuario;
import edu.utn.inspt.cinearchive.backend.modelo.Usuario.Rol;
import edu.utn.inspt.cinearchive.backend.repositorio.UsuarioRepository;
import edu.utn.inspt.cinearchive.backend.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio de lógica de negocio para la gestión de usuarios
 * Implementa autenticación, registro, actualización y validaciones
 *
 * Esta capa NO maneja HTTP, solo lógica de negocio
 * Los Controllers llaman a estos métodos
 */
@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ============================================================
    // MÉTODOS PRINCIPALES DE AUTENTICACIÓN Y REGISTRO
    // ============================================================

    /**
     * Registra un nuevo usuario en el sistema
     * Valida email único y contraseña segura, luego encripta la contraseña
     *
     * @param nombre Nombre completo del usuario
     * @param email Email único del usuario
     * @param password Contraseña en texto plano
     * @param rol Rol del usuario en el sistema
     * @return Usuario creado con su ID generado
     * @throws IllegalArgumentException si el email ya existe o la contraseña no es segura
     *
     * @example
     * Usuario nuevo = usuarioService.registrar("Juan Pérez", "juan@email.com", "Password123", Rol.USUARIO_REGULAR);
     */
    public Usuario registrar(String nombre, String email, String password, Rol rol) {
        // 1. Validar que el email no exista
        if (usuarioRepository.existeEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // 2. Validar que la contraseña sea segura
        if (!PasswordUtil.esSegura(password)) {
            String mensajeError = PasswordUtil.obtenerMensajeValidacion(password);
            throw new IllegalArgumentException(mensajeError);
        }

        // 3. Encriptar la contraseña con BCrypt
        String hashPassword = PasswordUtil.encriptar(password);

        // 4. Crear el usuario con valores por defecto
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setContrasena(hashPassword); // ⚠️ Guardar el HASH, no el texto plano
        usuario.setRol(rol != null ? rol : Rol.USUARIO_REGULAR);
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setActivo(true);

        // 5. Guardar en la base de datos
        return usuarioRepository.crear(usuario);
    }

    /**
     * Registra un nuevo usuario usando el objeto Usuario completo
     * Útil cuando se recibe el usuario desde un formulario
     *
     * @param usuario Usuario a registrar (sin ID, con contraseña en texto plano)
     * @return Usuario creado con su ID generado
     * @throws IllegalArgumentException si hay errores de validación
     */
    public Usuario registrar(Usuario usuario) {
        // Validar que tenga los datos mínimos
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        // Llamar al método principal
        return registrar(
            usuario.getNombre(),
            usuario.getEmail(),
            usuario.getContrasena(),
            usuario.getRol()
        );
    }

    /**
     * Autentica un usuario con email y contraseña
     * Verifica que el usuario exista, esté activo y la contraseña sea correcta
     *
     * @param email Email del usuario
     * @param password Contraseña en texto plano ingresada
     * @return Usuario autenticado si las credenciales son correctas, null si no
     *
     * @example
     * Usuario usuario = usuarioService.autenticar("juan@email.com", "Password123");
     * if (usuario != null) {
     *     // Login exitoso
     *     session.setAttribute("usuario", usuario);
     * }
     */
    public Usuario autenticar(String email, String password) {
        // 1. Validar parámetros
        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return null;
        }

        // 2. Buscar usuario por email
        Usuario usuario = usuarioRepository.buscarPorEmail(email.trim());
        if (usuario == null) {
            return null; // Usuario no existe
        }

        // 3. Verificar que el usuario esté activo
        if (!usuario.estaActivo()) {
            return null; // Cuenta desactivada
        }

        // 4. Verificar contraseña usando BCrypt
        String hashGuardado = usuario.getContrasena();
        if (!PasswordUtil.verificar(password, hashGuardado)) {
            return null; // Contraseña incorrecta
        }

        // 5. (OPCIONAL) Regenerar hash si es antiguo
        if (PasswordUtil.necesitaRegenerar(hashGuardado)) {
            String nuevoHash = PasswordUtil.encriptar(password);
            usuario.setContrasena(nuevoHash);
            usuarioRepository.actualizar(usuario);
        }

        // 6. Autenticación exitosa
        return usuario;
    }

    // ============================================================
    // MÉTODOS DE GESTIÓN DE CONTRASEÑAS
    // ============================================================

    /**
     * Cambia la contraseña de un usuario
     * Verifica la contraseña actual antes de permitir el cambio
     *
     * @param usuarioId ID del usuario
     * @param passwordActual Contraseña actual en texto plano
     * @param passwordNueva Nueva contraseña en texto plano
     * @throws IllegalArgumentException si la contraseña actual es incorrecta o la nueva no es segura
     */
    public void cambiarContrasena(int usuarioId, String passwordActual, String passwordNueva) {
        // 1. Buscar usuario
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        // 2. Verificar contraseña actual
        if (!PasswordUtil.verificar(passwordActual, usuario.getContrasena())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        // 3. Validar que la nueva contraseña sea segura
        if (!PasswordUtil.esSegura(passwordNueva)) {
            String mensajeError = PasswordUtil.obtenerMensajeValidacion(passwordNueva);
            throw new IllegalArgumentException(mensajeError);
        }

        // 4. Verificar que la nueva contraseña sea diferente
        if (PasswordUtil.verificar(passwordNueva, usuario.getContrasena())) {
            throw new IllegalArgumentException("La nueva contraseña debe ser diferente a la actual");
        }

        // 5. Encriptar y actualizar
        String nuevoHash = PasswordUtil.encriptar(passwordNueva);
        usuarioRepository.actualizarContrasena(usuario.getId(), nuevoHash);
    }

    /**
     * Restablece la contraseña de un usuario (solo para admin)
     * No requiere la contraseña actual
     *
     * @param usuarioId ID del usuario
     * @param passwordNueva Nueva contraseña en texto plano
     * @throws IllegalArgumentException si la contraseña no es segura
     */
    public void restablecerContrasena(int usuarioId, String passwordNueva) {
        // 1. Buscar usuario
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        // 2. Validar nueva contraseña
        if (!PasswordUtil.esSegura(passwordNueva)) {
            String mensajeError = PasswordUtil.obtenerMensajeValidacion(passwordNueva);
            throw new IllegalArgumentException(mensajeError);
        }

        // 3. Encriptar y actualizar
        String nuevoHash = PasswordUtil.encriptar(passwordNueva);
        usuarioRepository.actualizarContrasena(usuario.getId(), nuevoHash);
    }

    // ============================================================
    // MÉTODOS DE ACTUALIZACIÓN DE PERFIL
    // ============================================================

    /**
     * Actualiza el perfil de un usuario (nombre, email, fecha de nacimiento)
     * NO actualiza la contraseña (usar cambiarContrasena para eso)
     *
     * @param usuarioId ID del usuario a actualizar
     * @param nombre Nuevo nombre
     * @param email Nuevo email
     * @param fechaNacimiento Nueva fecha de nacimiento
     * @return Usuario actualizado
     * @throws IllegalArgumentException si el email ya está en uso por otro usuario
     */
    public Usuario actualizarPerfil(int usuarioId, String nombre, String email, LocalDate fechaNacimiento) {
        // 1. Buscar usuario existente
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        // 2. Si se cambió el email, verificar que no exista
        if (!usuario.getEmail().equals(email)) {
            if (usuarioRepository.existeEmail(email)) {
                throw new IllegalArgumentException("El email ya está en uso por otro usuario");
            }
        }

        // 3. Actualizar campos
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setFechaNacimiento(fechaNacimiento);

        // 4. Guardar cambios
        usuarioRepository.actualizar(usuario);
        return usuario;
    }

    /**
     * Actualiza un usuario completo (para admin)
     * Permite cambiar rol y estado activo
     * Si se proporciona una nueva contraseña, la encripta
     *
     * @param usuario Usuario con los datos actualizados
     * @return Usuario actualizado
     * @throws IllegalArgumentException si hay algún error de validación
     */
    public Usuario actualizar(Usuario usuario) {
        // 1. Verificar que el usuario existe
        Usuario existente = usuarioRepository.buscarPorId(usuario.getId());
        if (existente == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }

        // 2. Si se cambió el email, verificar que no exista
        if (!existente.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.existeEmail(usuario.getEmail())) {
                throw new IllegalArgumentException("El email ya está en uso");
            }
        }

        // 3. Si la contraseña cambió (no es el mismo hash), encriptarla
        if (!usuario.getContrasena().equals(existente.getContrasena())) {
            // Si la contraseña no está encriptada (no empieza con $2a$), encriptarla
            if (!usuario.getContrasena().startsWith("$2a$")) {
                if (!PasswordUtil.esSegura(usuario.getContrasena())) {
                    throw new IllegalArgumentException("La contraseña no es segura");
                }
                usuario.setContrasena(PasswordUtil.encriptar(usuario.getContrasena()));
            }
        }

        // 4. Actualizar en BD
        usuarioRepository.actualizar(usuario);
        return usuario;
    }

    // ============================================================
    // MÉTODOS DE CONSULTA
    // ============================================================

    /**
     * Busca un usuario por su ID
     *
     * @param id ID del usuario
     * @return Usuario encontrado o null si no existe
     */
    public Usuario buscarPorId(int id) {
        return usuarioRepository.buscarPorId(id);
    }

    /**
     * Busca un usuario por su email
     *
     * @param email Email del usuario
     * @return Usuario encontrado o null si no existe
     */
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(email);
    }

    /**
     * Lista todos los usuarios del sistema
     *
     * @return Lista de todos los usuarios (activos e inactivos)
     */
    public List<Usuario> listarTodos() {
        return usuarioRepository.listarTodos();
    }

    /**
     * Lista usuarios por rol
     *
     * @param rol Rol a filtrar
     * @return Lista de usuarios con ese rol
     */
    public List<Usuario> listarPorRol(Rol rol) {
        return usuarioRepository.buscarPorRol(rol);
    }

    /**
     * Lista solo usuarios activos
     *
     * @return Lista de usuarios con activo = true
     */
    public List<Usuario> listarActivos() {
        return usuarioRepository.listarActivos();
    }

    /**
     * Verifica si un email ya está registrado
     *
     * @param email Email a verificar
     * @return true si existe, false si no
     */
    public boolean existeEmail(String email) {
        return usuarioRepository.existeEmail(email);
    }

    /**
     * Busca usuarios por nombre (búsqueda parcial)
     *
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de usuarios que coinciden
     */
    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.buscarPorNombre(nombre);
    }

    // ============================================================
    // MÉTODOS DE ADMINISTRACIÓN
    // ============================================================

    /**
     * Desactiva un usuario (soft delete)
     *
     * @param id ID del usuario a desactivar
     * @return true si se desactivó, false si no existe
     */
    public boolean desactivar(int id) {
        return usuarioRepository.cambiarEstado(id, false);
    }

    /**
     * Activa un usuario previamente desactivado
     *
     * @param id ID del usuario a activar
     * @return true si se activó, false si no existe
     */
    public boolean activar(int id) {
        return usuarioRepository.cambiarEstado(id, true);
    }

    /**
     * Cambia el estado activo de un usuario
     *
     * @param id ID del usuario
     * @param activo Nuevo estado (true/false)
     * @return true si se cambió, false si no existe
     */
    public boolean cambiarEstado(int id, boolean activo) {
        return usuarioRepository.cambiarEstado(id, activo);
    }

    /**
     * Elimina un usuario permanentemente (hard delete)
     * Solo debe usarse en casos excepcionales
     *
     * @param id ID del usuario a eliminar
     * @return true si se eliminó, false si no existe
     */
    public boolean eliminar(int id) {
        return usuarioRepository.eliminarFisicamente(id);
    }

    /**
     * Cambia el rol de un usuario
     *
     * @param usuarioId ID del usuario
     * @param nuevoRol Nuevo rol a asignar
     * @throws IllegalArgumentException si el usuario no existe
     */
    public void cambiarRol(int usuarioId, Rol nuevoRol) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        usuario.setRol(nuevoRol);
        usuarioRepository.actualizar(usuario);
    }

    // ============================================================
    // MÉTODOS DE ESTADÍSTICAS
    // ============================================================

    /**
     * Cuenta usuarios por rol
     *
     * @param rol Rol a contar
     * @return Número de usuarios con ese rol
     */
    public int contarPorRol(Rol rol) {
        return usuarioRepository.contarPorRol(rol);
    }

    /**
     * Cuenta usuarios activos
     *
     * @return Número de usuarios con activo = true
     */
    public int contarActivos() {
        return usuarioRepository.contarActivos();
    }

    /**
     * Cuenta total de usuarios
     *
     * @return Número total de usuarios registrados
     */
    public int contarTotal() {
        return usuarioRepository.contarTotal();
    }

    /**
     * Obtiene estadísticas completas de usuarios por rol
     * Útil para el dashboard de administración
     *
     * @return String con estadísticas formateadas
     */
    public String obtenerEstadisticas() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== Estadísticas de Usuarios ===\n");
        stats.append("Total usuarios: ").append(contarTotal()).append("\n");
        stats.append("Usuarios activos: ").append(contarActivos()).append("\n");
        stats.append("\nPor rol:\n");
        stats.append("- Usuarios Regulares: ").append(contarPorRol(Rol.USUARIO_REGULAR)).append("\n");
        stats.append("- Administradores: ").append(contarPorRol(Rol.ADMINISTRADOR)).append("\n");
        stats.append("- Gestores de Inventario: ").append(contarPorRol(Rol.GESTOR_INVENTARIO)).append("\n");
        stats.append("- Analistas de Datos: ").append(contarPorRol(Rol.ANALISTA_DATOS)).append("\n");
        return stats.toString();
    }

    // ============================================================
    // MÉTODOS DE VALIDACIÓN
    // ============================================================

    /**
     * Verifica si un usuario tiene un rol específico
     *
     * @param usuarioId ID del usuario
     * @param rol Rol a verificar
     * @return true si el usuario tiene ese rol, false si no
     */
    public boolean tieneRol(int usuarioId, Rol rol) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        return usuario != null && usuario.tieneRol(rol);
    }

    /**
     * Verifica si un usuario está activo
     *
     * @param usuarioId ID del usuario
     * @return true si está activo, false si no
     */
    public boolean estaActivo(int usuarioId) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        return usuario != null && usuario.estaActivo();
    }
}

