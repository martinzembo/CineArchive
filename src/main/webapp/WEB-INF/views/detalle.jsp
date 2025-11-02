<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Detalle - CineArchive</title>
    <link rel="stylesheet" href="/css/styles.css" />
</head>
<body>
<c:if test="${not empty contenido}">
    <h1>${contenido.titulo}</h1>
    <img src="${contenido.imagenUrl}" alt="${contenido.titulo}" />
    <p>${contenido.descripcion}</p>
    <p>Género: ${contenido.genero} | Año: ${contenido.anio}</p>
    <form action="/alquilar" method="post">
        <input type="hidden" name="contenidoId" value="${contenido.id}" />
        <label>Periodo (días): <input type="number" name="periodo" value="3" /></label>
        <button type="submit">Alquilar</button>
    </form>
</c:if>
<script src="/js/alquiler.js"></script>
</body>
</html>

