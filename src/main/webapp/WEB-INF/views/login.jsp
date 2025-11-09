<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CineArchive - Iniciar Sesión</title>
    <link rel="stylesheet" href="/css/styles.css">
</head>
<body>
    <header>
        <nav>
            <a href="/" class="logo">CineArchive</a>
            <button class="menu-toggle">☰</button>
        </nav>
    </header>
    <div class="container">
        <div class="login-container">
            <h2>Iniciar Sesión</h2>
            <p class="subtitle">Accede a tu cuenta de CineArchive</p>
            <form class="login-form" action="/login" method="post">
                <input type="email" name="email" placeholder="Correo electrónico" required>
                <input type="password" name="password" placeholder="Contraseña" required>

                <div class="checkbox-group">
                    <input type="checkbox" id="remember" name="remember">
                    <label for="remember">Recordarme</label>
                </div>

                <button type="submit">Iniciar Sesión</button>
            </form>
            <div class="login-links">
                <a href="#">¿Olvidaste tu contraseña?</a>
                <br><br>
                <span>¿No tienes una cuenta? </span><a href="/registro">Regístrate</a>
            </div>
        </div>
    </div>
    <footer>
        <p>&copy; 2025 CineArchive. Todos los derechos reservados.</p>
    </footer>
</body>
</html>
