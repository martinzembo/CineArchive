<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <title>Mi Perfil - CineArchive</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .profile-container {
            max-width: 600px;
            margin: 30px auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .profile-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .profile-title {
            color: #dc2626;
            font-size: 28px;
            margin-bottom: 10px;
        }

        .profile-avatar {
            width: 80px;
            height: 80px;
            background: #dc2626;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 20px;
            color: white;
            font-size: 32px;
            font-weight: bold;
        }

        .profile-info {
            background: #f8f9fa;
            padding: 25px;
            border-radius: 8px;
            margin-bottom: 25px;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 12px 0;
            border-bottom: 1px solid #e9ecef;
        }

        .info-row:last-child {
            border-bottom: none;
        }

        .info-label {
            font-weight: bold;
            color: #333;
            flex: 1;
        }

        .info-value {
            color: #666;
            flex: 2;
            text-align: right;
        }

        .status-badge {
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
        }

        .status-active {
            background: #dcfce7;
            color: #166534;
        }

        .status-inactive {
            background: #fee2e2;
            color: #dc2626;
        }

        .role-badge {
            padding: 4px 12px;
            border-radius: 5px;
            font-size: 12px;
            font-weight: bold;
            background: #dc2626;
            color: white;
        }

        .profile-actions {
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

        .no-user-message {
            text-align: center;
            color: #666;
            font-size: 18px;
            margin: 50px 0;
        }
    </style>
</head>
<body>
    <div class="profile-container">
        <c:choose>
            <c:when test="${empty usuario}">
                <div class="no-user-message">
                    <h2>No hay usuario en sesión</h2>
                    <p>Por favor inicia sesión para ver tu perfil.</p>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">
                        Iniciar Sesión
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="profile-header">
                    <div class="profile-avatar">
                        ${usuario.nombre.substring(0, 1).toUpperCase()}
                    </div>
                    <h1 class="profile-title">Mi Perfil</h1>
                </div>

                <div class="profile-info">
                    <div class="info-row">
                        <span class="info-label">ID de Usuario:</span>
                        <span class="info-value">#${usuario.id}</span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Nombre Completo:</span>
                        <span class="info-value">${usuario.nombre}</span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Correo Electrónico:</span>
                        <span class="info-value">${usuario.email}</span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Rol en el Sistema:</span>
                        <span class="info-value">
                            <span class="role-badge">
                                <c:choose>
                                    <c:when test="${usuario.rol == 'ADMINISTRADOR'}">Administrador</c:when>
                                    <c:when test="${usuario.rol == 'GESTOR_INVENTARIO'}">Gestor de Inventario</c:when>
                                    <c:when test="${usuario.rol == 'ANALISTA_DATOS'}">Analista de Datos</c:when>
                                    <c:otherwise>Usuario Regular</c:otherwise>
                                </c:choose>
                            </span>
                        </span>
                    </div>

                    <div class="info-row">
                        <span class="info-label">Estado de la Cuenta:</span>
                        <span class="info-value">
                            <c:choose>
                                <c:when test="${usuario.activo}">
                                    <span class="status-badge status-active">Activa</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-badge status-inactive">Inactiva</span>
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                </div>

                <div class="profile-actions">
                    <a href="${pageContext.request.contextPath}/index" class="btn btn-primary">
                        Ir al Inicio
                    </a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary">
                        Cerrar Sesión
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
