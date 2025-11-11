package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Transaccion;
import edu.utn.inspt.cinearchive.backend.repositorio.TransaccionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;

    @Autowired
    public TransaccionServiceImpl(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
    }

    @Override
    public Transaccion getById(Long id) {
        return transaccionRepository.findById(id);
    }

    @Override
    public List<Transaccion> getByUsuario(Long usuarioId) {
        return transaccionRepository.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional
    public void create(Transaccion transaccion) {
        transaccionRepository.save(transaccion);
    }

    @Override
    @Transactional
    public void update(Transaccion transaccion) {
        transaccionRepository.update(transaccion);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        transaccionRepository.delete(id);
    }
}
