# 📊 Análisis de UsuarioRepository y Tareas Developer 1

## ✅ Estado de Usuario.java

La clase `Usuario.java` está **CORRECTAMENTE IMPLEMENTADA** y **COMPLETA**. Incluye:

### ✔️ Elementos Implementados:
1. ✅ **Validaciones con Bean Validation**:
   - `@NotNull` en campos obligatorios
   - `@Email` para validar formato de correo
   - `@Size` para validar longitudes
   - `@Past` para validar fecha de nacimiento
   
2. ✅ **Enum Rol** con los 4 tipos de usuario:
   - USUARIO_REGULAR
   - ADMINISTRADOR
   - GESTOR_INVENTARIO
   - ANALISTA_DATOS

3. ✅ **Todos los campos necesarios**:
   - id, nombre, email, contrasena, rol, fechaRegistro, activo, fechaNacimiento

4. ✅ **Constructores**:
   - Constructor vacío para frameworks
   - Constructor con parámetros que establece valores por defecto

5. ✅ **Getters y Setters completos**

6. ✅ **Métodos útiles**:
   - `tieneRol(Rol rolEsperado)` - Verifica si tiene un rol específico
   - `estaActivo()` - Verifica si el usuario está activo
   - `getEdad()` - Calcula edad desde fecha de nacimiento

7. ✅ **Métodos Object**:
   - `toString()` - Representación legible
   - `equals()` y `hashCode()` basados en id

### 🎯 Conclusión Usuario.java:
**NO REQUIERE MODIFICACIONES**. Está lista para producción.

---

## 🔍 ¿Qué es UsuarioRepository?

### Concepto:
`UsuarioRepository` es la **capa de acceso a datos** (DAO - Data Access Object) para la entidad Usuario. Es responsable de:

1. **CRUD básico**:
   - Crear (INSERT)
   - Leer (SELECT)
   - Actualizar (UPDATE)
   - Eliminar (DELETE)

2. **Consultas específicas**:
   - Buscar por email (para login)
   - Buscar por rol
   - Listar usuarios activos/inactivos
   - Contar usuarios por criterio

3. **Gestión de conexiones**:
   - Usa `JdbcTemplate` (inyectado por Spring)
   - Maneja excepciones de BD
   - Mapea ResultSet a objetos Usuario

### Arquitectura:
```
Controller → Service → Repository → Base de Datos
                ↑
            @Autowired
```

---

## 🛠️ Implementación de UsuarioRepository

### Estructura del Repository:

```
@Repository
public class UsuarioRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // RowMapper para convertir ResultSet → Usuario
    private static final RowMapper<Usuario> USUARIO_ROW_MAPPER = ...
    
    // CRUD básico
    public Usuario crear(Usuario usuario) { ... }
    public Usuario buscarPorId(int id) { ... }
    public List<Usuario> listarTodos() { ... }
    public boolean actualizar(Usuario usuario) { ... }
    public boolean eliminar(int id) { ... }
    
    // Consultas específicas
    public Usuario buscarPorEmail(String email) { ... }
    public List<Usuario> buscarPorRol(Usuario.Rol rol) { ... }
    public List<Usuario> listarActivos() { ... }
    public boolean existeEmail(String email) { ... }
    public int contarPorRol(Usuario.Rol rol) { ... }
}
```

### Métodos Esenciales:

#### 1. **crear(Usuario usuario)**
- Inserta un nuevo usuario en la BD
- Retorna el usuario con su ID generado
- SQL: `INSERT INTO usuarios (...) VALUES (?)`

#### 2. **buscarPorId(int id)**
- Busca un usuario por su ID
- Retorna el usuario o null si no existe
- SQL: `SELECT * FROM usuarios WHERE id = ?`

#### 3. **buscarPorEmail(String email)**
- **CRÍTICO para login**
- Busca usuario por email único
- SQL: `SELECT * FROM usuarios WHERE email = ?`

#### 4. **actualizar(Usuario usuario)**
- Actualiza datos de un usuario existente
- SQL: `UPDATE usuarios SET ... WHERE id = ?`

#### 5. **listarPorRol(Usuario.Rol rol)**
- Lista usuarios con un rol específico
- Útil para administración
- SQL: `SELECT * FROM usuarios WHERE rol = ?`

---

## 📋 Tareas del Developer 1 - Paso 2 (Semana 2)

### 🔴 PRIORIDAD CRÍTICA - Bloquea a Dev 2 y Dev 3

Según el Plan de Delegación, estás en **Días 9-10 (Lunes-Martes)**:

### Tareas Asignadas:

#### 1. ✅ **Usuario.java** (COMPLETADO)
   - Ya está completo y correcto
   - No requiere modificaciones

#### 2. 🔴 **UsuarioRepository.java** (EN PROGRESO)
   - **Estado**: Archivo creado pero vacío
   - **Qué hacer**: Implementar TODOS los métodos
   - **Tiempo estimado**: 2-3 horas
   - **Prioridad**: MÁXIMA (bloquea todo el backend)

#### 3. 🟡 **UsuarioService.java** (SIGUIENTE)
   - **Estado**: No creado
   - **Qué hacer**: Implementar lógica de negocio
   - **Depende de**: UsuarioRepository terminado
   - **Métodos clave**:
     - `registrarUsuario()` - Validación + hasheo + guardar
     - `autenticar()` - Login con verificación de contraseña
     - `actualizarPerfil()` - Edición de datos
     - `cambiarEstado()` - Activar/desactivar usuarios
     - `listarPorRol()` - Filtrado de usuarios

#### 4. 🟡 **LoginController.java** (SIGUIENTE)
   - **Estado**: No creado
   - **Qué hacer**: Controlador para login y registro
   - **Depende de**: UsuarioService terminado
   - **Endpoints**:
     - `GET /login` - Muestra formulario
     - `POST /login` - Procesa autenticación
     - `GET /registro` - Muestra formulario
     - `POST /registro` - Crea nuevo usuario
     - `GET /logout` - Cierra sesión

---

## 🎯 Orden de Implementación Recomendado

### Hoy (Día actual):

1. **Implementar UsuarioRepository** (2-3 horas)
   - Todos los métodos CRUD
   - Métodos de búsqueda específicos
   - Testing con consultas directas

2. **Crear UsuarioService** (2-3 horas)
   - Lógica de negocio
   - Validaciones
   - Hasheo de contraseñas (BCrypt)

3. **Implementar LoginController** (2-3 horas)
   - Endpoints de login/registro
   - Gestión de sesiones
   - Redirecciones

4. **Testing de integración** (1 hora)
   - Probar flujo completo
   - Login → Dashboard
   - Registro → Confirmación

---

## 🚨 Bloqueos que Resuelves

Al completar el **sistema de usuarios**, desbloqueas:

### Para Developer 2 (Contenido):
- ✅ Puede implementar `gestorInventarioId` en Contenido
- ✅ Puede validar roles al agregar contenido
- ✅ Puede mostrar "Agregado por: [nombre gestor]"

### Para Developer 3 (Alquileres):
- ✅ Puede implementar Alquiler con `usuarioId`
- ✅ Puede validar usuarios activos al alquilar
- ✅ Puede mostrar historial por usuario

### Para ti mismo:
- ✅ Puedes implementar reportes demográficos (requieren usuarios)
- ✅ Puedes implementar gestión de permisos

---

## 📊 Estructura de la Tabla `usuarios` en BD

```sql
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,  -- Hash BCrypt
    rol ENUM('USUARIO_REGULAR', 'ADMINISTRADOR', 'GESTOR_INVENTARIO', 'ANALISTA_DATOS') NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    fecha_nacimiento DATE,
    INDEX idx_email (email),
    INDEX idx_rol (rol)
);
```

---

## 💡 Buenas Prácticas en UsuarioRepository

### 1. **Manejo de excepciones**:
```java
try {
    return jdbcTemplate.queryForObject(sql, USUARIO_ROW_MAPPER, id);
} catch (EmptyResultDataAccessException e) {
    return null;  // No existe
}
```

### 2. **RowMapper reutilizable**:
```java
private static final RowMapper<Usuario> USUARIO_ROW_MAPPER = (rs, rowNum) -> {
    Usuario usuario = new Usuario();
    usuario.setId(rs.getInt("id"));
    usuario.setNombre(rs.getString("nombre"));
    // ... mapear todos los campos
    return usuario;
};
```

### 3. **SQL con PreparedStatements**:
```java
String sql = "INSERT INTO usuarios (nombre, email, contrasena, rol) VALUES (?, ?, ?, ?)";
jdbcTemplate.update(sql, usuario.getNombre(), usuario.getEmail(), ...);
```

### 4. **Recuperar ID generado**:
```java
KeyHolder keyHolder = new GeneratedKeyHolder();
jdbcTemplate.update(connection -> {
    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    // ... setear parámetros
    return ps;
}, keyHolder);
return keyHolder.getKey().intValue();
```

---

## ✅ Checklist de Implementación

- [ ] Implementar UsuarioRepository con todos los métodos
- [ ] Probar conexión a BD y consultas básicas
- [ ] Implementar UsuarioService con lógica de negocio
- [ ] Configurar BCrypt para hasheo de contraseñas
- [ ] Implementar LoginController con endpoints
- [ ] Crear vistas JSP para login y registro
- [ ] Configurar gestión de sesiones HTTP
- [ ] Testing de flujo completo
- [ ] Documentar código y APIs

---

## 🎯 Siguiente Paso Inmediato

**IMPLEMENTAR UsuarioRepository.java**

Métodos mínimos requeridos:
1. `crear(Usuario usuario)` → INSERT
2. `buscarPorId(int id)` → SELECT por ID
3. `buscarPorEmail(String email)` → SELECT por email (login)
4. `actualizar(Usuario usuario)` → UPDATE
5. `listarTodos()` → SELECT all
6. `listarActivos()` → SELECT con activo = true
7. `buscarPorRol(Rol rol)` → SELECT por rol
8. `existeEmail(String email)` → COUNT para validación
9. `eliminar(int id)` → DELETE (soft delete cambiando activo a false)

**Tiempo estimado**: 2-3 horas
**Bloquea**: TODO el resto del proyecto
**Prioridad**: MÁXIMA 🔴

