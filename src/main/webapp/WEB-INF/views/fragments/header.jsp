<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- Header fragment reutilizable (solo EL básica) --%>
<header>
  <nav>
    <a href="${pageContext.request.contextPath}/catalogo" class="logo">CineArchive</a>
    <button class="menu-toggle">&#9776;</button>
    <div class="nav-links">
      <a href="${pageContext.request.contextPath}/catalogo">Inicio</a>
      <a href="${pageContext.request.contextPath}/mi-lista">Mi Lista</a>
      <a href="${pageContext.request.contextPath}/para-ver">Para Ver</a>
      <a href="${pageContext.request.contextPath}/mis-alquileres">Alquileres</a>
      <c:if test="${not empty sessionScope.usuarioLogueado && sessionScope.usuarioLogueado.rol == 'ADMINISTRADOR'}">
        <a href="${pageContext.request.contextPath}/admin/usuarios" class="admin-link">👥 Panel Admin</a>
      </c:if>
      <c:if test="${not empty sessionScope.usuarioLogueado && sessionScope.usuarioLogueado.rol == 'GESTOR_INVENTARIO'}">
        <a href="${pageContext.request.contextPath}/inventario/panel" class="admin-link">📦 Inventario</a>
      </c:if>
      <c:if test="${not empty sessionScope.usuarioLogueado && sessionScope.usuarioLogueado.rol == 'ANALISTA_DATOS'}">
        <a href="${pageContext.request.contextPath}/reportes/panel" class="admin-link">📊 Reportes</a>
      </c:if>
      <a href="#" class="user-profile" title="Perfil (pendiente Dev 1)">👤 Perfil</a>
      <c:if test="${not empty sessionScope.usuarioLogueado}">
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn" title="Cerrar sesión">🚪 Salir</a>
      </c:if>
    </div>
  </nav>
</header>
