<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CineArchive - Iniciar Sesión</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        /* Estilos adicionales para mensajes */
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            font-size: 14px;
        }
        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .alert-info {
            background-color: #d1ecf1;
            color: #0c5460;
            border: 1px solid #bee5eb;
        }
    </style>
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

    <%-- JavaScript para validación en el cliente (opcional) --%>
    <script>
        // Validación del formulario en el cliente
        document.querySelector('.login-form').addEventListener('submit', function(e) {
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            if (!email || !password) {
                e.preventDefault();
                alert('Por favor, completa todos los campos');
                return false;
            }

            // Validación básica de email
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email)) {
                e.preventDefault();
                alert('Por favor, ingresa un email válido');
                return false;
            }
        });

        // Auto-ocultar mensajes después de 5 segundos
        setTimeout(function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(alert) {
                alert.style.transition = 'opacity 0.5s';
                alert.style.opacity = '0';
                setTimeout(function() {
                    alert.style.display = 'none';
                }, 500);
            });
        }, 5000);
    </script>
</body>
</html>

