<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Registro - CineArchive</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body>
<jsp:include page="/WEB-INF/views/fragments/header.jsp" />
<div class="container">
    <div class="login-container">
        <h2>Crear Cuenta</h2>
        <p class="subtitle">Únete a CineArchive y disfruta del mejor contenido</p>

        <%-- Mostrar mensaje de error si existe --%>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error" style="background-color: #ff4444; color: white; padding: 10px; margin-bottom: 15px; border-radius: 5px;">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <form class="login-form" action="${pageContext.request.contextPath}/registro" method="post">
            <input type="text" name="nombre" placeholder="Nombre completo" required value="<%= request.getAttribute("nombre") != null ? request.getAttribute("nombre") : "" %>" />
            <input type="email" name="email" placeholder="Correo electrónico" required value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" />
            <input type="password" name="password" placeholder="Contraseña" required />
            <input type="password" name="passwordConfirm" placeholder="Confirmar contraseña" required />
            <input type="date" name="fechaNacimiento" placeholder="Fecha de nacimiento" />
            <button type="submit">Registrarse</button>
        </form>
        <div class="login-links">
            <span>¿Ya tienes cuenta? </span><a href="${pageContext.request.contextPath}/login">Iniciar Sesión</a>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
</body>
</html>
