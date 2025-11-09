<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Eliminado taglib JSTL por falta de configuración -->
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalle - CineArchive</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script>window.APP_CTX='${pageContext.request.contextPath}';</script>
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
            <button class="login-btn" onclick="window.location.href='${pageContext.request.contextPath}/login'">Cerrar sesión</button>
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
                    <span class="badge">${contenido.genero}</span> •
                    <span>${contenido.duracion != null ? contenido.duracion + ' min' : ''}</span> •
                    <span>${contenido.anio}</span> •
                    <span class="rating-large">★★★★★</span>
                </div>
                <div class="rental-section">
                    <form action="${pageContext.request.contextPath}/alquilar" method="post">
                        <input type="hidden" name="contenidoId" value="${contenido.id}" />
                        <div class="rental-options">
                            <div class="rental-option selected">
                                <input type="radio" name="periodo" id="rental3" value="3" checked>
                                <label for="rental3"><span class="rental-duration">3 días</span><span class="rental-price-large">${contenido.precioAlquiler}</span></label>
                            </div>
                            <div class="rental-option">
                                <input type="radio" name="periodo" id="rental7" value="7">
                                <label for="rental7"><span class="rental-duration">7 días</span><span class="rental-price-large">${contenido.precioAlquiler}</span></label>
                            </div>
                        </div>
                        <button class="rent-btn-large" type="submit">🎬 Alquilar ahora</button>
                    </form>
                    <div class="action-buttons">
                        <button class="btn-secondary" onclick="addToListAjax(${contenido.id}, 'mi-lista')">➕ Mi Lista</button>
                        <button class="btn-secondary" onclick="addToListAjax(${contenido.id}, 'para-ver')">📋 Para Ver</button>
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
    </div>
</div>
<footer>
    <p>&copy; 2025 CineArchive. Todos los derechos reservados.</p>
</footer>
<script src="${pageContext.request.contextPath}/js/alquiler.js"></script>
<script src="${pageContext.request.contextPath}/js/listas.js"></script>
<script src="${pageContext.request.contextPath}/js/catalogo.js"></script>
<!-- Mensajes Flash -->
${not empty error ? '<div class="alert alert-error">' + error + '</div>' : ''}
${not empty msg ? '<div class="alert alert-success">' + msg + '</div>' : ''}
</body>
</html>
