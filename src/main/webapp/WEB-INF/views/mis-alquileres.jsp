<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
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
            <div class="movie-row">
                <c:forEach var="a" items="${alquileres}">
                    <div class="movie-card">
                        <img src="${a.imagenUrlContenido}" alt="${a.tituloContenido}" />
                        <div class="movie-info">
                            <div class="movie-title">${a.tituloContenido}</div>
                            <p>Inicio: ${a.fechaInicio}</p>
                            <p>Fin: ${a.fechaFin}</p>
                            <p>Estado: ${a.estado}</p>
                            <p>Días restantes: ${a.diasRestantes}</p>
                            <p>Precio: $${a.precio}</p>
                            <button class="btn-secondary" onclick="window.location.href='${pageContext.request.contextPath}/contenido/${a.contenidoId}'">Ver detalles</button>
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
