package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.ListaContenido;
import java.util.List;

public interface ListaContenidoService {
    void addContenido(Long listaId, Long contenidoId);
    void removeContenido(Long listaId, Long contenidoId);
    boolean existeEnLista(Long listaId, Long contenidoId);
    List<ListaContenido> obtenerPorLista(Long listaId);
}

