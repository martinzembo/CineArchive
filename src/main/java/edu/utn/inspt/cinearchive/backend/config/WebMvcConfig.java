package edu.utn.inspt.cinearchive.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Configuración de Spring MVC
 * Define ViewResolver, recursos estáticos, CORS, interceptores, etc.
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Configura el ViewResolver para páginas JSP
     * Las vistas JSP estarán en /WEB-INF/views/
     */
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setExposeContextBeansAsAttributes(true);
        resolver.setOrder(1);
        return resolver;
    }

    /**
     * Configura Gson para serialización/deserialización JSON
     * Usado en respuestas REST API
     */
    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setPrettyPrinting()
                .create();
    }

    /**
     * Configura recursos estáticos (CSS, JS, imágenes)
     * Sirve archivos desde /webapp/
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // CSS
        registry.addResourceHandler("/css/**")
                .addResourceLocations("/css/")
                .setCachePeriod(3600);

        // JavaScript
        registry.addResourceHandler("/js/**")
                .addResourceLocations("/js/")
                .setCachePeriod(3600);

        // Imágenes
        registry.addResourceHandler("/img/**")
                .addResourceLocations("/img/")
                .setCachePeriod(86400); // 24 horas

        // Diseños HTML estáticos
        registry.addResourceHandler("/disenio/**")
                .addResourceLocations("/disenio/")
                .setCachePeriod(3600);
    }

    /**
     * Configura CORS para permitir peticiones desde el frontend
     * Necesario para APIs REST consumidas desde JavaScript
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }

    /**
     * Configura la vista por defecto
     * Redirige "/" a index.jsp
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.jsp");
    }

    /**
     * Configura conversores de mensajes HTTP
     * Usa Gson para JSON en lugar de Jackson
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
        gsonConverter.setGson(gson());
        converters.add(gsonConverter);
    }

    /**
     * Habilita el DefaultServletHandler
     * Permite que Tomcat sirva recursos estáticos no manejados por Spring
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
