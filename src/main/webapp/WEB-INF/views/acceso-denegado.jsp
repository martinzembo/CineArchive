<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle != null ? pageTitle : 'Acceso Denegado - CineArchive'}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .access-denied-container {
            max-width: 600px;
            margin: 50px auto;
            padding: 40px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .access-denied-icon {
            font-size: 80px;
            color: #dc2626;
            margin-bottom: 20px;
        }

        .access-denied-title {
            color: #dc2626;
            font-size: 32px;
            margin-bottom: 20px;
        }

        .access-denied-message {
            color: #666;
            font-size: 18px;
            line-height: 1.6;
            margin-bottom: 30px;
        }

        .access-denied-actions {
            display: flex;
            gap: 15px;
            justify-content: center;
            flex-wrap: wrap;
        }

        .btn {
            padding: 12px 24px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            transition: all 0.3s;
            border: 2px solid;
        }

        .btn-primary {
            background: #dc2626;
            color: white;
            border-color: #dc2626;
        }

        .btn-primary:hover {
            background: #b91c1c;
            border-color: #b91c1c;
        }

        .btn-secondary {
            background: white;
            color: #333;
            border-color: #ddd;
        }

        .btn-secondary:hover {
            background: #f8f9fa;
            border-color: #999;
        }

        .error-code {
            font-size: 14px;
            color: #999;
            margin-top: 30px;
        }
    </style>
</head>
<body>
    <div class="access-denied-container">
        <div class="access-denied-icon">🚫</div>

        <h1 class="access-denied-title">Acceso Denegado</h1>

        <div class="access-denied-message">
            <c:if test="${not empty mensaje}">
                <p>${mensaje}</p>
            </c:if>
            <c:if test="${empty mensaje}">
                <p>No tienes permisos para acceder a esta sección del sistema.</p>
            </c:if>
        </div>

        <div class="access-denied-actions">
            <c:choose>
                <c:when test="${not empty sessionScope.usuarioLogueado}">
                    <a href="${pageContext.request.contextPath}/index" class="btn btn-primary">
                        Ir al Inicio
                    </a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary">
                        Cerrar Sesión
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">
                        Iniciar Sesión
                    </a>
                    <a href="${pageContext.request.contextPath}/registro" class="btn btn-secondary">
                        Registrarse
                    </a>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="error-code">
            Error 403 - Forbidden Access
        </div>
    </div>
</body>
</html>

