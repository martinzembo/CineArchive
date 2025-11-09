package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Alquiler;
import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import edu.utn.inspt.cinearchive.backend.repositorio.AlquilerRepository;
import edu.utn.inspt.cinearchive.backend.repositorio.ContenidoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlquilerServiceImpl implements AlquilerService {

    private final AlquilerRepository alquilerRepository;
    private final ContenidoRepository contenidoRepository;

    @Autowired
    public AlquilerServiceImpl(AlquilerRepository alquilerRepository, ContenidoRepository contenidoRepository) {
        this.alquilerRepository = alquilerRepository;
        this.contenidoRepository = contenidoRepository;
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

    @Override
    @Transactional
    public void rent(Long usuarioId, Long contenidoId, Integer periodoDias) {
        if (usuarioId == null || contenidoId == null || periodoDias == null || periodoDias <= 0) {
            throw new IllegalArgumentException("Parámetros inválidos para alquiler");
        }
        Contenido c = contenidoRepository.findById(contenidoId);
        if (c == null || Boolean.FALSE.equals(c.getDisponibleParaAlquiler())) {
            throw new IllegalStateException("Contenido no disponible para alquiler");
        }
        if (c.getCopiasDisponibles() == null || c.getCopiasDisponibles() <= 0) {
            throw new IllegalStateException("No hay copias disponibles");
        }
        // Calcular fechas y precio (simple: precio base según contenido)
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fin = inicio.plusDays(periodoDias);
        BigDecimal precio = c.getPrecioAlquiler() != null ? c.getPrecioAlquiler() : BigDecimal.ZERO;

        Alquiler a = new Alquiler();
        a.setUsuarioId(usuarioId);
        a.setContenidoId(contenidoId);
        a.setPeriodoAlquiler(periodoDias);
        a.setFechaInicio(inicio);
        a.setFechaFin(fin);
        a.setPrecio(precio);
        a.setEstado(Alquiler.Estado.ACTIVO);
        a.setVisto(false);
        a.setFechaVista(null);
        alquilerRepository.save(a);
        // Decrementar una copia
        contenidoRepository.updateCopiasDisponibles(contenidoId, -1);
    }
}
