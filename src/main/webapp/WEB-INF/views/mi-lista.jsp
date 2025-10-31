<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Mi Lista - CineArchive</title>
    <link rel="stylesheet" href="/css/styles.css" />
</head>
<body>
<h1>Mis Listas</h1>
<c:forEach var="l" items="${listas}">
    <div class="lista">
        <h3>${l.nombre}</h3>
        <p>${l.descripcion}</p>
    </div>
</c:forEach>
<script src="/js/listas.js"></script>
</body>
</html>

