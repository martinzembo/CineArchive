<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CineArchive - Tu gestor de películas y series</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <header>
        <nav>
            <a href="${pageContext.request.contextPath}/" class="logo">CineArchive</a>
            <button class="menu-toggle">☰</button>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/">Inicio</a>
                <a href="${pageContext.request.contextPath}/mi-lista">Mi Lista</a>
                <a href="${pageContext.request.contextPath}/para-ver">Para Ver</a>
                <a href="#" class="user-profile">👤 Mi Perfil</a>
                <a href="${pageContext.request.contextPath}/api/categorias" class="categories-btn">
                    <i class="fas fa-tags"></i>
                    Ver Categorías
                </a>
                <button class="login-btn" onclick="window.location.href='${pageContext.request.contextPath}/login'">Cerrar sesión</button>
            </div>
        </nav>
    </header>

    <div class="container">
        <!-- Barra de búsqueda y filtros -->
        <section class="search-section">
            <div class="search-container">
                <input type="text" class="search-input" placeholder="Buscar películas, series, actores...">
                <button class="search-btn">🔍 Buscar</button>
            </div>
            <div class="filter-container">
                <select class="filter-select" id="generos">
                    <option value="">Todos los géneros</option>
                    <!-- Los géneros se cargarán dinámicamente -->
                </select>
                <select class="filter-select">
                    <option value="">Tipo de contenido</option>
                    <option value="peliculas">Películas</option>
                    <option value="series">Series</option>
                </select>
                <select class="filter-select">
                    <option value="">Ordenar por</option>
                    <option value="popularidad">Más Popular</option>
                    <option value="fecha">Más Reciente</option>
                    <option value="calificacion">Mejor Calificado</option>
                    <option value="nombre">Nombre A-Z</option>
                </select>
            </div>
        </section>

        <!-- Mis Alquileres Activos -->
        <section class="category highlighted-section">
            <h2>🎬 Mis Alquileres Activos</h2>
            <div class="movie-row">
                <div class="movie-card rental-active">
                    <img src="${pageContext.request.contextPath}/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg" alt="Película 1">
                    <div class="movie-info">
                        <div class="movie-title">Interstellar</div>
                        <div class="movie-rating">★★★★★</div>
                        <div class="rental-time">Quedan 2 días</div>
                        <button class="watch-btn">▶ Ver ahora</button>
                    </div>
                </div>
                <div class="movie-card rental-active">
                    <img src="${pageContext.request.contextPath}/img/MV5BOGVkODYxMDEtODczZC00MjRiLTg3ZWYtZjgzN2QyMDBjZTUzXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg" alt="Serie 1">
                    <div class="movie-info">
                        <div class="movie-title">Breaking Bad T1</div>
                        <div class="movie-rating">★★★★★</div>
                        <div class="rental-time">Quedan 5 días</div>
                        <button class="watch-btn">▶ Ver ahora</button>
                    </div>
                </div>
            </div>
        </section>

        <!-- Novedades -->
        <section class="category">
            <h2>🆕 Novedades</h2>
            <div class="movie-row">
                <!-- El contenido se cargará dinámicamente -->
            </div>
        </section>
    </div>

    <script src="${pageContext.request.contextPath}/js/categorias.js"></script>
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
