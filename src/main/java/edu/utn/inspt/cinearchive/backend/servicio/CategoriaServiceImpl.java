package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Categoria;
import edu.utn.inspt.cinearchive.backend.repositorio.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Categoria> obtenerPorId(int id) {
        return categoriaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> obtenerPorTipo(Categoria.Tipo tipo) {
        return categoriaRepository.findByTipo(tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Categoria> obtenerPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    @Override
    public Categoria guardar(Categoria categoria) {
        validarCategoria(categoria);
        return categoriaRepository.save(categoria);
    }

    @Override
    public void eliminar(int id) {
        if (!categoriaRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe una categoría con el ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(int id) {
        return categoriaRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> obtenerGeneros() {
        return categoriaRepository.findByTipo(Categoria.Tipo.GENERO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> obtenerTags() {
        return categoriaRepository.findByTipo(Categoria.Tipo.TAG);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> obtenerClasificaciones() {
        return categoriaRepository.findByTipo(Categoria.Tipo.CLASIFICACION);
    }

    private void validarCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser null");
        }
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        if (categoria.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de categoría es obligatorio");
        }
        if (categoria.getId() == 0 && categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
    }
}
