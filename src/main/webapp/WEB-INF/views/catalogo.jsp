<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>CineArchive - Catálogo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
    <script>window.APP_CTX='${pageContext.request.contextPath}';</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/fragments/header.jsp" />
<div class="container">
    <section class="search-section">
        <form action="${pageContext.request.contextPath}/catalogo" method="get" id="searchForm">
            <div class="search-container">
                <input type="text" name="q" class="search-input" placeholder="Buscar películas, series, actores..." value="${param.q}" />
                <button type="submit" class="search-btn">🔍 Buscar</button>
            </div>
            <div class="filter-container">
                <select name="genero" class="filter-select">
                    <option value="">Todos los géneros</option>
                    <option value="accion">Acción</option>
                    <option value="comedia">Comedia</option>
                    <option value="drama">Drama</option>
                    <option value="terror">Terror</option>
                    <option value="ciencia-ficcion">Ciencia Ficción</option>
                    <option value="romance">Romance</option>
                    <option value="thriller">Thriller</option>
                    <option value="animacion">Animación</option>
                </select>
                <select name="tipo" class="filter-select">
                    <option value="">Tipo de contenido</option>
                    <option value="PELICULA">Películas</option>
                    <option value="SERIE">Series</option>
                </select>
                <select name="orden" class="filter-select">
                    <option value="">Ordenar por</option>
                    <option value="popularidad">Más Popular</option>
                    <option value="fecha">Más Reciente</option>
                    <option value="calificacion">Mejor Calificado</option>
                    <option value="nombre">Nombre A-Z</option>
                </select>
            </div>
        </form>
    </section>
    <section class="category">
        <h2>Resultados</h2>
        <div class="movie-row">
            <c:choose>
                <c:when test="${not empty contenidos}">
                    <c:forEach var="c" items="${contenidos}">
                        <div class="movie-card">
                            <img src="${c.imagenUrl}" alt="${c.titulo}" />
                            <div class="movie-info">
                                <div class="movie-title">${c.titulo}</div>
                                <div class="movie-rating">★★★★★</div>
                                <div class="rental-price">${c.precioAlquiler != null ? '$' + c.precioAlquiler : ''} / 3 días</div>
                                <div class="movie-actions">
                                    <button class="rent-btn" onclick="rentNow(${c.id})">Alquilar</button>
                                    <button class="btn-secondary" onclick="window.location.href='${pageContext.request.contextPath}/contenido/${c.id}'">Ver detalles</button>
                                    <button class="btn-link" onclick="addToListAjax(${c.id}, 'mi-lista')">➕ Mi Lista</button>
                                    <button class="btn-link" onclick="addToListAjax(${c.id}, 'para-ver')">📋 Para Ver</button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p>No se encontraron contenidos para la búsqueda.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </section>
</div>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
<script src="${pageContext.request.contextPath}/js/catalogo.js"></script>
<script src="${pageContext.request.contextPath}/js/alquiler.js"></script>
<script src="${pageContext.request.contextPath}/js/listas.js"></script>
</body>
</html>
