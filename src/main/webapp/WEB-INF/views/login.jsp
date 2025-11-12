<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Configurar headers de no-cache en el JSP también
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <!-- Meta tags para prevenir caché del navegador -->
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />

    <title>CineArchive - Iniciar Sesión</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body>
<jsp:include page="/WEB-INF/views/fragments/header.jsp" />
<div class="container">
    <div class="login-container">
        <h2>Iniciar Sesión</h2>
        <p class="subtitle">Accede a tu cuenta de CineArchive</p>

        <%-- Mostrar mensaje de registro exitoso --%>
        <% if ("registroExitoso".equals(request.getParameter("mensaje"))) { %>
            <div class="alert alert-success" style="background-color: #28a745; color: white; padding: 10px; margin-bottom: 15px; border-radius: 5px; text-align: center;">
                ¡Registro exitoso! Ahora puedes iniciar sesión con tu cuenta.
            </div>
        <% } %>

        <%-- Mostrar mensaje de error si existe --%>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error" style="background-color: #ff4444; color: white; padding: 10px; margin-bottom: 15px; border-radius: 5px; text-align: center;">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <form class="login-form" action="${pageContext.request.contextPath}/login" method="post">
            <input type="email" name="email" placeholder="Correo electrónico" required />
            <input type="password" name="password" placeholder="Contraseña" required />
            <button type="submit">Iniciar Sesión</button>
        </form>
        <div class="login-links">
            <a href="#">¿Olvidaste tu contraseña?</a>
            <br /><br />
            <span>¿No tienes una cuenta? </span><a href="${pageContext.request.contextPath}/registro">Regístrate</a>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp" />

<script>
    // Detectar si la página se cargó desde el caché del navegador (bfcache)
    // Esto sucede cuando el usuario presiona el botón "Atrás" del navegador
    window.addEventListener('pageshow', function(event) {
        // event.persisted es true si la página viene del bfcache
        if (event.persisted) {
            console.log('Página cargada desde caché del navegador - Forzando recarga...');
            // Forzar recarga completa desde el servidor
            window.location.reload(true);
        }
    });

    // Prevenir navegación hacia atrás usando el caché
    window.addEventListener('load', function() {
        // Reemplazar el historial actual para evitar navegación hacia atrás
        window.history.pushState(null, document.title, window.location.href);

        // Detectar cuando el usuario intenta ir hacia atrás
        window.addEventListener('popstate', function(event) {
            // Volver a empujar el estado para mantenerse en login
            window.history.pushState(null, document.title, window.location.href);
            console.log('Navegación hacia atrás bloqueada en la página de login');
        });
    });

    // Invalidar sesión usando AJAX cuando la página se carga
    // Esto asegura que siempre se invalide la sesión, incluso si viene desde caché
    (function() {
        fetch('${pageContext.request.contextPath}/api/session/invalidate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'same-origin'
        }).then(function(response) {
            if (response.ok) {
                console.log('Sesión invalidada correctamente');
            }
        }).catch(function(error) {
            console.log('Nota: La invalidación de sesión será manejada por el servidor');
        });
    })();
</script>
</body>
</html>
