package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<Categoria> obtenerTodas();
    Optional<Categoria> obtenerPorId(Long id);
    List<Categoria> obtenerPorTipo(Categoria.Tipo tipo);
    Optional<Categoria> obtenerPorNombre(String nombre);
    Categoria guardar(Categoria categoria);
    void eliminar(Long id);
    boolean existePorId(Long id);
    boolean existePorNombre(String nombre);
    List<Categoria> obtenerGeneros();
    List<Categoria> obtenerTags();
    List<Categoria> obtenerClasificaciones();
}
