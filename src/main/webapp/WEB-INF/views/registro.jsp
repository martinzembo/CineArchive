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
    <div class="register-container">
        <h2>Crear Cuenta</h2>
        <p class="subtitle">Únete a CineArchive y disfruta del mejor contenido</p>
        <form class="login-form" action="${pageContext.request.contextPath}/registro" method="post">
            <input type="text" name="nombre" placeholder="Nombre completo" required />
            <input type="email" name="email" placeholder="Correo electrónico" required />
            <input type="password" name="password" placeholder="Contraseña" required />
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
