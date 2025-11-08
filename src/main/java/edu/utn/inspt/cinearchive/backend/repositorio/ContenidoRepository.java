package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Contenido
 */
@Repository
public interface ContenidoRepository extends JpaRepository<Contenido, Long> {

    List<Contenido> findByTitulo(String titulo);

    List<Contenido> findByTituloContainingIgnoreCase(String titulo);

    List<Contenido> findByGenero(String genero);

    List<Contenido> findByTipo(Contenido.Tipo tipo);

    List<Contenido> findByDisponibleParaAlquiler(Boolean disponible);

    List<Contenido> findByGestorInventarioId(Long gestorId);

    boolean existsByTitulo(String titulo);

    @Query("SELECT c FROM Contenido c WHERE c.anio = :anio")
    List<Contenido> findByAnio(@Param("anio") Integer anio);

    @Query("SELECT c FROM Contenido c JOIN c.categorias cc WHERE cc.categoria.id = :categoriaId")
    List<Contenido> findByCategoria(@Param("categoriaId") Long categoriaId);

    @Query("SELECT c FROM Contenido c WHERE c.disponibleParaAlquiler = true AND c.copiasDisponibles > 0")
    List<Contenido> findDisponiblesParaAlquiler();

    @Query("SELECT c FROM Contenido c WHERE c.tipo = :tipo AND c.anio = :anio")
    List<Contenido> findByTipoAndAnio(@Param("tipo") Contenido.Tipo tipo, @Param("anio") Integer anio);
}
