# 🔗 Ejemplo de Integración: PasswordUtil + UsuarioService

## 📋 Cómo usar PasswordUtil en UsuarioService.java

Este documento muestra **exactamente** cómo integrar `PasswordUtil` en tu `UsuarioService`.

---

## 📝 UsuarioService.java Completo con PasswordUtil

```java
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
     * Autentica un usuario con email y contraseña
     * Verifica que el usuario exista, esté activo y la contraseña sea correcta
     * 
     * @param email Email del usuario
     * @param password Contraseña en texto plano ingresada
     * @return Usuario autenticado si las credenciales son correctas, null si no
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
        usuario.setContrasena(nuevoHash);
        usuarioRepository.actualizar(usuario);
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
        usuario.setContrasena(nuevoHash);
        usuarioRepository.actualizar(usuario);
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
     */
    public Usuario buscarPorId(int id) {
        return usuarioRepository.buscarPorId(id);
    }

    /**
     * Busca un usuario por su email
     */
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(email);
    }

    /**
     * Lista todos los usuarios del sistema
     */
    public List<Usuario> listarTodos() {
        return usuarioRepository.listarTodos();
    }

    /**
     * Lista usuarios por rol
     */
    public List<Usuario> listarPorRol(Rol rol) {
        return usuarioRepository.buscarPorRol(rol);
    }

    /**
     * Lista solo usuarios activos
     */
    public List<Usuario> listarActivos() {
        return usuarioRepository.listarActivos();
    }

    /**
     * Verifica si un email ya está registrado
     */
    public boolean existeEmail(String email) {
        return usuarioRepository.existeEmail(email);
    }

    // ============================================================
    // MÉTODOS DE ADMINISTRACIÓN
    // ============================================================

    /**
     * Desactiva un usuario (soft delete)
     */
    public void desactivar(int id) {
        usuarioRepository.cambiarEstado(id, false);
    }

    /**
     * Activa un usuario previamente desactivado
     */
    public void activar(int id) {
        usuarioRepository.cambiarEstado(id, true);
    }

    /**
     * Elimina un usuario permanentemente (hard delete)
     * Solo debe usarse en casos excepcionales
     */
    public void eliminar(int id) {
        usuarioRepository.eliminarFisicamente(id);
    }

    /**
     * Cambia el rol de un usuario
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
     */
    public int contarPorRol(Rol rol) {
        return usuarioRepository.contarPorRol(rol);
    }

    /**
     * Cuenta usuarios activos
     */
    public int contarActivos() {
        return usuarioRepository.contarActivos();
    }

    /**
     * Cuenta total de usuarios
     */
    public int contarTotal() {
        return usuarioRepository.contarTotal();
    }
}
```

---

## 🎯 Puntos Clave de la Integración

### 1. **En el Registro**:
```java
// ❌ NUNCA hagas esto
usuario.setContrasena(password); // Texto plano

// ✅ SIEMPRE encripta
String hash = PasswordUtil.encriptar(password);
usuario.setContrasena(hash);
```

### 2. **En el Login**:
```java
// ❌ NUNCA compares así
if (password.equals(usuario.getContrasena())) { ... }

// ✅ SIEMPRE usa BCrypt
if (PasswordUtil.verificar(password, usuario.getContrasena())) { ... }
```

### 3. **Validación antes de guardar**:
```java
// ✅ Siempre valida primero
if (!PasswordUtil.esSegura(password)) {
    String mensaje = PasswordUtil.obtenerMensajeValidacion(password);
    throw new IllegalArgumentException(mensaje);
}
```

---

## 📊 Flujo Completo: Registro → Login

```
1. REGISTRO
   ├─ Usuario ingresa: "Password123"
   ├─ Controller recibe: "Password123"
   ├─ Service valida: PasswordUtil.esSegura("Password123") → true ✅
   ├─ Service encripta: PasswordUtil.encriptar("Password123") → "$2a$12$..."
   ├─ Repository guarda: INSERT INTO usuarios VALUES (..., "$2a$12$...", ...)
   └─ BD almacena: contrasena = "$2a$12$K5v7vWxq3pJ1Y9Nh8NZ.5O..."

2. LOGIN (días después)
   ├─ Usuario ingresa: "Password123"
   ├─ Controller recibe: "Password123"
   ├─ Service busca: usuarioRepository.buscarPorEmail(email)
   ├─ BD retorna: Usuario { contrasena: "$2a$12$K5v7vWxq3pJ1Y9Nh8NZ.5O..." }
   ├─ Service verifica: PasswordUtil.verificar("Password123", "$2a$12$K5v7...")
   │  ├─ BCrypt extrae el salt del hash
   │  ├─ BCrypt genera hash con el salt extraído
   │  ├─ BCrypt compara ambos hashes
   │  └─ Retorna: true ✅
   └─ Service retorna: Usuario autenticado
```

---

## 🎉 ¡Todo Listo!

Con este `UsuarioService` tienes:

✅ Registro seguro con contraseñas encriptadas
✅ Login con verificación BCrypt
✅ Cambio de contraseña validado
✅ Actualización de perfil
✅ Gestión completa de usuarios

**Siguiente paso**: Crear los Controllers (LoginController, RegistroController) que usen este Service.

