<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Mis Alquileres - CineArchive</title>
    <link rel="stylesheet" href="/css/styles.css" />
</head>
<body>
<h1>Mis Alquileres</h1>
<c:forEach var="a" items="${alquileres}">
    <div class="alquiler">
        <h3>Contenido ID: ${a.contenidoId}</h3>
        <p>Inicio: ${a.fechaInicio} - Fin: ${a.fechaFin}</p>
        <p>Estado: ${a.estado}</p>
    </div>
</c:forEach>
</body>
</html>

