package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import edu.utn.inspt.cinearchive.backend.repositorio.ContenidoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContenidoServiceImpl implements ContenidoService {

    private final ContenidoRepository contenidoRepository;

    @Autowired
    public ContenidoServiceImpl(ContenidoRepository contenidoRepository) {
        this.contenidoRepository = contenidoRepository;
    }

    @Override
    public Contenido getById(Long id) {
        return contenidoRepository.findById(id);
    }

    @Override
    public List<Contenido> getAll() {
        return contenidoRepository.findAll();
    }

    @Override
    public List<Contenido> searchByTitulo(String tituloPattern) {
        return contenidoRepository.findByTituloLike(tituloPattern);
    }

    @Override
    public List<Contenido> search(String q, String genero, String tipo, String orden) {
        return contenidoRepository.search(q, genero, tipo, orden);
    }

    @Override
    @Transactional
    public void create(Contenido contenido) {
        contenidoRepository.save(contenido);
    }

    @Override
    @Transactional
    public void update(Contenido contenido) {
        contenidoRepository.update(contenido);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        contenidoRepository.delete(id);
    }

    @Override
    public List<Contenido> searchPaged(String q, String genero, String tipo, String orden, int page, int size) {
        return contenidoRepository.searchPaged(q, genero, tipo, orden, page, size);
    }

    @Override
    @Cacheable(value = "catalogoCount", key = "#q + '|' + #genero + '|' + #tipo")
    public long searchCount(String q, String genero, String tipo) {
        return contenidoRepository.searchCount(q, genero, tipo);
    }

    @Override
    @Cacheable(value = "catalogoPagedLight", key = "#q + '|' + #genero + '|' + #tipo + '|' + #orden + '|' + #page + '|' + #size")
    public List<Contenido> searchPagedLight(String q, String genero, String tipo, String orden, int page, int size) {
        return contenidoRepository.searchPagedLight(q, genero, tipo, orden, page, size);
    }

    @Override
    public List<Contenido> getSeasonsByTitlePrefix(String titlePrefix) {
        return contenidoRepository.findSeasonsByTitlePrefix(titlePrefix);
    }
}
