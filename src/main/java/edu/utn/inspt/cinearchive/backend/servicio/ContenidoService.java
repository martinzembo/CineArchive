package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import java.util.List;

public interface ContenidoService {
    Contenido getById(Long id);
    List<Contenido> getAll();
    void create(Contenido contenido);
    void update(Contenido contenido);
    void delete(Long id);
}

