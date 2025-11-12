<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <title>Mis Alquileres - CineArchive</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
    <script>window.APP_CTX='${pageContext.request.contextPath}';</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/fragments/header.jsp" />
<div class="container">
    <h1 class="page-title">🎫 Mis Alquileres</h1>
    <c:choose>
        <c:when test="${not empty alquileres}">
            <div class="movie-row no-select">
                <c:forEach var="a" items="${alquileres}">
                    <div class="movie-card" id="cont-${a.contenidoId}">
                        <c:choose>
                            <c:when test="${empty a.imagenUrlContenido}">
                                <c:url var="imgSrc" value="/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg"/>
                            </c:when>
                            <c:when test="${fn:startsWith(a.imagenUrlContenido, 'http')}">
                                <c:set var="imgSrc" value="${a.imagenUrlContenido}"/>
                            </c:when>
                            <c:otherwise>
                                <c:url var="imgSrc" value="${a.imagenUrlContenido}"/>
                            </c:otherwise>
                        </c:choose>
                        <img src="${imgSrc}" alt="${a.tituloContenido}" draggable="false" onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg';" />
                        <div class="movie-info">
                            <div class="movie-title">${a.tituloContenido}</div>
                            <div class="alquiler-meta">
                                <span class="badge-estado ${a.estado == 'ACTIVO' ? 'estado-activo' : (a.estado == 'FINALIZADO' ? 'estado-finalizado' : 'estado-cancelado')}">${a.estado}</span>
                                <c:if test="${a.diasRestantes lt 0}"><span class="badge-expirado">Vencido</span></c:if>
                            </div>
                            <div class="rental-time">
                                <c:if test="${a.periodoAlquiler != null}">Periodo: ${a.periodoAlquiler} días</c:if>
                                <c:choose>
                                    <c:when test="${a.diasRestantes != null && a.diasRestantes ge 0}"> • Restan ${a.diasRestantes} días</c:when>
                                    <c:otherwise> • Restan 0 días</c:otherwise>
                                </c:choose>
                            </div>
                            <div class="progress-wrapper">
                                <div class="progress-bar"><div class="progress-fill" style="width:${a.progresoPct}%;"></div></div>
                                <div class="progress-text">${a.progresoPct}% transcurrido</div>
                            </div>
                            <div class="movie-actions">
                                <div class="details-wrapper">
                                    <button class="btn-secondary details-btn" onclick="window.location.href='${pageContext.request.contextPath}/contenido/${a.contenidoId}'">Ver detalles</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <p>No tienes alquileres activos.</p>
        </c:otherwise>
    </c:choose>
</div>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
<script src="${pageContext.request.contextPath}/js/alquiler.js"></script>
</body>
</html>
