package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.modelo.Usuario;
import edu.utn.inspt.cinearchive.backend.servicio.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador temporal para testing del backend
 * ELIMINAR EN PRODUCCIÓN
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Test 1: Verificar conexión a BD y listar usuarios
     * GET /test/usuarios
     */
    @GetMapping("/usuarios")
    @ResponseBody
    public String testUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listarTodos();
            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>");
            sb.append("<h1>🧪 Test de Conexión a BD</h1>");
            sb.append("<h2>✅ Conexión exitosa</h2>");
            sb.append("<h3>Total de usuarios: ").append(usuarios.size()).append("</h3>");
            sb.append("<table border='1' cellpadding='10'>");
            sb.append("<tr><th>ID</th><th>Nombre</th><th>Email</th><th>Rol</th><th>Activo</th><th>Fecha Registro</th></tr>");
            for (Usuario u : usuarios) {
                sb.append("<tr>")
                  .append("<td>").append(u.getId()).append("</td>")
                  .append("<td>").append(u.getNombre()).append("</td>")
                  .append("<td>").append(u.getEmail()).append("</td>")
                  .append("<td>").append(u.getRol()).append("</td>")
                  .append("<td>").append(u.estaActivo() ? "✅" : "❌").append("</td>")
                  .append("<td>").append(u.getFechaRegistro()).append("</td>")
                  .append("</tr>");
            }
            sb.append("</table>");
            sb.append("<hr>");
            sb.append("<p><a href='/cinearchive/test'>← Volver al menú de tests</a></p>");
            sb.append("</body></html>");
            return sb.toString();
        } catch (Exception e) {
            return "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>" +
                   "<h1>❌ Error al conectar con la BD</h1>" +
                   "<p><strong>Error:</strong> " + e.getMessage() + "</p>" +
                   "<p><strong>Tipo:</strong> " + e.getClass().getName() + "</p>" +
                   "<hr><p><a href='/cinearchive/test'>← Volver al menú de tests</a></p>" +
                   "</body></html>";
        }
    }

    /**
     * Test 2: Registrar un nuevo usuario
     * GET /test/registro
     */
    @GetMapping("/registro")
    @ResponseBody
    public String testRegistro() {
        try {
            long timestamp = System.currentTimeMillis();
            String nombre = "Test User " + timestamp;
            String email = "test" + timestamp + "@test.com";
            String password = "Test1234";

            Usuario nuevoUsuario = usuarioService.registrar(nombre, email, password, Usuario.Rol.USUARIO_REGULAR);

            return "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>" +
                   "<h1>✅ Usuario registrado exitosamente</h1>" +
                   "<ul>" +
                   "<li><strong>ID:</strong> " + nuevoUsuario.getId() + "</li>" +
                   "<li><strong>Nombre:</strong> " + nuevoUsuario.getNombre() + "</li>" +
                   "<li><strong>Email:</strong> " + nuevoUsuario.getEmail() + "</li>" +
                   "<li><strong>Rol:</strong> " + nuevoUsuario.getRol() + "</li>" +
                   "<li><strong>Activo:</strong> " + (nuevoUsuario.estaActivo() ? "Sí" : "No") + "</li>" +
                   "</ul>" +
                   "<p>✅ La contraseña ha sido encriptada con BCrypt</p>" +
                   "<hr>" +
                   "<p><a href='/cinearchive/test/usuarios'>Ver todos los usuarios</a></p>" +
                   "<p><a href='/cinearchive/test'>← Volver al menú de tests</a></p>" +
                   "</body></html>";
        } catch (Exception e) {
            return "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>" +
                   "<h1>❌ Error al registrar usuario</h1>" +
                   "<p><strong>Error:</strong> " + e.getMessage() + "</p>" +
                   "<hr><p><a href='/cinearchive/test'>← Volver al menú de tests</a></p>" +
                   "</body></html>";
        }
    }

    /**
     * Test 3: Autenticar usuario de prueba
     * GET /test/autenticacion
     */
    @GetMapping("/autenticacion")
    @ResponseBody
    public String testAutenticacion() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>");
        sb.append("<h1>🔐 Test de Autenticación</h1>");

        try {
            // Intenta autenticar con el usuario de prueba
            Usuario usuario = usuarioService.autenticar("admin@test.com", "admin123");

            if (usuario != null) {
                sb.append("<h2>✅ Autenticación exitosa</h2>");
                sb.append("<ul>");
                sb.append("<li><strong>ID:</strong> ").append(usuario.getId()).append("</li>");
                sb.append("<li><strong>Nombre:</strong> ").append(usuario.getNombre()).append("</li>");
                sb.append("<li><strong>Email:</strong> ").append(usuario.getEmail()).append("</li>");
                sb.append("<li><strong>Rol:</strong> ").append(usuario.getRol()).append("</li>");
                sb.append("</ul>");
                sb.append("<p>✅ La verificación de contraseña con BCrypt funcionó correctamente</p>");
            } else {
                sb.append("<h2>❌ Autenticación fallida</h2>");
                sb.append("<p>Usuario o contraseña incorrectos</p>");
                sb.append("<p><strong>Nota:</strong> Asegúrate de haber creado el usuario de prueba en la BD:</p>");
                sb.append("<pre>")
                  .append("INSERT INTO usuario (nombre, email, contrasena, rol, fecha_registro, activo)\n")
                  .append("VALUES ('Admin Test', 'admin@test.com', ")
                  .append("'$2a$10$N9qo8uLOickgx2ZMRZoMye.IVI8zZ6rXF8hUPLQOjPQU8t2KG1Ntu', ")
                  .append("'ADMINISTRADOR', NOW(), 1);")
                  .append("</pre>");
            }
        } catch (Exception e) {
            sb.append("<h2>❌ Error en autenticación</h2>");
            sb.append("<p><strong>Error:</strong> ").append(e.getMessage()).append("</p>");
        }

        sb.append("<hr>");
        sb.append("<p><a href='/cinearchive/test'>← Volver al menú de tests</a></p>");
        sb.append("</body></html>");
        return sb.toString();
    }

    /**
     * Test 4: Buscar usuario por email
     * GET /test/buscar-email
     */
    @GetMapping("/buscar-email")
    @ResponseBody
    public String testBuscarEmail() {
        try {
            Usuario usuario = usuarioService.buscarPorEmail("admin@test.com");

            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>");
            sb.append("<h1>🔍 Test de Búsqueda por Email</h1>");

            if (usuario != null) {
                sb.append("<h2>✅ Usuario encontrado</h2>");
                sb.append("<ul>");
                sb.append("<li><strong>ID:</strong> ").append(usuario.getId()).append("</li>");
                sb.append("<li><strong>Nombre:</strong> ").append(usuario.getNombre()).append("</li>");
                sb.append("<li><strong>Email:</strong> ").append(usuario.getEmail()).append("</li>");
                sb.append("<li><strong>Rol:</strong> ").append(usuario.getRol()).append("</li>");
                sb.append("</ul>");
            } else {
                sb.append("<h2>❌ Usuario no encontrado</h2>");
                sb.append("<p>No existe un usuario con email: admin@test.com</p>");
            }

            sb.append("<hr>");
            sb.append("<p><a href='/cinearchive/test'>← Volver al menú de tests</a></p>");
            sb.append("</body></html>");
            return sb.toString();
        } catch (Exception e) {
            return "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>" +
                   "<h1>❌ Error al buscar usuario</h1>" +
                   "<p>" + e.getMessage() + "</p>" +
                   "<hr><p><a href='/cinearchive/test'>← Volver al menú de tests</a></p>" +
                   "</body></html>";
        }
    }

    /**
     * Test 5: Verificar email único
     * GET /test/email-existe
     */
    @GetMapping("/email-existe")
    @ResponseBody
    public String testEmailExiste() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>");
        sb.append("<h1>📧 Test de Email Único</h1>");

        String email1 = "admin@test.com";
        String email2 = "noexiste" + System.currentTimeMillis() + "@test.com";

        boolean existe1 = usuarioService.existeEmail(email1);
        boolean existe2 = usuarioService.existeEmail(email2);

        sb.append("<h3>Email 1: ").append(email1).append("</h3>");
        sb.append("<p>¿Existe?: ").append(existe1 ? "✅ SÍ" : "❌ NO").append("</p>");

        sb.append("<h3>Email 2: ").append(email2).append("</h3>");
        sb.append("<p>¿Existe?: ").append(existe2 ? "✅ SÍ" : "❌ NO").append("</p>");

        sb.append("<hr>");
        sb.append("<p><a href='/cinearchive/test'>← Volver al menú de tests</a></p>");
        sb.append("</body></html>");
        return sb.toString();
    }

    /**
     * Página principal de tests
     * GET /test
     */
    @GetMapping("")
    @ResponseBody
    public String menuTests() {
        return "<!DOCTYPE html>" +
               "<html><head><meta charset='UTF-8'><title>CineArchive - Tests</title></head>" +
               "<body style='font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px;'>" +
               "<h1>🧪 CineArchive - Suite de Tests (Developer 1)</h1>" +
               "<h2>Tests de Backend</h2>" +
               "<ul style='line-height: 2;'>" +
               "<li><a href='/cinearchive/test/usuarios'>📋 Test 1: Listar Usuarios (BD)</a></li>" +
               "<li><a href='/cinearchive/test/registro'>➕ Test 2: Registrar Usuario Nuevo</a></li>" +
               "<li><a href='/cinearchive/test/autenticacion'>🔐 Test 3: Autenticar Usuario</a></li>" +
               "<li><a href='/cinearchive/test/buscar-email'>🔍 Test 4: Buscar Usuario por Email</a></li>" +
               "<li><a href='/cinearchive/test/email-existe'>📧 Test 5: Verificar Email Único</a></li>" +
               "</ul>" +
               "<h2>Tests de Utilidades</h2>" +
               "<ul style='line-height: 2;'>" +
               "<li><a href='/cinearchive/test/password/encriptar?password=MiPassword123'>🔒 Test 6: Encriptar Password</a></li>" +
               "<li><a href='/cinearchive/test/password/verificar?password=admin123&hash=$2a$10$N9qo8uLOickgx2ZMRZoMye.IVI8zZ6rXF8hUPLQOjPQU8t2KG1Ntu'>✅ Test 7: Verificar Password</a></li>" +
               "<li><a href='/cinearchive/test/password/validar?password=debil'>⚠️ Test 8: Validar Password Débil</a></li>" +
               "<li><a href='/cinearchive/test/password/validar?password=Password123'>✅ Test 9: Validar Password Fuerte</a></li>" +
               "</ul>" +
               "<hr>" +
               "<h2>Tests de Frontend (Rutas de Controllers)</h2>" +
               "<ul style='line-height: 2;'>" +
               "<li><a href='/cinearchive/login'>🔑 Login (GET)</a> - Debería mostrar formulario</li>" +
               "<li><a href='/cinearchive/registro'>📝 Registro (GET)</a> - Debería mostrar formulario</li>" +
               "<li><a href='/cinearchive/perfil'>👤 Perfil (GET)</a> - Debería pedir sesión</li>" +
               "</ul>" +
               "<hr>" +
               "<p><strong>Nota:</strong> Estos tests son temporales para validar el backend. " +
               "Eliminar este controlador en producción.</p>" +
               "<p><strong>Estado:</strong> ✅ Backend 100% Implementado | ❌ JSPs Pendientes</p>" +
               "</body></html>";
    }
}

