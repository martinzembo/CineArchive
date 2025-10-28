package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import edu.utn.inspt.cinearchive.backend.repositorio.ContenidoRepository;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public class ContenidoServiceImpl implements ContenidoService {

    private final ContenidoRepository contenidoRepository;

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
}

