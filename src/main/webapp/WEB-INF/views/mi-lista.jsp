<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <title>Mi Lista - CineArchive</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
    <script>window.APP_CTX='${pageContext.request.contextPath}';</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/fragments/header.jsp" />
<div class="container">
    <h1 class="page-title">📚 Mi Biblioteca Personal</h1>
    <!-- Estadísticas Usuario (derivadas de contenidos si no llegan explícitas) -->
    <c:set var="vistasCount" value="${not empty vistasRecientes ? fn:length(vistasRecientes) : 0}"/>
    <c:set var="favoritasCount" value="${not empty favoritas ? fn:length(favoritas) : 0}"/>
    <c:set var="resenasCount" value="${not empty misResenas ? fn:length(misResenas) : 0}"/>
    <div class="user-stats">
        <div class="stat-item"><span class="stat-number">${vistasCount}</span><span class="stat-label">Vistas</span></div>
        <div class="stat-item"><span class="stat-number">${favoritasCount}</span><span class="stat-label">Favoritas</span></div>
        <div class="stat-item"><span class="stat-number">${resenasCount}</span><span class="stat-label">Reseñadas</span></div>
        <div class="stat-item"><span class="stat-number">
            <c:choose>
                <c:when test="${favoritasCount > 0}"><fmt:formatNumber value="${favoritasCount * 0.9}" minFractionDigits="1" maxFractionDigits="1"/></c:when>
                <c:otherwise>—</c:otherwise>
            </c:choose>
        </span><span class="stat-label">Rating Promedio</span></div>
    </div>
    <!-- Vistas Recientemente -->
    <c:if test="${not empty vistasRecientes}">
        <section class="category">
            <div class="section-header"><h2>✅ Vistas Recientemente</h2></div>
            <div class="movie-row no-select">
                <c:forEach var="v" items="${vistasRecientes}">
                    <div class="movie-card">
                        <c:choose>
                            <c:when test="${empty v.imagenUrl}"><c:url var="vImg" value="/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg"/></c:when>
                            <c:when test="${fn:startsWith(v.imagenUrl,'http')}"><c:set var="vImg" value="${v.imagenUrl}"/></c:when>
                            <c:otherwise><c:url var="vImg" value="${v.imagenUrl}"/></c:otherwise>
                        </c:choose>
                        <img loading="lazy" src="${vImg}" alt="${v.titulo}" draggable="false" onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg';"/>
                        <div class="movie-info">
                            <div class="movie-title">${v.titulo}</div>
                            <div class="movie-rating">Mi rating: ★★★★★</div>
                            <div class="watched-date">Visto: <fmt:formatDate value="${v.fechaVista}" pattern="dd MMM yyyy"/></div>
                            <div class="details-wrapper"><button class="btn-secondary" onclick="window.location.href='${pageContext.request.contextPath}/contenido/${v.id}'">Ver detalles</button></div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>
    </c:if>
    <!-- Favoritas -->
    <c:if test="${not empty favoritas}">
        <section class="category">
            <div class="section-header"><h2>❤️ Mis Favoritas</h2></div>
            <div class="movie-row no-select">
                <c:forEach var="f" items="${favoritas}">
                    <div class="movie-card favorite">
                        <c:choose>
                            <c:when test="${empty f.imagenUrl}"><c:url var="fImg" value="/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg"/></c:when>
                            <c:when test="${fn:startsWith(f.imagenUrl,'http')}"><c:set var="fImg" value="${f.imagenUrl}"/></c:when>
                            <c:otherwise><c:url var="fImg" value="${f.imagenUrl}"/></c:otherwise>
                        </c:choose>
                        <img loading="lazy" src="${fImg}" alt="${f.titulo}" draggable="false"/>
                        <div class="movie-info">
                            <div class="movie-title">${f.titulo}</div>
                            <div class="movie-rating">★★★★★</div>
                            <button class="btn-link" onclick="removeFromListAjax(${f.id}, 'mi-lista', this)">✖ Quitar</button>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>
    </c:if>
    <!-- Contenidos en Mi Lista (existente) -->
    <section class="category">
        <div class="section-header">
            <h2>🎬 Contenidos en Mi Lista</h2>
        </div>
        <div class="movie-row no-select">
            <c:choose>
                <c:when test="${not empty contenidos}">
                    <c:forEach var="c" items="${contenidos}">
                        <div class="movie-card">
                            <c:choose>
                                <c:when test="${empty c.imagenUrl}">
                                    <c:url var="imgSrc" value="/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg"/>
                                </c:when>
                                <c:when test="${fn:startsWith(c.imagenUrl, 'http')}">
                                    <c:set var="imgSrc" value="${c.imagenUrl}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:url var="imgSrc" value="${c.imagenUrl}"/>
                                </c:otherwise>
                            </c:choose>
                            <img loading="lazy" src="${imgSrc}" alt="${c.titulo}" draggable="false" ondragstart="return false;" onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg';" />
                            <div class="movie-info">
                                <div class="movie-title">${c.titulo}</div>
                                <div class="movie-actions">
                                    <div class="details-wrapper"><button class="btn-secondary" onclick="window.location.href='${pageContext.request.contextPath}/contenido/${c.id}'">Ver detalles</button></div>
                                    <button class="btn-link" onclick="removeFromListAjax(${c.id}, 'mi-lista', this)">✖ Quitar</button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p>No hay contenidos aún en tu lista. Agrega desde el catálogo.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </section>
    <!-- Mis Reseñas -->
    <c:if test="${not empty misResenas}">
        <section class="category">
            <div class="section-header"><h2>✍️ Mis Reseñas</h2></div>
            <div class="reviews-grid">
                <c:forEach var="r" items="${misResenas}">
                    <div class="my-review-card">
                        <c:choose>
                            <c:when test="${empty r.imagenUrl}"><c:url var="rImg" value="/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg"/></c:when>
                            <c:when test="${fn:startsWith(r.imagenUrl,'http')}"><c:set var="rImg" value="${r.imagenUrl}"/></c:when>
                            <c:otherwise><c:url var="rImg" value="${r.imagenUrl}"/></c:otherwise>
                        </c:choose>
                        <img src="${rImg}" alt="${r.titulo}" loading="lazy"/>
                        <div class="review-content">
                            <h3>${r.titulo}</h3>
                            <div class="movie-rating">${r.ratingStr != null ? r.ratingStr : '★★★★★'}</div>
                            <p class="review-text">${r.texto}</p>
                            <span class="review-date">Publicado: <fmt:formatDate value="${r.fecha}" pattern="dd MMM yyyy"/></span>
                            <div class="review-actions">
                                <button class="btn-icon">✏️ Editar</button>
                                <button class="btn-icon" onclick="showToast('Eliminado','error')">🗑️ Eliminar</button>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>
    </c:if>
    <!-- Historial de Alquileres -->
    <c:if test="${not empty historialAlquileres}">
        <section class="category">
            <div class="section-header"><h2>📝 Historial de Alquileres</h2></div>
            <div class="table-container">
                <table class="history-table">
                    <thead><tr><th>Título</th><th>Fecha Alquiler</th><th>Periodo</th><th>Precio</th><th>Estado</th></tr></thead>
                    <tbody>
                        <c:forEach var="h" items="${historialAlquileres}">
                            <tr>
                                <td>${h.titulo}</td>
                                <td><fmt:formatDate value="${h.fechaAlquiler}" pattern="dd/MM/yyyy"/></td>
                                <td>${h.periodo} días</td>
                                <td>$<fmt:formatNumber value="${h.precio}" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td><span class="status-badge ${h.estado}">${h.estado}</span></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>
    </c:if>
</div>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
<script src="${pageContext.request.contextPath}/js/listas.js"></script>
</body>
</html>
