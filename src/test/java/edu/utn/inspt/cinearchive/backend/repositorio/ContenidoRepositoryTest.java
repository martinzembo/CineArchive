package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.config.AppConfig;
import edu.utn.inspt.cinearchive.backend.config.DatabaseConfig;
import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, DatabaseConfig.class})
public class ContenidoRepositoryTest {

    @Autowired
    private ContenidoRepository contenidoRepository;

    @Test
    public void testSearchPagedLightEmptyOk() {
        List<Contenido> page = contenidoRepository.searchPagedLight(null, null, null, "nombre", 1, 5);
        Assert.assertNotNull(page);
    }

    @Test
    public void testReserveCopyFailsWhenNoStock() {
        Contenido c = new Contenido();
        c.setTitulo("Test Pelicula");
        c.setGenero("accion");
        c.setCopiasDisponibles(0);
        c.setCopiasTotales(0);
        contenidoRepository.save(c);
        int updated = contenidoRepository.reserveCopy(c.getId());
        Assert.assertEquals(0, updated);
    }
}

