package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Alquiler;
import edu.utn.inspt.cinearchive.backend.modelo.AlquilerDetalle;
import java.time.LocalDateTime;
import java.util.List;

public interface AlquilerRepository {
    Alquiler findById(Long id);
    List<Alquiler> findByUsuarioId(Long usuarioId);
    List<AlquilerDetalle> findByUsuarioIdWithContenido(Long usuarioId);
    int save(Alquiler alquiler); // Debe asignar el ID generado en alquiler.setId()
    int update(Alquiler alquiler);
    int delete(Long id);
    // Verificar alquiler activo existente para evitar duplicados
    boolean existsActiveByUsuarioAndContenido(Long usuarioId, Long contenidoId);
    // Nuevo: buscar alquileres vencidos aún activos
    List<Alquiler> findExpiredActivos(LocalDateTime now);
}
