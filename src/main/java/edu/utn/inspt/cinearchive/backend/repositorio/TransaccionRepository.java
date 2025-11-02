package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Transaccion;
import java.util.List;

public interface TransaccionRepository {
    Transaccion findById(Long id);
    List<Transaccion> findByUsuarioId(Long usuarioId);
    int save(Transaccion transaccion);
    int update(Transaccion transaccion);
    int delete(Long id);
}

