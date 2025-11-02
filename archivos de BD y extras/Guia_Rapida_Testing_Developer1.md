# 🧪 GUÍA RÁPIDA DE TESTING - Developer 1
## Checklist para probar el sistema de autenticación y usuarios

---

## 🔐 CREDENCIALES DE ACCESO

### 📋 Usuarios de Prueba Disponibles

**Las contraseñas ya están configuradas en la BD. Solo úsalas para iniciar sesión:**

| Email | Contraseña | Rol |
|-------|-----------|-----|
| `admin@cinearchive.com` | `Admin123` | ADMINISTRADOR |
| `gestor@cinearchive.com` | `Gestor123` | GESTOR_INVENTARIO |
| `analista@cinearchive.com` | `Analista123` | ANALISTA_DATOS |
| `maria@example.com` | `User123` | USUARIO_REGULAR |
| `juan@example.com` | `User123` | USUARIO_REGULAR |

---

## 🚨 SOLUCIÓN DE PROBLEMAS

### ❓ Si las contraseñas no funcionan

Las contraseñas están encriptadas con BCrypt. Si no puedes iniciar sesión:

1. **Verifica que la BD tenga los datos correctos:**
   - Ejecuta el script `cineArchiveBD.sql` para recrear la base de datos
   - Los hashes ya están incluidos en el script SQL

2. **Prueba las herramientas de testing:**
   ```
   http://localhost:8080/cinearchive/test/password/verificar?password=Admin123&hash=$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5koQKeWcrmyS6
   ```

3. **Genera un nuevo hash si es necesario:**
   ```
   http://localhost:8080/cinearchive/test/password/encriptar?password=TuNuevaPassword123
   ```

### ✅ Verificar nombre de base de datos

En `application.properties`, verifica:
```properties
db.url=jdbc:mysql://localhost:3306/cinearchive_v2?useSSL=false&serverTimezone=UTC
```

Si tu BD se llama solo `cinearchive`, cambia a:
```properties
db.url=jdbc:mysql://localhost:3306/cinearchive?useSSL=false&serverTimezone=UTC
```

---

## 📋 PASO 1: TESTS MANUALES RÁPIDOS (15 minutos)

### ✅ Test 1: Registro de Usuario Nuevo

1. **Iniciar servidor:**
   ```bash
   cd C:\Users\Francisco\Desktop\CineArchive
   mvn jetty:run
   ```

2. **Abrir navegador:**
   ```
   http://localhost:8080/cinearchive/registro
   ```

3. **Llenar formulario:**
   - Nombre: `Test Usuario`
   - Email: `test@test.com`
   - Password: `Test123456`
   - Confirmar Password: `Test123456`
   
4. **Verificar:**
   - ✅ Redirección a `/login`
   - ✅ Mensaje: "¡Registro exitoso! Ya puedes iniciar sesión"

5. **Verificar en BD:**
   ```sql
   SELECT * FROM usuarios WHERE email = 'test@test.com';
   ```
   - ✅ Usuario existe
   - ✅ Contraseña NO es texto plano (empieza con `$2a$`)

---

### ✅ Test 2: Login con Usuario Regular

1. **Ir a:**
   ```
   http://localhost:8080/cinearchive/login
   ```

2. **Credenciales:**
   - Email: `test@test.com` (el que acabas de crear)
   - Password: `Test123456`

3. **Verificar:**
   - ✅ Login exitoso
   - ✅ Redirección a `/catalogo` (o `/index` si no existe catalogo)
   - ✅ Sesión creada (ver cookies en DevTools)

---

### ✅ Test 3: Login como Administrador

1. **Credenciales:**
   - Email: `admin@cinearchive.com`
   - Password: `Admin123` (o la que hayas configurado)

2. **Verificar:**
   - ✅ Login exitoso
   - ✅ Redirección a `/admin/panel`
   - ✅ Rol = ADMINISTRADOR en sesión

---

### ✅ Test 4: Validaciones de Registro

**Test 4.1: Email duplicado**
- Intentar registrar con `test@test.com` (ya existe)
- ✅ Debe mostrar error: "El email ya está registrado"

**Test 4.2: Contraseña débil**
- Password: `pass` (muy corta)
- ✅ Debe mostrar error: "La contraseña debe tener al menos 8 caracteres..."

**Test 4.3: Contraseñas no coinciden**
- Password: `Test123456`
- Confirmar: `Test999999`
- ✅ Debe mostrar error: "Las contraseñas no coinciden"

---

### ✅ Test 5: Control de Acceso (SecurityInterceptor)

**Test 5.1: Acceso sin autenticación**
1. Abrir navegador en modo **incógnito**
2. Ir a: `http://localhost:8080/cinearchive/perfil`
3. ✅ Debe redirigir a `/login`

**Test 5.2: Usuario Regular intenta acceder a Admin**
1. Login como `test@test.com` (Usuario Regular)
2. Ir a: `http://localhost:8080/cinearchive/admin/panel`
3. ✅ Debe redirigir a `/acceso-denegado`

**Test 5.3: Admin accede a todo**
1. Login como `admin@cinearchive.com` (Administrador)
2. Probar acceder a:
   - `/admin/panel` → ✅ OK
   - `/inventario/panel` → ✅ OK
   - `/reportes/panel` → ✅ OK

---

### ✅ Test 6: Logout

1. Hacer login con cualquier usuario
2. Ir a: `http://localhost:8080/cinearchive/logout`
3. **Verificar:**
   - ✅ Redirección a `/login`
   - ✅ Mensaje: "Has cerrado sesión exitosamente"
   - ✅ Intentar ir a `/perfil` → debe redirigir a `/login`

---

### ✅ Test 7: Login con Credenciales Incorrectas

**Test 7.1: Email no existe**
- Email: `noexiste@test.com`
- Password: `cualquiera`
- ✅ Error: "Email o contraseña incorrectos..."

**Test 7.2: Password incorrecta**
- Email: `test@test.com`
- Password: `WrongPassword123`
- ✅ Error: "Email o contraseña incorrectos..."

**Test 7.3: Usuario inactivo**
1. Desactivar usuario en BD:
   ```sql
   UPDATE usuarios SET activo = 0 WHERE email = 'test@test.com';
   ```
2. Intentar login
3. ✅ Error: "cuenta desactivada"
4. Reactivar:
   ```sql
   UPDATE usuarios SET activo = 1 WHERE email = 'test@test.com';
   ```

---

## 🧪 PASO 2: TESTS UNITARIOS (Maven)

### Ejecutar todos los tests

```bash
cd C:\Users\Francisco\Desktop\CineArchive
mvn test
```

### Ejecutar un test específico

```bash
# Test de PasswordUtil
mvn test -Dtest=PasswordUtilTest

# Test de UsuarioService
mvn test -Dtest=UsuarioServiceTest

# Test de UsuarioRepository
mvn test -Dtest=UsuarioRepositoryTest
```

---

## 🔍 PASO 3: VERIFICACIONES EN BASE DE DATOS

### ✅ Verificar estructura de tabla

```sql
DESCRIBE usuarios;
```

**Debe tener:**
- id (bigint, PRIMARY KEY, AUTO_INCREMENT)
- nombre (varchar 255)
- email (varchar 255, UNIQUE)
- contrasena (varchar 255)
- rol (ENUM)
- fecha_registro (timestamp)
- activo (tinyint)
- fecha_nacimiento (date, nullable)

### ✅ Verificar índices

```sql
SHOW INDEX FROM usuarios;
```

**Debe tener índices en:**
- id (PRIMARY)
- email (UNIQUE)
- idx_usuario_email
- idx_usuario_rol

### ✅ Verificar constraint de email único

```sql
-- Esto debe FALLAR:
INSERT INTO usuarios (nombre, email, contrasena, rol) 
VALUES ('Duplicado', 'admin@cinearchive.com', 'hash', 'USUARIO_REGULAR');
-- Error: Duplicate entry for key 'email'
```

### ✅ Verificar ENUM de roles

```sql
-- Esto debe FALLAR:
INSERT INTO usuarios (nombre, email, contrasena, rol) 
VALUES ('Test', 'testenumrol@test.com', 'hash', 'ROL_INVALIDO');
-- Error: Invalid ENUM value
```

---

## 🔐 PASO 4: TESTS DE SEGURIDAD

### ✅ Test 1: Contraseñas encriptadas

```sql
SELECT id, email, contrasena FROM usuarios LIMIT 5;
```

**Verificar:**
- ✅ Las contraseñas NO son texto plano
- ✅ Empiezan con `$2a$` (BCrypt)
- ✅ Tienen 60 caracteres de longitud

### ✅ Test 2: Inyección SQL en login

**Intentar login con:**
- Email: `admin' OR '1'='1`
- Password: `cualquiera`

**Verificar:**
- ✅ NO funciona (JdbcTemplate protege contra esto)

### ✅ Test 3: XSS en nombre

1. Registrar usuario con nombre:
   ```
   <script>alert('XSS')</script>Test
   ```

2. Ver el perfil

3. **Verificar:**
   - ✅ El script NO se ejecuta
   - ✅ JSTL escapa el HTML automáticamente

---

## 📊 CHECKLIST FINAL

### Backend - Configuración
- [ ] AppConfig.java existe y funciona
- [ ] DatabaseConfig.java conecta a BD correctamente
- [ ] WebAppInitializer.java inicializa DispatcherServlet
- [ ] WebMvcConfig.java configura ViewResolver
- [ ] SecurityInterceptor está registrado

### Backend - Lógica
- [ ] PasswordUtil encripta con BCrypt
- [ ] UsuarioRepository hace CRUD en BD
- [ ] UsuarioService valida y procesa lógica de negocio
- [ ] Usuario.java tiene validaciones Bean Validation

### Frontend - Controllers
- [ ] LoginController procesa login/logout
- [ ] RegistroController procesa registro
- [ ] Sesiones HTTP funcionan correctamente
- [ ] Redirección por rol funciona

### Frontend - Vistas
- [ ] login.jsp se muestra correctamente
- [ ] registro.jsp se muestra correctamente
- [ ] Mensajes de error/éxito funcionan
- [ ] Estilos CSS se cargan

### Base de Datos
- [ ] Tabla usuarios existe con estructura correcta
- [ ] Constraint UNIQUE en email funciona
- [ ] ENUM de roles funciona
- [ ] Índices están creados
- [ ] Datos de prueba insertados
- [ ] **Contraseñas son hashes BCrypt válidos** ⚠️

### Seguridad
- [ ] Contraseñas encriptadas en BD
- [ ] SecurityInterceptor bloquea accesos no autorizados
- [ ] Control por roles funciona
- [ ] No hay inyección SQL
- [ ] No hay XSS

---

## 🐛 PROBLEMAS COMUNES Y SOLUCIONES

### ❌ Error: "Cannot login with test users"
**Causa:** Hashes de contraseña inválidos en BD  
**Solución:** Ejecutar el generador de hashes (ver PASO 0)

### ❌ Error: "Unknown database 'cinearchive'"
**Causa:** Nombre de BD incorrecto en application.properties  
**Solución:** Cambiar a `cinearchive_v2` o crear BD con nombre `cinearchive`

### ❌ Error: "404 Not Found" en recursos CSS
**Causa:** Servidor no encuentra los archivos estáticos  
**Solución:** Verificar que `/css/styles.css` existe en `src/main/webapp/css/`

### ❌ Error: "Bean not found" al iniciar
**Causa:** ComponentScan no encuentra las clases  
**Solución:** Verificar que los paquetes en `@ComponentScan` sean correctos

### ❌ Error: Redirección infinita en login
**Causa:** Usuario ya logueado intenta ir a /login  
**Solución:** Es normal, el código redirige a /index (funcionando correctamente)

---

## ✅ CRITERIOS DE APROBACIÓN

El trabajo del Developer 1 está **APROBADO** si:

1. ✅ Puedes registrar un nuevo usuario
2. ✅ Puedes hacer login con ese usuario
3. ✅ Puedes hacer login con usuarios de prueba de la BD
4. ✅ SecurityInterceptor bloquea accesos no autorizados
5. ✅ Logout funciona correctamente
6. ✅ Contraseñas están encriptadas en BD
7. ✅ Validaciones de email y password funcionan
8. ✅ Control de acceso por roles funciona

**Si TODOS estos tests pasan → Developer 1 completó su trabajo exitosamente. ✅**

---

## 📞 CONTACTO

Si encuentras algún problema durante el testing, documenta:
1. URL donde ocurre
2. Datos ingresados
3. Error mostrado (captura de pantalla)
4. Logs del servidor
5. Query ejecutado (si aplica)

---

**Última actualización:** 2025-11-01  
**Versión:** 1.0  
**Proyecto:** CineArchive V2

