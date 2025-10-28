package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Lista;
import edu.utn.inspt.cinearchive.backend.repositorio.ListaRepository;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public class ListaServiceImpl implements ListaService {

    private final ListaRepository listaRepository;

    public ListaServiceImpl(ListaRepository listaRepository) {
        this.listaRepository = listaRepository;
    }

    @Override
    public Lista getById(Long id) {
        return listaRepository.findById(id);
    }

    @Override
    public List<Lista> getByUsuario(Long usuarioId) {
        return listaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional
    public void create(Lista lista) {
        listaRepository.save(lista);
    }

    @Override
    @Transactional
    public void update(Lista lista) {
        listaRepository.update(lista);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        listaRepository.delete(id);
    }
}

