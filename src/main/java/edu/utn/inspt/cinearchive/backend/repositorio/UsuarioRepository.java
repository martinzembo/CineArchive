package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    // Métodos CRUD básicos
    List<Usuario> findAll();
    Optional<Usuario> findById(Integer id);
    Usuario findByEmail(String email);
    Optional<Usuario> findByEmailOptional(String email);
    List<Usuario> findByRol(Usuario.Rol rol);
    List<Usuario> findByActivo(boolean activo);
    Usuario save(Usuario usuario);
    void deleteById(Integer id);
    boolean existsByEmail(String email);
    boolean existsById(Integer id);
    int updatePassword(Integer id, String password);
    int updateActivo(Integer id, boolean activo);
    long count();
    long countByRol(Usuario.Rol rol);

    // Métodos adicionales específicos
    List<Usuario> findByNombre(String nombre);
    boolean cambiarEstado(Long id, boolean activo);
    boolean eliminarFisicamente(Long id);
    int contarActivos();
    int contarTotal();
}
