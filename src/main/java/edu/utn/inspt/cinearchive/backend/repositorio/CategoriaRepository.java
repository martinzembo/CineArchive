package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaRepository {
    List<Categoria> findAll();
    Optional<Categoria> findById(int id);
    List<Categoria> findByTipo(Categoria.Tipo tipo);
    Optional<Categoria> findByNombre(String nombre);
    Categoria save(Categoria categoria);
    void deleteById(int id);
    boolean existsById(int id);
    boolean existsByNombre(String nombre);
}
