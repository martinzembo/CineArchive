package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Transaccion;
import java.util.List;

public interface TransaccionService {
    Transaccion getById(Long id);
    List<Transaccion> getByUsuario(Long usuarioId);
    void create(Transaccion transaccion);
    void update(Transaccion transaccion);
    void delete(Long id);
}

