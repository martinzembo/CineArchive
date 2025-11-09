<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Mis Alquileres - CineArchive</title>
    <link rel="stylesheet" href="/css/styles.css" />
</head>
<body>
<header>
    <nav>
        <a href="/" class="logo">CineArchive</a>
        <button class="menu-toggle">☰</button>
        <div class="nav-links">
            <a href="/catalogo">Catálogo</a>
            <a href="/mi-lista">Mi Lista</a>
            <a href="/para-ver">Para Ver</a>
        </div>
    </nav>
</header>
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
                            <button class="btn-secondary" onclick="window.location.href='/contenido/${a.contenidoId}'">Ver detalles</button>
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
<footer>
    <p>&copy; 2025 CineArchive. Todos los derechos reservados.</p>
</footer>
</body>
</html>
