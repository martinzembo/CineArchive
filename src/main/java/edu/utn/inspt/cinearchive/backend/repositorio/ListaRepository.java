package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Lista;
import java.util.List;

public interface ListaRepository {
    Lista findById(Long id);
    List<Lista> findByUsuarioId(Long usuarioId);
    int save(Lista lista);
    int update(Lista lista);
    int delete(Long id);
    // Nuevos métodos para gestionar lista_contenido sin repositorio extra
    int addContenido(Long listaId, Long contenidoId);
    int removeContenido(Long listaId, Long contenidoId);
    boolean existeContenido(Long listaId, Long contenidoId);
}
