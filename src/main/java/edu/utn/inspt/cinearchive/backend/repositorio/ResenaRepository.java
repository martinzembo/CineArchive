package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para la entidad Reseña
 */
@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {

    List<Resena> findByUsuarioId(Long usuarioId);

    List<Resena> findByContenidoId(Long contenidoId);

    List<Resena> findByCalificacionGreaterThanEqual(Double calificacion);

    @Query("SELECT AVG(r.calificacion) FROM Resena r WHERE r.contenido.id = :contenidoId")
    Double calcularCalificacionPromedio(@Param("contenidoId") Long contenidoId);

    @Query("SELECT r FROM Resena r WHERE r.usuario.id = :usuarioId AND r.contenido.id = :contenidoId")
    List<Resena> findByUsuarioIdAndContenidoId(
        @Param("usuarioId") Long usuarioId,
        @Param("contenidoId") Long contenidoId
    );

    boolean existsByUsuarioIdAndContenidoId(Long usuarioId, Long contenidoId);
}
