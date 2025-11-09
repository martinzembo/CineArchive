<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro - CineArchive</title>
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
        <div class="register-container">
            <h2>Crear Cuenta</h2>
            <form action="/registro" method="post">
                <input type="text" name="nombre" placeholder="Nombre completo" required>
                <input type="email" name="email" placeholder="Correo electrónico" required>
                <input type="password" name="password" placeholder="Contraseña" required>
                <input type="date" name="fechaNacimiento" placeholder="Fecha de nacimiento">
                <button type="submit">Registrarse</button>
            </form>
        </div>
    </div>
    <footer>
        <p>&copy; 2025 CineArchive. Todos los derechos reservados.</p>
    </footer>
</body>
</html>

