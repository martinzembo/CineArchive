<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CineArchive - Iniciar Sesión</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <header>
        <nav>
            <a href="${pageContext.request.contextPath}/index" class="logo">CineArchive</a>
            <button class="menu-toggle">☰</button>
        </nav>
    </header>

    <div class="container">
        <div class="login-container">
            <h2>Iniciar Sesión</h2>
            <p class="subtitle">Accede a tu cuenta de CineArchive</p>

            <%-- Mensajes de Error --%>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <strong>⚠️ Error:</strong> ${error}
                </div>
            </c:if>

            <%-- Mensajes Específicos según parámetros URL --%>
            <c:if test="${param.mensaje == 'logout'}">
                <div class="alert alert-info">
                    <strong>👋 Sesión cerrada:</strong> Has cerrado sesión exitosamente.
                </div>
            </c:if>

            <%-- Otros mensajes de éxito --%>
            <c:if test="${not empty mensaje}">
                <div class="alert alert-success">
                    <strong>✅ Éxito:</strong> ${mensaje}
                </div>
            </c:if>

            <c:if test="${param.mensaje == 'registroExitoso'}">
                <div class="alert alert-success">
                    <strong>🎉 ¡Registro exitoso!</strong> Ya puedes iniciar sesión con tus credenciales.
                </div>
            </c:if>

            <c:if test="${param.error == 'sesionExpirada'}">
                <div class="alert alert-danger">
                    <strong>⏰ Sesión expirada:</strong> Tu sesión ha expirado. Por favor, inicia sesión nuevamente.
                </div>
            </c:if>

            <%-- Formulario de Login --%>
            <form class="login-form" action="${pageContext.request.contextPath}/login" method="post">
                <input type="email"
                       name="email"
                       id="email"
                       placeholder="Correo electrónico"
                       value="${email}"
                       required
                       autofocus>

                <input type="password"
                       name="password"
                       id="password"
                       placeholder="Contraseña"
                       required>


                <button type="submit">Iniciar Sesión</button>
            </form>

            <div class="login-links">
                <a href="${pageContext.request.contextPath}/recuperar-password">¿Olvidaste tu contraseña?</a>
                <br><br>
                <span>¿No tienes una cuenta? </span>
                <a href="${pageContext.request.contextPath}/registro">Regístrate</a>
            </div>
        </div>

        <section class="movie-section">
            <h2>🆕 Novedades</h2>
            <div class="movie-row">
                <div class="movie-card">
                    <img src="${pageContext.request.contextPath}/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg" alt="Película 1">
                    <div class="movie-info">
                        <div class="movie-title">Oppenheimer</div>
                        <div class="movie-rating">★★★★★</div>
                    </div>
                </div>
                <div class="movie-card">
                    <img src="${pageContext.request.contextPath}/img/MV5BOGVkODYxMDEtODczZC00MjRiLTg3ZWYtZjgzN2QyMDBjZTUzXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg" alt="Serie 1">
                    <div class="movie-info">
                        <div class="movie-title">Breaking Bad</div>
                        <div class="movie-rating">★★★★★</div>
                    </div>
                </div>
                <div class="movie-card">
                    <img src="${pageContext.request.contextPath}/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg" alt="Película 2">
                    <div class="movie-info">
                        <div class="movie-title">Interstellar</div>
                        <div class="movie-rating">★★★★★</div>
                    </div>
                </div>
                <div class="movie-card">
                    <img src="${pageContext.request.contextPath}/img/MV5BOGVkODYxMDEtODczZC00MjRiLTg3ZWYtZjgzN2QyMDBjZTUzXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg" alt="Serie 2">
                    <div class="movie-info">
                        <div class="movie-title">Stranger Things</div>
                        <div class="movie-rating">★★★★☆</div>
                    </div>
                </div>
                <div class="movie-card">
                    <img src="${pageContext.request.contextPath}/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg" alt="Película 3">
                    <div class="movie-info">
                        <div class="movie-title">Dune: Part Two</div>
                        <div class="movie-rating">★★★★★</div>
                    </div>
                </div>
            </div>
        </section>

        <section class="movie-section">
            <h2>🔥 Más Populares</h2>
            <div class="movie-row">
                <div class="movie-card">
                    <img src="${pageContext.request.contextPath}/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg" alt="Película 4">
                    <div class="movie-info">
                        <div class="movie-title">The Dark Knight</div>
                        <div class="movie-rating">★★★★★</div>
                    </div>
                </div>
                <div class="movie-card">
                    <img src="${pageContext.request.contextPath}/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg" alt="Serie 3">
                    <div class="movie-info">
                        <div class="movie-title">The Last of Us</div>
                        <div class="movie-rating">★★★★★</div>
                    </div>
                </div>
                <div class="movie-card">
                    <img src="${pageContext.request.contextPath}/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg" alt="Película 5">
                    <div class="movie-info">
                        <div class="movie-title">Inception</div>
                        <div class="movie-rating">★★★★★</div>
                    </div>
                </div>
                <div class="movie-card">
                    <img src="${pageContext.request.contextPath}/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg" alt="Serie 4">
                    <div class="movie-info">
                        <div class="movie-title">Wednesday</div>
                        <div class="movie-rating">★★★★☆</div>
                    </div>
                </div>
                <div class="movie-card">
                    <img src="${pageContext.request.contextPath}/img/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_FMjpg_UX1000_.jpg" alt="Película 6">
                    <div class="movie-info">
                        <div class="movie-title">Barbie</div>
                        <div class="movie-rating">★★★★☆</div>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <footer>
        <p>&copy; 2025 CineArchive. Todos los derechos reservados.</p>
        <div class="footer-links">
            <a href="${pageContext.request.contextPath}/acerca">Acerca de</a>
            <a href="${pageContext.request.contextPath}/privacidad">Privacidad</a>
            <a href="${pageContext.request.contextPath}/terminos">Términos de uso</a>
            <a href="${pageContext.request.contextPath}/contacto">Contacto</a>
        </div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>

