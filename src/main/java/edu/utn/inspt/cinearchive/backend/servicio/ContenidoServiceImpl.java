package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import edu.utn.inspt.cinearchive.backend.repositorio.ContenidoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de Contenido
 */
@Service
public class ContenidoServiceImpl implements ContenidoService {

    private final ContenidoRepository contenidoRepository;

    @Autowired
    public ContenidoServiceImpl(ContenidoRepository contenidoRepository) {
        this.contenidoRepository = contenidoRepository;
    }

    // ===== MÉTODOS CRUD BÁSICOS =====

    @Override
    public Optional<Contenido> getById(Long id) {
        return contenidoRepository.findById(id);
    }

    @Override
    public List<Contenido> getAll() {
        return contenidoRepository.findAll();
    }

    @Override
    @Transactional
    @CacheEvict(value = {"catalogoPagedLight", "catalogoCount"}, allEntries = true)
    public void create(Contenido contenido) {
        contenidoRepository.save(contenido);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"catalogoPagedLight", "catalogoCount"}, allEntries = true)
    public void update(Contenido contenido) {
        contenidoRepository.update(contenido);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"catalogoPagedLight", "catalogoCount"}, allEntries = true)
    public void delete(Long id) {
        contenidoRepository.delete(id);
    }

    // ===== MÉTODOS DE BÚSQUEDA =====

    @Override
    public List<Contenido> searchByTitulo(String tituloPattern) {
        return contenidoRepository.findByTituloLike(tituloPattern);
    }

    @Override
    public List<Contenido> findByTitulo(String titulo) {
        return contenidoRepository.findByTitulo(titulo);
    }

    @Override
    public List<Contenido> findByGenero(String genero) {
        return contenidoRepository.findByGenero(genero);
    }

    @Override
    public List<Contenido> findByTipo(Contenido.Tipo tipo) {
        return contenidoRepository.findByTipo(tipo);
    }

    @Override
    public List<Contenido> findDisponiblesParaAlquiler() {
        return contenidoRepository.findDisponiblesParaAlquiler();
    }

    @Override
    public List<Contenido> findByGestorInventario(Long gestorId) {
        return contenidoRepository.findByGestorInventarioId(gestorId);
    }

    @Override
    public List<Contenido> getSeasonsByTitlePrefix(String titlePrefix) {
        return contenidoRepository.findSeasonsByTitlePrefix(titlePrefix);
    }

    // ===== BÚSQUEDA AVANZADA CON FILTROS =====

    @Override
    public List<Contenido> search(String q, String genero, String tipo, String orden) {
        return contenidoRepository.search(q, genero, tipo, orden);
    }

    @Override
    public List<Contenido> searchPaged(String q, String genero, String tipo, String orden, int page, int size) {
        return contenidoRepository.searchPaged(q, genero, tipo, orden, page, size);
    }

    @Override
    @Cacheable(value = "catalogoPagedLight", key = "#q + '|' + #genero + '|' + #tipo + '|' + #orden + '|' + #page + '|' + #size")
    public List<Contenido> searchPagedLight(String q, String genero, String tipo, String orden, int page, int size) {
        return contenidoRepository.searchPagedLight(q, genero, tipo, orden, page, size);
    }

    @Override
    @Cacheable(value = "catalogoCount", key = "#q + '|' + #genero + '|' + #tipo")
    public long searchCount(String q, String genero, String tipo) {
        return contenidoRepository.searchCount(q, genero, tipo);
    }

    // ===== MÉTODOS DE VALIDACIÓN =====

    @Override
    public boolean existsById(Long id) {
        return contenidoRepository.existsById(id);
    }

    @Override
    public boolean existsByTitulo(String titulo) {
        return contenidoRepository.existsByTitulo(titulo);
    }

    // ===== GESTIÓN DE CATEGORÍAS =====

    @Override
    @Transactional
    public void agregarCategoria(Long contenidoId, Long categoriaId) {
        // Implementación depende de si tienes un repositorio de categorías
        // Por ahora, delegar al repositorio si existe el método
        // O implementar la lógica aquí:
        Optional<Contenido> contenidoOpt = contenidoRepository.findById(contenidoId);
        if (contenidoOpt.isPresent()) {
            // Aquí deberías tener lógica para agregar la categoría
            // Requiere acceso a CategoriaRepository
            throw new UnsupportedOperationException("Implementación pendiente - requiere CategoriaRepository");
        } else {
            throw new IllegalArgumentException("Contenido no encontrado con ID: " + contenidoId);
        }
    }

    @Override
    @Transactional
    public void removerCategoria(Long contenidoId, Long categoriaId) {
        // Similar a agregarCategoria
        Optional<Contenido> contenidoOpt = contenidoRepository.findById(contenidoId);
        if (contenidoOpt.isPresent()) {
            // Aquí deberías tener lógica para remover la categoría
            throw new UnsupportedOperationException("Implementación pendiente - requiere CategoriaRepository");
        } else {
            throw new IllegalArgumentException("Contenido no encontrado con ID: " + contenidoId);
        }
    }

    // ===== GESTIÓN DE INVENTARIO =====

    @Override
    @Transactional
    @CacheEvict(value = {"catalogoPagedLight", "catalogoCount"}, allEntries = true)
    public boolean reservarCopia(Long contenidoId) {
        // Usar el método atómico del repositorio
        int filasAfectadas = contenidoRepository.reserveCopy(contenidoId);
        return filasAfectadas > 0;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"catalogoPagedLight", "catalogoCount"}, allEntries = true)
    public void devolverCopia(Long contenidoId) {
        // Incrementar las copias disponibles en 1
        int filasAfectadas = contenidoRepository.updateCopiasDisponibles(contenidoId, 1);
        if (filasAfectadas == 0) {
            throw new IllegalArgumentException("No se pudo devolver la copia. Contenido no encontrado con ID: " + contenidoId);
        }
    }
}