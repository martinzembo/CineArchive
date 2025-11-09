<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalle - CineArchive</title>
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
        <div class="detail-container">
            <div class="detail-hero">
                <img src="${contenido.imagenUrl}" alt="${contenido.titulo}" class="detail-poster">
                <div class="detail-info">
                    <h1>${contenido.titulo}</h1>
                    <div class="detail-meta">
                        <span class="badge">${contenido.genero}</span>
                        <span>•</span>
                        <span>${contenido.duracion != null ? contenido.duracion + ' min' : ''}</span>
                        <span>•</span>
                        <span>${contenido.anio}</span>
                        <span>•</span>
                        <span class="rating-large">★★★★★</span>
                    </div>

                    <div class="rental-section">
                        <form action="/alquilar" method="post">
                            <input type="hidden" name="contenidoId" value="${contenido.id}" />
                            <div class="rental-options">
                                <div class="rental-option selected">
                                    <input type="radio" name="periodo" id="rental3" value="3" checked>
                                    <label for="rental3">
                                        <span class="rental-duration">3 días</span>
                                        <span class="rental-price-large">${contenido.precioAlquiler}</span>
                                    </label>
                                </div>
                                <div class="rental-option">
                                    <input type="radio" name="periodo" id="rental7" value="7">
                                    <label for="rental7">
                                        <span class="rental-duration">7 días</span>
                                        <span class="rental-price-large">${contenido.precioAlquiler}</span>
                                    </label>
                                </div>
                            </div>
                            <button class="rent-btn-large" type="submit">🎬 Alquilar ahora</button>
                        </form>
                        <div class="action-buttons">
                            <button class="btn-secondary">➕ Agregar a Mi Lista</button>
                            <button class="btn-secondary">📋 Agregar a Para Ver</button>
                        </div>
                    </div>

                    <div class="detail-synopsis">
                        <h3>Sinopsis</h3>
                        <p>${contenido.descripcion}</p>
                    </div>

                    <div class="detail-cast">
                        <h3>Información Adicional</h3>
                        <p><strong>Director:</strong> ${contenido.director}</p>
                        <p><strong>Temporadas:</strong> ${contenido.temporadas}</p>
                    </div>

                    <div class="availability">
                        <p>✅ <strong>${contenido.disponibleParaAlquiler ? 'Disponible para alquiler' : 'No disponible'}</strong></p>
                        <p>${contenido.copiasDisponibles} copias disponibles • Alta calidad</p>
                    </div>
                </div>
            </div>

            <!-- Trailer -->
            <section class="admin-section">
                <h2>🎥 Tráiler Oficial</h2>
                <div style="background: #000; padding: 20px; border-radius: 10px; text-align: center;">
                    <p style="color: #888;">El reproductor de video se cargará aquí</p>
                </div>
            </section>

            <!-- Sección de reseñas -->
            <div class="reviews-section">
                <h2>✍️ Reseñas de Usuarios</h2>
                <!-- Reseñas dinámicas en futuros commits -->
            </div>

            <!-- Contenido relacionado -->
            <section class="category">
                <h2>🎬 Películas Similares</h2>
                <div class="movie-row">
                    <jsp:include page="/WEB-INF/views/catalogo.jsp" />
                </div>
            </section>
        </div>
    </div>

    <footer>
        <p>&copy; 2025 CineArchive. Todos los derechos reservados.</p>
    </footer>

    <script src="/js/script.js"></script>
</body>
</html>
