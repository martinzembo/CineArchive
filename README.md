# 🎥 CineArchive (V2)

Aplicación web para **alquilar**, **almacenar** y **gestionar** series y películas vistas.

## 📋 Descripción

CineArchive es una plataforma integral que permite a los usuarios llevar un registro detallado de su contenido audiovisual visto, alquilar series o películas temporalmente, y gestionar su biblioteca personal de entretenimiento. El sistema está diseñado con una arquitectura de roles robusta que permite diferentes niveles de acceso y funcionalidades según el tipo de usuario.

## ✨ Características Principales

- **Sistema de alquiler digital** con gestión de períodos temporales y pagos
- **Biblioteca personal** para organizar contenido visto y favoritos
- **Sistema de listas personalizadas** y reseñas de usuarios
- **Panel de administración completo** para gestión de usuarios y políticas
- **Gestión de inventario** y control de disponibilidad de contenido
- **Sistema de reportes y analítica avanzada** para análisis de datos
- **Integración con APIs externas** (TMDb, OMDb) para enriquecer el catálogo
- **Búsqueda y filtrado avanzado** adaptado a cada tipo de usuario

## 🎯 Objetivos del Proyecto

1. Implementar un sistema robusto de **autenticación y autorización con múltiples roles** que contemple Usuarios Regulares, Administradores, Gestores de Inventario y Analistas de Datos, garantizando permisos diferenciados según responsabilidades
2. Diseñar y desarrollar un **módulo de alquiler digital** con gestión de períodos temporales y pagos para Usuarios Regulares, incluyendo biblioteca personal, listas y reseñas
3. Crear un **panel de administración completo** para que los Administradores gestionen usuarios, precios y políticas del sistema
4. Implementar un **sistema de gestión de inventario y disponibilidad** que permita a los Gestores de Inventario controlar el catálogo, stock de licencias y disponibilidad de contenido
5. Desarrollar un **sistema de reportes y analítica avanzada** que permita a los Analistas de Datos estudiar comportamiento de usuarios, rendimiento de géneros y contenido más popular
6. Integrar APIs externas para enriquecer el catálogo con información actualizada, facilitando la importación masiva de contenido
7. Proporcionar herramientas avanzadas de búsqueda, filtrado y personalización adaptadas a cada tipo de actor

## 👥 Actores del Sistema

### 🎬 Usuario Regular (Cliente)

**Descripción:** Usuario final de la plataforma que consume y gestiona contenido audiovisual mediante alquiler temporal.

**Funcionalidades:**

- Navegación y búsqueda en el catálogo de películas y series
- Alquiler temporal de contenido según disponibilidad
- Gestión de biblioteca personal (contenido visto y alquilado)
- Creación y administración de listas personalizadas de favoritos
- Calificación y escritura de reseñas
- Consulta de historial de alquileres y transacciones
- Visualización de contenido durante el período de alquiler activo

### 👨‍💼 Administrador del Sistema

**Descripción:** Usuario con privilegios para la gestión estratégica de la plataforma.

**Funcionalidades:**

- Administración completa de usuarios (CRUD, modificación de roles)
- Configuración de precios y políticas de alquiler
- Definición de períodos de alquiler y restricciones
- Supervisión general del sistema y resolución de conflictos
- Auditoría de actividades y logs del sistema
- Gestión de reportes generados por Analistas
- Toma de decisiones estratégicas basadas en analytics

### 📦 Gestor de Inventario (Content Manager)

**Descripción:** Usuario especializado en la gestión del catálogo y control de disponibilidad.

**Funcionalidades:**

- Gestión completa del catálogo (CRUD de películas y series)
- Control de disponibilidad y stock de licencias digitales
- Importación masiva de contenido desde APIs externas
- Gestión de categorías, géneros, tags y metadatos
- Actualización de información del contenido (sinopsis, posters, trailers)
- Marcado de disponibilidad para alquiler
- Renovación y control de vencimiento de licencias
- Gestión de copias digitales simultáneas disponibles

### 📊 Analista de Datos (Generador de Reportes)

**Descripción:** Usuario especializado en análisis de información y generación de reportes estratégicos.

**Funcionalidades:**

- Generación de reportes de contenido más alquilado por período
- Análisis de comportamiento y patrones de consumo de usuarios
- Análisis demográfico de usuarios por rango de edad y preferencias
- Comparativa de rendimiento entre géneros
- Identificación de contenido con mejor y peor performance
- Análisis de tendencias temporales de alquileres
- Generación de dashboards y visualizaciones de datos
- Recomendaciones basadas en datos para estrategia de contenido

## 🛠️ Tecnologías Utilizadas

### **Backend:**
- **Spring Web MVC 5.3.30** - Framework principal con patrón MVC
- **Spring JDBC** - Gestión de acceso a datos
- **Java 11** - Lenguaje de programación
- **Maven** - Gestión de dependencias y construcción del proyecto
- **JDBC** - Conectividad con base de datos

### **Frontend:**
- **JSP (JavaServer Pages)** - Vistas dinámicas server-side
- **JSTL 1.2** - Biblioteca de etiquetas estándar para JSP
- **JavaScript** - Validaciones del lado del cliente y AJAX
- **HTML5 + CSS3** - Estructura y estilos

### **Base de Datos:**
- **MySQL 8.0** - Sistema de gestión de base de datos relacional
- **MySQL Workbench** - Diseño y administración de BD

### **APIs Externas:**
- **TMDb API** - The Movie Database para información de películas/series
- **OMDb API** - Open Movie Database (alternativa/complemento)

### **Seguridad:**
- **BCrypt** - Encriptación de contraseñas
- **Spring Security Filters** - Filtros de autenticación y autorización

### **Servidor de Aplicaciones:**
- **Eclipse Jetty 9.4** - Contenedor de servlets embebible

### **Librerías Adicionales:**
- **Gson 2.10.1** - Serialización/deserialización JSON
- **Apache HttpClient 4.5.14** - Cliente HTTP para APIs externas
- **Hibernate Validator 6.2.5** - Validaciones de beans
- **JUnit 4.13.2** - Framework de testing

## 📦 Instalación

```bash
# Clonar el repositorio
git clone [URL_DEL_REPOSITORIO]

# Navegar al directorio del proyecto
cd CineArchive

# Compilar el proyecto e instalar dependencias
mvn clean install

# Configurar la base de datos MySQL
# 1. Abrir MySQL Workbench
# 2. Importar el archivo: archivos de BD y extras/modelo_de_BD_CineArchiveV2.mwb
# 3. Ejecutar los scripts SQL en orden:
#    - 01_usuarios.sql
#    - 02_contenido.sql
#    - 03_alquileres.sql
#    - 04_listas.sql
#    - 05_categorias_resenas.sql
#    - 06_views_reportes.sql

# Configurar credenciales de BD (actualizar si es necesario)
# En: src/main/java/edu/utn/inspt/cinearchive/backend/config/DatabaseConfig.java

# Ejecutar la aplicación con Jetty
mvn jetty:run

# Abrir en el navegador
# http://localhost:8080/cinearchive
```

## ⚙️ Configuración

1. Configurar las credenciales de las APIs externas (TMDb, OMDb)
2. Configurar la base de datos
3. Establecer las variables de entorno necesarias
4. Configurar el sistema de autenticación y autorización

## 🚀 Uso

> **Nota:** Completar con instrucciones específicas de uso
>

## 📝 Manual de Instrucciones

> **Nota:** Incluir manual detallado de instrucciones para cada tipo de usuario
>

## 🤝 Contribución

> **Nota:** Añadir guidelines para contribuir al proyecto si aplica
>

## 📄 Licencia

> **Nota:** Especificar la licencia del proyecto
>

## 👨‍💻 Autor

**Francisco Chiminelli** - [francisco.chiminelli@alu.inspt.utn.edu.ar](mailto:francisco.chiminelli@alu.inspt.utn.edu.ar)
**Franco Vilaseco** - [franco.vilaseco@alu.inspt.utn.edu.ar](mailto:franco.vilaseco@alu.inspt.utn.edu.ar)
**Martín Zembo** - [martin.zembo@alu.inspt.utn.edu.ar](mailto:martin.zembo@alu.inspt.utn.edu.ar)

## 📌 Notas de Entrega

- Informe del código
- Manual de instrucciones
- Link con enlace de Git
- Despliegue del trabajo con defensa oral
- Detalle de tecnologías utilizadas

---

*Proyecto desarrollado para [INSPT UTN]*