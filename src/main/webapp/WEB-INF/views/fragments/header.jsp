<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- Header fragment reutilizable --%>
<header>
  <nav>
    <a href="${pageContext.request.contextPath}/catalogo" class="logo">CineArchive</a>
    <button class="menu-toggle">&#9776;</button>
    <div class="nav-links">
      <a href="${pageContext.request.contextPath}/catalogo">Inicio</a>
      <a href="${pageContext.request.contextPath}/mi-lista">Mi Lista</a>
      <a href="${pageContext.request.contextPath}/para-ver">Para Ver</a>
      <a href="${pageContext.request.contextPath}/mis-alquileres">Alquileres</a>
      <a href="${pageContext.request.contextPath}/login" class="user-profile">👤 Perfil</a>
      <button class="login-btn" onclick="window.location.href='${pageContext.request.contextPath}/login'">Cerrar sesión</button>
    </div>
  </nav>
</header>

