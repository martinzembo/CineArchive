package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Lista;
import edu.utn.inspt.cinearchive.backend.repositorio.ListaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListaServiceImpl implements ListaService {

    private final ListaRepository listaRepository;

    @Autowired
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

    @Override
    @Transactional
    public void addContenido(Long listaId, Long contenidoId) {
        if (!listaRepository.existeContenido(listaId, contenidoId)) {
            listaRepository.addContenido(listaId, contenidoId);
        }
    }

    @Override
    @Transactional
    public void removeContenido(Long listaId, Long contenidoId) {
        if (listaRepository.existeContenido(listaId, contenidoId)) {
            listaRepository.removeContenido(listaId, contenidoId);
        }
    }

    @Override
    public boolean existeContenido(Long listaId, Long contenidoId) {
        return listaRepository.existeContenido(listaId, contenidoId);
    }
}
