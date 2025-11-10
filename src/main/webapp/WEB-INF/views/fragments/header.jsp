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
      <a href="#" class="user-profile" title="Perfil (pendiente Dev 1)">👤 Perfil</a>
    </div>
  </nav>
</header>
