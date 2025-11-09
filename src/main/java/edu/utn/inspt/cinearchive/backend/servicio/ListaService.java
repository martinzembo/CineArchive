package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Lista;
import java.util.List;

public interface ListaService {
    Lista getById(Long id);
    List<Lista> getByUsuario(Long usuarioId);
    void create(Lista lista);
    void update(Lista lista);
    void delete(Long id);
    // Nuevos métodos simplificados
    void addContenido(Long listaId, Long contenidoId);
    void removeContenido(Long listaId, Long contenidoId);
    boolean existeContenido(Long listaId, Long contenidoId);
}
