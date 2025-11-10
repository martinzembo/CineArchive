<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CineArchive - Tu gestor de películas y series</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<header>
    <nav>
        <a href="${pageContext.request.contextPath}/catalogo" class="logo">CineArchive</a>
        <button class="menu-toggle">☰</button>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/catalogo">Inicio</a>
            <a href="${pageContext.request.contextPath}/mi-lista">Mi Lista</a>
            <a href="${pageContext.request.contextPath}/para-ver">Para Ver</a>
            <a href="${pageContext.request.contextPath}/mis-alquileres">Alquileres</a>
            <a href="#" class="user-profile" title="Perfil (pendiente Dev 1)">👤 Perfil</a>
        </div>
    </nav>
</header>
<div class="container">
    <section class="search-section">
        <div class="search-container">
            <form action="${pageContext.request.contextPath}/catalogo" method="get">
                <label for="q" class="sr-only">Buscar</label>
                <input id="q" type="text" name="q" class="search-input" placeholder="Buscar películas, series, actores..." value="${empty param.q ? '' : param.q}">
                <button class="search-btn" type="submit">🔍 Buscar</button>
            </form>
        </div>
    </section>

    <!-- Incluimos catálogo reutilizable -->
    <jsp:include page="/WEB-INF/views/catalogo.jsp" />
</div>
<footer>
    <p>&copy; 2025 CineArchive. Todos los derechos reservados.</p>
    <div class="footer-links">
        <a href="#">Acerca de</a>
        <a href="#">Privacidad</a>
        <a href="#">Términos de uso</a>
        <a href="#">Contacto</a>
        <a href="#">Centro de ayuda</a>
    </div>
</footer>
<script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
