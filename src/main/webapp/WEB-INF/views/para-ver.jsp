<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Para Ver - CineArchive</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
    <script>window.APP_CTX='${pageContext.request.contextPath}';</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/fragments/header.jsp" />
<div class="container">
    <h1 class="page-title">📋 Mi Lista Para Ver</h1>
    <p class="page-subtitle">Organiza tu próximo contenido a ver</p>
    <!-- Contador (si hay datos) -->
    <c:set var="totalPV" value="${not empty contenidos ? fn:length(contenidos) : 0}"/>
    <div class="watchlist-stats">
        <div class="stat-item"><span class="stat-number">${totalPV}</span><span class="stat-label">Total en lista</span></div>
        <div class="stat-item"><span class="stat-number">—</span><span class="stat-label">Películas</span></div>
        <div class="stat-item"><span class="stat-number">—</span><span class="stat-label">Series</span></div>
    </div>
    <!-- Filtros visuales (sin funcionalidad backend aún) -->
    <div class="filter-bar">
        <select class="filter-select"><option>Todas</option><option>Solo Películas</option><option>Solo Series</option></select>
        <select class="filter-select"><option>Fecha Agregada</option><option>Prioridad</option><option>Título A-Z</option><option>Mejor Valoradas</option></select>
    </div>
    <!-- Lista principal existente -->
    <div class="movie-row no-select">
        <c:choose>
            <c:when test="${not empty contenidos}">
                <c:forEach var="c" items="${contenidos}">
                    <div class="movie-card">
                        <c:choose>
                            <c:when test="${empty c.imagenUrl}"><c:url var="imgSrc" value="/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg"/></c:when>
                            <c:when test="${fn:startsWith(c.imagenUrl,'http')}"><c:set var="imgSrc" value="${c.imagenUrl}"/></c:when>
                            <c:otherwise><c:url var="imgSrc" value="${c.imagenUrl}"/></c:otherwise>
                        </c:choose>
                        <img loading="lazy" src="${imgSrc}" alt="${c.titulo}" draggable="false" ondragstart="return false;" onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg';" />
                        <div class="movie-info">
                            <div class="movie-title">${c.titulo}</div>
                            <div class="movie-rating">★★★★★</div>
                            <div class="rental-price"><c:if test="${not empty c.precioAlquiler}">$<fmt:formatNumber value="${c.precioAlquiler}" minFractionDigits="2" maxFractionDigits="2"/> / 3 días</c:if></div>
                            <div class="action-buttons-vertical">
                                <button class="rent-btn" onclick="window.location.href='${pageContext.request.contextPath}/contenido/${c.id}'">🎬 Alquilar</button>
                                <button class="btn-link" onclick="removeFromListAjax(${c.id}, 'para-ver', this)">✖ Quitar</button>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>No tienes elementos en tu lista “Para Ver”. Agrega desde el catálogo.</p>
            </c:otherwise>
        </c:choose>
    </div>
    <!-- Recomendaciones (placeholder si el backend aún no las provee) -->
    <c:if test="${not empty recomendaciones}">
        <section class="category">
            <div class="section-header"><h2>💡 Recomendaciones para Ti</h2><p class="section-subtitle">Basado en tu lista para ver</p></div>
            <div class="movie-row no-select">
                <c:forEach var="r" items="${recomendaciones}">
                    <div class="movie-card">
                        <c:choose>
                            <c:when test="${empty r.imagenUrl}"><c:url var="rImg" value="/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg"/></c:when>
                            <c:when test="${fn:startsWith(r.imagenUrl,'http')}"><c:set var="rImg" value="${r.imagenUrl}"/></c:when>
                            <c:otherwise><c:url var="rImg" value="${r.imagenUrl}"/></c:otherwise>
                        </c:choose>
                        <img loading="lazy" src="${rImg}" alt="${r.titulo}"/>
                        <div class="movie-info">
                            <div class="movie-title">${r.titulo}</div>
                            <div class="movie-rating">★★★★★</div>
                            <div class="rental-price"><c:if test="${not empty r.precioAlquiler}">$<fmt:formatNumber value="${r.precioAlquiler}" minFractionDigits="2" maxFractionDigits="2"/> / 3 días</c:if></div>
                            <button class="btn-link" onclick="addToListAjax(${r.id}, 'para-ver', this)">➕ Agregar a lista</button>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>
    </c:if>
</div>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
<script src="${pageContext.request.contextPath}/js/listas.js"></script>
</body>
</html>
