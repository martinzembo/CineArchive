<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CineArchive - Tu gestor de películas y series</title>
    <link rel="stylesheet" href="/css/styles.css">
</head>
<body>
    <header>
        <nav>
            <a href="/" class="logo">CineArchive</a>
            <button class="menu-toggle">☰</button>
            <div class="nav-links">
                <a href="/">Inicio</a>
                <a href="/mi-lista">Mi Lista</a>
                <a href="/para-ver">Para Ver</a>
                <a href="#" class="user-profile">👤 Mi Perfil</a>
                <button class="login-btn" onclick="window.location.href='/disenio/login.html'">Cerrar sesión</button>
            </div>
        </nav>
    </header>
    <div class="container">
        <section class="search-section">
            <div class="search-container">
                <form action="/catalogo" method="get">
                    <input type="text" name="q" class="search-input" placeholder="Buscar películas, series, actores..." value="${query}">
                    <button class="search-btn">🔍 Buscar</button>
                </form>
            </div>
        </section>

        <!-- Reutilizamos el catálogo mostrado por el controlador -->
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
    <script src="/js/script.js"></script>
</body>
</html>
