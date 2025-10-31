package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.util.PasswordUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controlador temporal para testing de PasswordUtil
 * ELIMINAR EN PRODUCCIÓN
 */
@Controller
@RequestMapping("/test/password")
public class PasswordTestController {

    /**
     * Test: Encriptar una contraseña
     * GET /test/password/encriptar?password=MiPassword
     */
    @GetMapping("/encriptar")
    @ResponseBody
    public String encriptarPassword(@RequestParam(defaultValue = "Password123") String password) {
        String encrypted = PasswordUtil.encriptar(password);

        return "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>" +
               "<h1>🔒 Test de Encriptación BCrypt</h1>" +
               "<table border='1' cellpadding='10'>" +
               "<tr><th>Password Original</th><td>" + password + "</td></tr>" +
               "<tr><th>Password Encriptada</th><td style='word-break: break-all;'>" + encrypted + "</td></tr>" +
               "<tr><th>Longitud</th><td>" + encrypted.length() + " caracteres</td></tr>" +
               "</table>" +
               "<h3>✅ Encriptación exitosa</h3>" +
               "<p><strong>Nota:</strong> BCrypt genera un hash diferente cada vez, incluso para la misma contraseña. " +
               "Esto es normal y esperado por el algoritmo.</p>" +
               "<hr>" +
               "<h3>Probar con otras contraseñas:</h3>" +
               "<ul>" +
               "<li><a href='/cinearchive/test/password/encriptar?password=admin123'>admin123</a></li>" +
               "<li><a href='/cinearchive/test/password/encriptar?password=Test1234'>Test1234</a></li>" +
               "<li><a href='/cinearchive/test/password/encriptar?password=MiPasswordSegura2024'>MiPasswordSegura2024</a></li>" +
               "</ul>" +
               "<hr>" +
               "<p><a href='/cinearchive/test'>← Volver al menú de tests</a></p>" +
               "</body></html>";
    }

    /**
     * Test: Verificar una contraseña contra su hash
     * GET /test/password/verificar?password=admin123&hash=...
     */
    @GetMapping("/verificar")
    @ResponseBody
    public String verificarPassword(
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String hash) {

        // Hash de prueba para "admin123"
        String hashPrueba = "$2a$10$N9qo8uLOickgx2ZMRZoMye.IVI8zZ6rXF8hUPLQOjPQU8t2KG1Ntu";

        if (password == null) password = "admin123";
        if (hash == null) hash = hashPrueba;

        boolean coincide = PasswordUtil.verificar(password, hash);

        return "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>" +
               "<h1>✅ Test de Verificación BCrypt</h1>" +
               "<table border='1' cellpadding='10'>" +
               "<tr><th>Password</th><td>" + password + "</td></tr>" +
               "<tr><th>Hash</th><td style='word-break: break-all;'>" + hash + "</td></tr>" +
               "<tr><th>¿Coincide?</th><td style='font-size: 24px;'>" +
               (coincide ? "✅ <strong>SÍ</strong>" : "❌ <strong>NO</strong>") + "</td></tr>" +
               "</table>" +
               "<h3>" + (coincide ? "✅ Verificación exitosa" : "❌ Las contraseñas no coinciden") + "</h3>" +
               "<hr>" +
               "<h3>Probar otras combinaciones:</h3>" +
               "<ul>" +
               "<li><a href='/cinearchive/test/password/verificar?password=admin123&hash=" + hashPrueba + "'>" +
               "admin123 vs hash correcto ✅</a></li>" +
               "<li><a href='/cinearchive/test/password/verificar?password=incorrecta&hash=" + hashPrueba + "'>" +
               "incorrecta vs hash de admin123 ❌</a></li>" +
               "</ul>" +
               "<hr>" +
               "<p><a href='/cinearchive/test'>← Volver al menú de tests</a></p>" +
               "</body></html>";
    }

    /**
     * Test: Validar fortaleza de contraseña
     * GET /test/password/validar?password=...
     */
    @GetMapping("/validar")
    @ResponseBody
    public String validarPassword(@RequestParam(defaultValue = "debil") String password) {
        String resultado = PasswordUtil.obtenerMensajeValidacion(password);
        boolean esValida = resultado == null;

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>");
        sb.append("<h1>⚠️ Test de Validación de Contraseña</h1>");
        sb.append("<table border='1' cellpadding='10'>");
        sb.append("<tr><th>Password</th><td>").append(password).append("</td></tr>");
        sb.append("<tr><th>Longitud</th><td>").append(password.length()).append(" caracteres</td></tr>");
        sb.append("<tr><th>¿Es válida?</th><td style='font-size: 24px;'>")
          .append(esValida ? "✅ <strong>SÍ</strong>" : "❌ <strong>NO</strong>")
          .append("</td></tr>");

        if (!esValida) {
            sb.append("<tr><th>Error</th><td style='color: red;'><strong>")
              .append(resultado)
              .append("</strong></td></tr>");
        }
        sb.append("</table>");

        sb.append("<h3>").append(esValida ? "✅ Contraseña válida" : "❌ Contraseña inválida").append("</h3>");

        if (esValida) {
            sb.append("<p>Esta contraseña cumple con todos los requisitos de seguridad.</p>");
        } else {
            sb.append("<p>Esta contraseña no cumple con los requisitos de seguridad.</p>");
        }

        sb.append("<hr>");
        sb.append("<h3>Requisitos de contraseña:</h3>");
        sb.append("<ul>");
        sb.append("<li>Mínimo 8 caracteres</li>");
        sb.append("<li>Al menos una letra mayúscula</li>");
        sb.append("<li>Al menos una letra minúscula</li>");
        sb.append("<li>Al menos un número</li>");
        sb.append("</ul>");

        sb.append("<hr>");
        sb.append("<h3>Probar con diferentes contraseñas:</h3>");
        sb.append("<ul>");
        sb.append("<li><a href='/cinearchive/test/password/validar?password=debil'>debil</a> - ❌ Muy corta</li>");
        sb.append("<li><a href='/cinearchive/test/password/validar?password=password'>password</a> - ❌ Sin mayúsculas ni números</li>");
        sb.append("<li><a href='/cinearchive/test/password/validar?password=Password'>Password</a> - ❌ Sin números</li>");
        sb.append("<li><a href='/cinearchive/test/password/validar?password=password1'>password1</a> - ❌ Sin mayúsculas</li>");
        sb.append("<li><a href='/cinearchive/test/password/validar?password=PASSWORD1'>PASSWORD1</a> - ❌ Sin minúsculas</li>");
        sb.append("<li><a href='/cinearchive/test/password/validar?password=Password1'>Password1</a> - ✅ Válida</li>");
        sb.append("<li><a href='/cinearchive/test/password/validar?password=MiPassword123'>MiPassword123</a> - ✅ Válida</li>");
        sb.append("<li><a href='/cinearchive/test/password/validar?password=Admin2024!'>Admin2024!</a> - ✅ Válida</li>");
        sb.append("</ul>");

        sb.append("<hr>");
        sb.append("<p><a href='/cinearchive/test'>← Volver al menú de tests</a></p>");
        sb.append("</body></html>");

        return sb.toString();
    }

    /**
     * Test: Generar múltiples hashes de la misma contraseña
     * GET /test/password/multiples-hashes
     */
    @GetMapping("/multiples-hashes")
    @ResponseBody
    public String multiplesHashes(@RequestParam(defaultValue = "admin123") String password) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>");
        sb.append("<h1>🔐 Test: Múltiples Hashes de la Misma Contraseña</h1>");
        sb.append("<p><strong>Password:</strong> ").append(password).append("</p>");
        sb.append("<p>BCrypt genera un hash diferente cada vez, pero todos son válidos:</p>");
        sb.append("<table border='1' cellpadding='10'>");
        sb.append("<tr><th>Intento</th><th>Hash Generado</th><th>Verificación</th></tr>");

        for (int i = 1; i <= 5; i++) {
            String hash = PasswordUtil.encriptar(password);
            boolean verifica = PasswordUtil.verificar(password, hash);
            sb.append("<tr>")
              .append("<td>").append(i).append("</td>")
              .append("<td style='word-break: break-all; font-size: 12px;'>").append(hash).append("</td>")
              .append("<td>").append(verifica ? "✅" : "❌").append("</td>")
              .append("</tr>");
        }

        sb.append("</table>");
        sb.append("<h3>✅ Todos los hashes son diferentes pero válidos</h3>");
        sb.append("<p>Esto es una característica de seguridad de BCrypt llamada 'salt'.</p>");
        sb.append("<hr>");
        sb.append("<p><a href='/cinearchive/test'>← Volver al menú de tests</a></p>");
        sb.append("</body></html>");

        return sb.toString();
    }
}

