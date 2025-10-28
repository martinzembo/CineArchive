package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Alquiler;
import edu.utn.inspt.cinearchive.backend.repositorio.AlquilerRepository;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public class AlquilerServiceImpl implements AlquilerService {

    private final AlquilerRepository alquilerRepository;

    public AlquilerServiceImpl(AlquilerRepository alquilerRepository) {
        this.alquilerRepository = alquilerRepository;
    }

    @Override
    public Alquiler getById(Long id) {
        return alquilerRepository.findById(id);
    }

    @Override
    public List<Alquiler> getByUsuario(Long usuarioId) {
        return alquilerRepository.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional
    public void create(Alquiler alquiler) {
        alquilerRepository.save(alquiler);
    }

    @Override
    @Transactional
    public void update(Alquiler alquiler) {
        alquilerRepository.update(alquiler);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        alquilerRepository.delete(id);
    }
}

