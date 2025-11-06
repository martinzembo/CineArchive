package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.modelo.Categoria;
import edu.utn.inspt.cinearchive.backend.servicio.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/categorias")
public class CategoriaViewController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String mostrarCategorias(Model model) {
        List<Categoria> todasLasCategorias = categoriaService.obtenerTodas();

        // Crear mapa manualmente para mantener el orden deseado
        Map<String, List<Categoria>> categoriasPorTipo = new LinkedHashMap<>();

        // Agregar géneros
        List<Categoria> generos = categoriaService.obtenerGeneros();
        if (!generos.isEmpty()) {
            categoriasPorTipo.put("GENERO", generos);
        }

        // Agregar tags
        List<Categoria> tags = categoriaService.obtenerTags();
        if (!tags.isEmpty()) {
            categoriasPorTipo.put("TAG", tags);
        }

        // Agregar clasificaciones
        List<Categoria> clasificaciones = categoriaService.obtenerClasificaciones();
        if (!clasificaciones.isEmpty()) {
            categoriasPorTipo.put("CLASIFICACION", clasificaciones);
        }

        model.addAttribute("categoriasPorTipo", categoriasPorTipo);
        return "categorias";
    }
}
