<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CineArchive - Registro</title>
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
            <h2>Crear Cuenta</h2>
            <p class="subtitle">Únete a CineArchive y disfruta del mejor contenido</p>

            <%-- Mensajes de Error --%>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <strong>⚠️ Error:</strong> ${error}
                </div>
            </c:if>

            <%-- Mensajes de Éxito --%>
            <c:if test="${not empty mensaje}">
                <div class="alert alert-success">
                    <strong>✅ Éxito:</strong> ${mensaje}
                </div>
            </c:if>

            <%-- Formulario de Registro --%>
            <form class="login-form" action="${pageContext.request.contextPath}/registro" method="post" id="registroForm">

                <%-- Nombre Completo --%>
                <input type="text"
                       name="nombre"
                       id="nombre"
                       placeholder="Nombre completo"
                       value="${nombre}"
                       required
                       minlength="3"
                       maxlength="100"
                       autofocus>

                <%-- Email --%>
                <input type="email"
                       name="email"
                       id="email"
                       placeholder="Correo electrónico"
                       value="${email}"
                       required
                       maxlength="150">

                <%-- Contraseña --%>
                <input type="password"
                       name="password"
                       id="password"
                       placeholder="Contraseña"
                       required
                       minlength="8"
                       maxlength="100">

                <%-- Requisitos de contraseña (se mostrarán dinámicamente) --%>
                <div class="password-requirements" id="passwordRequirements" style="display: none;">
                    <small>La contraseña debe contener:</small>
                    <ul>
                        <li id="req-length">Mínimo 8 caracteres</li>
                        <li id="req-uppercase">Al menos una mayúscula</li>
                        <li id="req-lowercase">Al menos una minúscula</li>
                        <li id="req-number">Al menos un número</li>
                    </ul>
                </div>

                <%-- Confirmar Contraseña --%>
                <input type="password"
                       name="passwordConfirm"
                       id="passwordConfirm"
                       placeholder="Confirmar contraseña"
                       required
                       minlength="8"
                       maxlength="100">

                <%-- Mensaje de coincidencia de contraseñas --%>
                <div class="password-match" id="passwordMatch" style="display: none;"></div>

                <%-- Fecha de Nacimiento (Opcional) --%>
                <input type="date"
                       name="fechaNacimiento"
                       id="fechaNacimiento"
                       placeholder="Fecha de nacimiento (opcional)"
                       value="${fechaNacimiento}"
                       max="${java.time.LocalDate.now()}">

                <%-- Términos y Condiciones --%>
                <div class="checkbox-group">
                    <input type="checkbox" id="terms" name="terms" required>
                    <label for="terms">
                        Acepto los <a href="${pageContext.request.contextPath}/terminos" target="_blank">Términos y Condiciones</a>
                        y la <a href="${pageContext.request.contextPath}/privacidad" target="_blank">Política de Privacidad</a>
                    </label>
                </div>


                <%-- Botón Submit --%>
                <button type="submit" id="submitBtn">Crear Cuenta</button>
            </form>

            <%-- Link a Login --%>
            <div class="login-links">
                <span>¿Ya tienes una cuenta? </span>
                <a href="${pageContext.request.contextPath}/login">Iniciar Sesión</a>
            </div>
        </div>
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

    <%-- JavaScript para validación en el cliente --%>
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>

