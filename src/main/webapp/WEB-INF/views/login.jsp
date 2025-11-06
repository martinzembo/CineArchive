<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CineArchive - Iniciar Sesión</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <div class="container">
        <div class="login-container">
            <h1 class="page-title">Iniciar Sesión</h1>
            <p class="subtitle">Ingresa a tu cuenta de CineArchive</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form class="login-form" action="${pageContext.request.contextPath}/login" method="post">
                <input type="email" name="email" placeholder="Correo electrónico" required autofocus>
                <input type="password" name="password" placeholder="Contraseña" required>

                <div class="checkbox-group">
                    <input type="checkbox" id="remember" name="remember">
                    <label for="remember">Recordarme</label>
                </div>

                <button type="submit">Iniciar Sesión</button>
            </form>

            <div class="login-links">
                <a href="${pageContext.request.contextPath}/recuperar-password">¿Olvidaste tu contraseña?</a>
                <div class="divider"><span>o</span></div>
                <a href="${pageContext.request.contextPath}/registro">Regístrate</a>
            </div>

            <!-- Botón para ver categorías -->
            <a href="${pageContext.request.contextPath}/categorias" class="categories-btn">
                <i class="fas fa-tags"></i>
                Ver Categorías
            </a>
        </div>
    </div>
</body>
</html>
