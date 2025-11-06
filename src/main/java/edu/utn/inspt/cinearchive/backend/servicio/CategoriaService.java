package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<Categoria> obtenerTodas();
    Optional<Categoria> obtenerPorId(int id);
    List<Categoria> obtenerPorTipo(Categoria.Tipo tipo);
    Optional<Categoria> obtenerPorNombre(String nombre);
    Categoria guardar(Categoria categoria);
    void eliminar(int id);
    boolean existePorId(int id);
    boolean existePorNombre(String nombre);
    List<Categoria> obtenerGeneros();
    List<Categoria> obtenerTags();
    List<Categoria> obtenerClasificaciones();
}
