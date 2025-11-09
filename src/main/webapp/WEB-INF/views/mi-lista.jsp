<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Mi Lista - CineArchive</title>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
    <script>window.APP_CTX='${pageContext.request.contextPath}';</script>
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
        </div>
    </nav>
</header>
<div class="container">
    <h1 class="page-title">📚 Mi Biblioteca Personal</h1>
    <section class="category">
        <div class="section-header">
            <h2>🎬 Contenidos en Mi Lista</h2>
        </div>
        <div class="movie-row">
            <c:choose>
                <c:when test="${not empty contenidos}">
                    <c:forEach var="c" items="${contenidos}">
                        <div class="movie-card">
                            <img src="${c.imagenUrl}" alt="${c.titulo}" />
                            <div class="movie-info">
                                <div class="movie-title">${c.titulo}</div>
                                <div class="movie-actions">
                                    <button class="btn-secondary" onclick="window.location.href='/contenido/${c.id}'">Ver detalles</button>
                                    <button class="btn-link" onclick="removeFromListAjax(${c.id}, 'mi-lista')">❌ Quitar</button>
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
</div>
<footer>
    <p>&copy; 2025 CineArchive. Todos los derechos reservados.</p>
</footer>
<script src="<c:url value='/js/listas.js'/>"></script>
</body>
</html>
