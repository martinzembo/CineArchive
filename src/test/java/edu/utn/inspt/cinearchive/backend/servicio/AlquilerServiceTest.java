package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.config.AppConfig;
import edu.utn.inspt.cinearchive.backend.config.DatabaseConfig;
import edu.utn.inspt.cinearchive.backend.modelo.Alquiler;
import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import edu.utn.inspt.cinearchive.backend.repositorio.ContenidoRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, DatabaseConfig.class})
public class AlquilerServiceTest {

    @Autowired
    private AlquilerService alquilerService;

    @Autowired
    private ContenidoRepository contenidoRepository;

    @Test(expected = IllegalStateException.class)
    public void testRentFailsNoStock() {
        Contenido c = new Contenido();
        c.setTitulo("Sin Stock");
        c.setGenero("accion");
        c.setPrecioAlquiler(BigDecimal.valueOf(50));
        c.setCopiasDisponibles(0);
        c.setCopiasTotales(0);
        contenidoRepository.save(c);
        // asegurar que se asignó ID
        Assert.assertNotNull(c.getId());
        alquilerService.rent(5L, c.getId(), 3, "TARJETA");
    }

    @Test
    public void testRentCreatesAlquilerAndPreventsDuplicate() {
        Contenido c = new Contenido();
        c.setTitulo("Alquiler Test");
        c.setGenero("drama");
        c.setPrecioAlquiler(BigDecimal.valueOf(100));
        c.setCopiasDisponibles(2);
        c.setCopiasTotales(2);
        contenidoRepository.save(c);
        Assert.assertNotNull(c.getId());
        Long usuarioId = 10L;
        alquilerService.rent(usuarioId, c.getId(), 3, "TARJETA");
        boolean duplicatedBlocked = false;
        try {
            alquilerService.rent(usuarioId, c.getId(), 3, "TARJETA");
        } catch (IllegalStateException ex) {
            duplicatedBlocked = true;
        }
        Assert.assertTrue("Debe impedir alquiler duplicado activo", duplicatedBlocked);
    }
}
