package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Categoria;
import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import edu.utn.inspt.cinearchive.backend.repositorio.ContenidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que maneja la lógica de negocio relacionada con los contenidos
 */
@Service
@Transactional
public class ContenidoServiceImpl implements ContenidoService {

    private final ContenidoRepository contenidoRepository;

    @Autowired
    public ContenidoServiceImpl(ContenidoRepository contenidoRepository) {
        this.contenidoRepository = contenidoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contenido> obtenerTodos() {
        return contenidoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Contenido> obtenerPorId(Long id) {
        return contenidoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contenido> buscarPorTitulo(String titulo) {
        return contenidoRepository.findByTituloContainingIgnoreCase(titulo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contenido> buscarPorGenero(String genero) {
        return contenidoRepository.findByGenero(genero);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contenido> buscarPorTipo(Contenido.Tipo tipo) {
        return contenidoRepository.findByTipo(tipo);
    }

    @Override
    public Contenido guardar(Contenido contenido) {
        validarContenido(contenido);
        return contenidoRepository.save(contenido);
    }

    @Override
    public void eliminar(Long id) {
        if (!contenidoRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe un contenido con el ID: " + id);
        }
        contenidoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(Long id) {
        return contenidoRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorTitulo(String titulo) {
        return contenidoRepository.existsByTitulo(titulo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contenido> obtenerDisponiblesParaAlquiler() {
        return contenidoRepository.findDisponiblesParaAlquiler();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contenido> obtenerPorGestorInventario(Long gestorId) {
        return contenidoRepository.findByGestorInventarioId(gestorId);
    }

    @Override
    public void agregarCategoria(Long contenidoId, Long categoriaId) {
        Contenido contenido = obtenerPorId(contenidoId)
            .orElseThrow(() -> new IllegalArgumentException("No existe el contenido"));

        // La categoría se valida en el método agregarCategoria de Contenido
        contenido.agregarCategoria(new Categoria(categoriaId));
        contenidoRepository.save(contenido);
    }

    @Override
    public void removerCategoria(Long contenidoId, Long categoriaId) {
        Contenido contenido = obtenerPorId(contenidoId)
            .orElseThrow(() -> new IllegalArgumentException("No existe el contenido"));

        contenido.removerCategoria(new Categoria(categoriaId));
        contenidoRepository.save(contenido);
    }

    @Override
    public boolean reservarCopia(Long contenidoId) {
        Contenido contenido = obtenerPorId(contenidoId)
            .orElseThrow(() -> new IllegalArgumentException("No existe el contenido"));

        if (!contenido.estaDisponibleParaAlquiler()) {
            return false;
        }

        contenido.reducirCopiasDisponibles();
        contenidoRepository.save(contenido);
        return true;
    }

    @Override
    public void devolverCopia(Long contenidoId) {
        Contenido contenido = obtenerPorId(contenidoId)
            .orElseThrow(() -> new IllegalArgumentException("No existe el contenido"));

        contenido.aumentarCopiasDisponibles();
        contenidoRepository.save(contenido);
    }

    private void validarContenido(Contenido contenido) {
        if (contenido == null) {
            throw new IllegalArgumentException("El contenido no puede ser null");
        }
        if (contenido.getTitulo() == null || contenido.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        if (contenido.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de contenido es obligatorio");
        }
        if (contenido.getAnio() == null) {
            throw new IllegalArgumentException("El año es obligatorio");
        }
        if (contenido.getId() == null && contenidoRepository.existsByTitulo(contenido.getTitulo())) {
            throw new IllegalArgumentException("Ya existe un contenido con el título: " + contenido.getTitulo());
        }
    }
}
