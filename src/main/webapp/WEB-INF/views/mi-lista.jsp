<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Mi Lista - CineArchive</title>
    <link rel="stylesheet" href="/css/styles.css" />
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
        <h1 class="page-title">📚 Mi Biblioteca Personal</h1>

        <!-- Películas y Series Vistas -->
        <section class="category">
            <div class="section-header">
                <h2>✅ Vistas Recientemente</h2>
            </div>
            <div class="movie-row">
                <c:forEach var="c" items="${contenidos}">
                    <div class="movie-card">
                        <img src="${c.imagenUrl}" alt="${c.titulo}">
                        <div class="movie-info">
                            <div class="movie-title">${c.titulo}</div>
                            <div class="movie-rating">Mi rating: ★★★★★</div>
                            <div class="watched-date">Visto: --</div>
                            <button class="btn-secondary" onclick="window.location.href='/contenido/${c.id}'">Ver detalles</button>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>
    </div>

    <footer>
        <p>&copy; 2025 CineArchive. Todos los derechos reservados.</p>
    </footer>
</body>
</html>
