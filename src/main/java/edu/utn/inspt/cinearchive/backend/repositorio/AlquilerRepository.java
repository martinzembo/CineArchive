package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Alquiler;
import edu.utn.inspt.cinearchive.backend.modelo.AlquilerDetalle;
import java.util.List;

public interface AlquilerRepository {
    Alquiler findById(Long id);
    List<Alquiler> findByUsuarioId(Long usuarioId);
    List<AlquilerDetalle> findByUsuarioIdWithContenido(Long usuarioId);
    int save(Alquiler alquiler);
    int update(Alquiler alquiler);
    int delete(Long id);
}
