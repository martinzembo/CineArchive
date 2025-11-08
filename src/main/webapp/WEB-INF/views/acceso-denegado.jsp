<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Acceso Denegado - CineArchive</title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <!-- Estilos personalizados -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="acceso-denegado">

<div class="error-container">
    <div class="error-card">

        <!-- Icono de Error -->
        <div class="error-icon">
            <i class="fas fa-ban"></i>
        </div>

        <!-- Código de Error -->
        <h1 class="error-code">403</h1>

        <!-- Título -->
        <h2 class="error-title">Acceso Denegado</h2>

        <!-- Descripción -->
        <p class="error-description">
            Lo sentimos, no tienes permisos para acceder a esta página.
            <br>
            Esta sección está restringida a ciertos roles de usuario.
        </p>

        <!-- Información del Usuario (si está logueado) -->
        <c:if test="${not empty usuarioLogueado}">
            <div class="info-box">
                <h5><i class="fas fa-user-circle"></i> Información de tu Cuenta</h5>
                <p class="mb-2">
                    <strong>Nombre:</strong> ${usuarioLogueado.nombre}
                </p>
                <p class="mb-2">
                    <strong>Email:</strong> ${usuarioLogueado.email}
                </p>
                <p class="mb-0">
                    <strong>Tu Rol Actual:</strong>
                    <c:choose>
                        <c:when test="${usuarioLogueado.rol == 'ADMINISTRADOR'}">
                            <span class="rol-badge rol-admin">
                                <i class="fas fa-user-shield"></i> Administrador
                            </span>
                        </c:when>
                        <c:when test="${usuarioLogueado.rol == 'GESTOR_INVENTARIO'}">
                            <span class="rol-badge rol-gestor">
                                <i class="fas fa-warehouse"></i> Gestor de Inventario
                            </span>
                        </c:when>
                        <c:when test="${usuarioLogueado.rol == 'ANALISTA_DATOS'}">
                            <span class="rol-badge rol-analista">
                                <i class="fas fa-chart-line"></i> Analista de Datos
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="rol-badge rol-regular">
                                <i class="fas fa-user"></i> Usuario Regular
                            </span>
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>

            <!-- Información de Rol Requerido (si está disponible) -->
            <c:if test="${not empty rolRequerido}">
                <div class="alert alert-warning mt-3">
                    <i class="fas fa-exclamation-triangle"></i>
                    <strong>Rol Requerido:</strong> ${rolRequerido}
                </div>
            </c:if>
        </c:if>

        <!-- Botones de Acción -->
        <div class="mt-4">
            <a href="${pageContext.request.contextPath}/index" class="btn btn-primary btn-home">
                <i class="fas fa-home"></i> Volver al Inicio
            </a>

            <c:choose>
                <c:when test="${not empty usuarioLogueado}">
                    <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-info btn-home">
                        <i class="fas fa-film"></i> Ver Catálogo
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-success btn-home">
                        <i class="fas fa-sign-in-alt"></i> Iniciar Sesión
                    </a>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Información Adicional -->
        <div class="mt-4">
            <p class="text-muted small">
                <i class="fas fa-info-circle"></i>
                Si crees que deberías tener acceso a esta sección, contacta al administrador del sistema.
            </p>
        </div>

        <!-- Secciones Disponibles según Rol -->
        <c:if test="${not empty usuarioLogueado}">
            <div class="mt-4">
                <hr>
                <h5 class="mb-3">
                    <i class="fas fa-compass"></i> Secciones Disponibles para Ti
                </h5>

                <div class="row text-left">
                    <!-- Para todos los usuarios autenticados -->
                    <div class="col-md-6">
                        <ul class="list-unstyled">
                            <li class="mb-2">
                                <i class="fas fa-check text-success"></i>
                                <a href="${pageContext.request.contextPath}/catalogo">Catálogo de Películas</a>
                            </li>
                            <li class="mb-2">
                                <i class="fas fa-check text-success"></i>
                                <a href="${pageContext.request.contextPath}/mi-lista">Mi Lista</a>
                            </li>
                            <li class="mb-2">
                                <i class="fas fa-check text-success"></i>
                                <a href="${pageContext.request.contextPath}/mis-alquileres">Mis Alquileres</a>
                            </li>
                        </ul>
                    </div>

                    <!-- Según el rol -->
                    <div class="col-md-6">
                        <ul class="list-unstyled">
                            <c:choose>
                                <c:when test="${usuarioLogueado.rol == 'ADMINISTRADOR'}">
                                    <li class="mb-2">
                                        <i class="fas fa-check text-success"></i>
                                        <a href="${pageContext.request.contextPath}/admin/usuarios">Gestión de Usuarios</a>
                                    </li>
                                    <li class="mb-2">
                                        <i class="fas fa-check text-success"></i>
                                        Panel de Administración
                                    </li>
                                </c:when>
                                <c:when test="${usuarioLogueado.rol == 'GESTOR_INVENTARIO'}">
                                    <li class="mb-2">
                                        <i class="fas fa-check text-success"></i>
                                        <a href="${pageContext.request.contextPath}/inventario">Gestión de Inventario</a>
                                    </li>
                                    <li class="mb-2">
                                        <i class="fas fa-check text-success"></i>
                                        Administrar Catálogo
                                    </li>
                                </c:when>
                                <c:when test="${usuarioLogueado.rol == 'ANALISTA_DATOS'}">
                                    <li class="mb-2">
                                        <i class="fas fa-check text-success"></i>
                                        <a href="${pageContext.request.contextPath}/reportes">Reportes y Estadísticas</a>
                                    </li>
                                    <li class="mb-2">
                                        <i class="fas fa-check text-success"></i>
                                        Panel de Analytics
                                    </li>
                                </c:when>
                            </c:choose>
                        </ul>
                    </div>
                </div>
            </div>
        </c:if>

    </div>

    <!-- Footer -->
    <div class="text-center mt-4 text-white">
        <p>
            <i class="fas fa-film"></i> CineArchive &copy; 2025
            | <a href="${pageContext.request.contextPath}/index" class="text-white">Inicio</a>
            | <a href="${pageContext.request.contextPath}/catalogo" class="text-white">Catálogo</a>
        </p>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // Efecto de shake en el icono al cargar
    $(document).ready(function() {
        $('.error-icon').addClass('animated shake');
    });

    // Efecto hover en botones
    $('.btn-home').hover(
        function() {
            $(this).addClass('shadow-lg');
        },
        function() {
            $(this).removeClass('shadow-lg');
        }
    );
</script>

</body>
</html>

