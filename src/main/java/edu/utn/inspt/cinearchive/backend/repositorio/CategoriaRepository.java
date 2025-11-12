package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaRepository {
    List<Categoria> findAll();
    Optional<Categoria> findById(Long id);
    List<Categoria> findByTipo(Categoria.Tipo tipo);
    Optional<Categoria> findByNombre(String nombre);
    Categoria save(Categoria categoria);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByNombre(String nombre);
}
