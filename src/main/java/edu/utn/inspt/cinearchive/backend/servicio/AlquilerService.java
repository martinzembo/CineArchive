package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Alquiler;
import java.util.List;

public interface AlquilerService {
    Alquiler getById(Long id);
    List<Alquiler> getByUsuario(Long usuarioId);
    void create(Alquiler alquiler);
    void update(Alquiler alquiler);
    void delete(Long id);
}

