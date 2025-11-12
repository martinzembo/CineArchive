                    </div>
                    <div class="stat-card">
                        <div class="icon" style="color: #28a745;"><i class="fas fa-tv"></i></div>
                        <div class="number" id="stat-series">-</div>
                        <div class="label">Series</div>
                    </div>
                    <div class="stat-card">
                        <div class="icon" style="color: #ffc107;"><i class="fas fa-eye"></i></div>
                        <div class="number" id="stat-visualizaciones">-</div>
                        <div class="label">Total Alquileres</div>
                    </div>
                    <div class="stat-card">
                        <div class="icon" style="color: #17a2b8;"><i class="fas fa-dollar-sign"></i></div>
                        <div class="number" id="stat-ingresos">-</div>
                        <div class="label">Ingresos ($)</div>
                    </div>
                </div>

                <div style="margin-top: 30px;">
                    <h3>Géneros Más Populares</h3>
                    <div id="generos-chart" style="height: 300px; background: #f8f9fa; border-radius: 10px; padding: 20px;">
                        <!-- Aquí iría un gráfico -->
                        <p style="text-align: center; color: #666; margin-top: 120px;">Gráfico de géneros (requiere biblioteca de gráficos)</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal Agregar Contenido -->
    <div id="add-content-modal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeModal('add-content-modal')">&times;</span>
            <h2>Agregar Nuevo Contenido</h2>
            <form id="add-content-form" onsubmit="agregarContenido(event)">
                <div class="form-group">
                    <label>Título:</label>
                    <input type="text" name="titulo" required>
                </div>
                <div class="form-group">
                    <label>Tipo:</label>
                    <select name="tipo" required>
                        <option value="">Seleccionar tipo</option>
                        <option value="PELICULA">Película</option>
                        <option value="SERIE">Serie</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Género:</label>
                    <input type="text" name="genero" required>
                </div>
                <div class="form-group">
                    <label>Año:</label>
                    <input type="number" name="anio" min="1900" max="2025" required>
                </div>
                <div class="form-group">
                    <label>Descripción:</label>
                    <textarea name="descripcion" rows="3"></textarea>
                </div>
                <div class="form-group">
                    <label>Precio de alquiler:</label>
                    <input type="number" name="precioAlquiler" step="0.01" min="0" required>
                </div>
                <div class="form-group">
                    <label>Copias totales:</label>
                    <input type="number" name="copiasTotales" min="1" required>
                </div>
                <button type="submit" class="btn btn-primary">Agregar Contenido</button>
                <button type="button" class="btn btn-secondary" onclick="closeModal('add-content-modal')">Cancelar</button>
            </form>
        </div>
    </div>

    <!-- Modal Agregar Categoría -->
    <div id="add-category-modal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeModal('add-category-modal')">&times;</span>
            <h2>Nueva Categoría</h2>
            <form id="add-category-form" onsubmit="agregarCategoria(event)">
                <div class="form-group">
                    <label>Nombre:</label>
                    <input type="text" name="nombre" required>
                </div>
                <div class="form-group">
                    <label>Tipo:</label>
                    <select name="tipo" required>
                        <option value="">Seleccionar tipo</option>
                        <option value="GENERO">Género</option>
                        <option value="TAG">Tag</option>
                        <option value="CLASIFICACION">Clasificación</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Descripción:</label>
                    <textarea name="descripcion" rows="2"></textarea>
                </div>
                <button type="submit" class="btn btn-primary">Crear Categoría</button>
                <button type="button" class="btn btn-secondary" onclick="closeModal('add-category-modal')">Cancelar</button>
            </form>
        </div>
    </div>

    <!-- Scripts -->
    <script src="/cinearchive/js/inventario.js"></script>
    <script>
        // Inicializar dashboard cuando carga la página
        document.addEventListener('DOMContentLoaded', function() {
            cargarEstadisticasDashboard();
            cargarContenidos();
            cargarCategorias();
            cargarResenas();
        });
    </script>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <title>CineArchive - Gestor de Inventario</title>
    <link rel="stylesheet" href="/cinearchive/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .inventory-dashboard {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        .dashboard-header {
            background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
            color: white;
            padding: 30px;
            border-radius: 15px;
            margin-bottom: 30px;
            text-align: center;
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .stat-card {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            text-align: center;
            transition: transform 0.3s ease;
        }

        .stat-card:hover {
            transform: translateY(-5px);
        }

        .stat-card .icon {
            font-size: 3em;
            margin-bottom: 15px;
        }

        .stat-card .number {
            font-size: 2.5em;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .stat-card .label {
            color: #666;
            font-size: 1.1em;
        }

        .content-tabs {
            margin-bottom: 30px;
        }

        .tab-buttons {
            display: flex;
            background: #f5f5f5;
            border-radius: 10px;
            padding: 5px;
            margin-bottom: 20px;
        }

        .tab-button {
            flex: 1;
            padding: 15px;
            text-align: center;
            background: none;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: bold;
            transition: all 0.3s ease;
        }

        .tab-button.active {
            background: #dc143c;
            color: white;
        }

        .tab-content {
            display: none;
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }

        .tab-content.active {
            display: block;
        }

        .content-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        .content-table th,
        .content-table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #eee;
        }

        .content-table th {
            background: #f8f9fa;
            font-weight: bold;
        }

        .content-table tr:hover {
            background: #f8f9fa;
        }

        .action-buttons {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: bold;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background: #dc143c;
            color: white;
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-success {
            background: #28a745;
            color: white;
        }

        .btn-danger {
            background: #dc3545;
            color: white;
        }

        .btn:hover {
            opacity: 0.8;
            transform: translateY(-2px);
        }

        .search-filters {
            display: flex;
            gap: 15px;
            margin-bottom: 20px;
            flex-wrap: wrap;
        }

        .search-filters input,
        .search-filters select {
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }

        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,0.5);
        }

        .modal-content {
            background-color: white;
            margin: 5% auto;
            padding: 30px;
            border-radius: 10px;
            width: 80%;
            max-width: 600px;
            position: relative;
        }

        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
            position: absolute;
            right: 15px;
            top: 15px;
        }

        .close:hover {
            color: #000;
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }

        .form-group input,
        .form-group select,
        .form-group textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-sizing: border-box;
        }

        .categories-section {
            margin-top: 30px;
        }

        .category-filters {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
        }

        .category-type-btn {
            padding: 8px 16px;
            border: 2px solid #dc143c;
            background: white;
            color: #dc143c;
            border-radius: 25px;
            cursor: pointer;
            font-weight: bold;
            transition: all 0.3s ease;
        }

        .category-type-btn.active {
            background: #dc143c;
            color: white;
        }

        .categories-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 15px;
        }

        .category-card {
            background: white;
            padding: 15px;
            border-radius: 8px;
            border: 1px solid #eee;
            text-align: center;
            transition: all 0.3s ease;
        }

        .category-card:hover {
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            transform: translateY(-3px);
        }
    </style>
</head>
<body>
    <div class="inventory-dashboard">
        <!-- Header -->
        <div class="dashboard-header">
            <h1><i class="fas fa-warehouse"></i> Gestor de Inventario</h1>
            <p>Panel de control completo para la gestión de contenidos, categorías y reseñas</p>
        </div>

        <!-- Estadísticas del Dashboard -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="icon" style="color: #dc143c;"><i class="fas fa-film"></i></div>
                <div class="number" id="total-contenidos">-</div>
                <div class="label">Total Contenidos</div>
            </div>
            <div class="stat-card">
                <div class="icon" style="color: #28a745;"><i class="fas fa-check-circle"></i></div>
                <div class="number" id="contenidos-disponibles">-</div>
                <div class="label">Disponibles</div>
            </div>
            <div class="stat-card">
                <div class="icon" style="color: #ffc107;"><i class="fas fa-tags"></i></div>
                <div class="number" id="total-categorias">-</div>
                <div class="label">Categorías</div>
            </div>
            <div class="stat-card">
                <div class="icon" style="color: #17a2b8;"><i class="fas fa-star"></i></div>
                <div class="number" id="total-resenas">-</div>
                <div class="label">Reseñas</div>
            </div>
        </div>

        <!-- Pestañas de Contenido -->
        <div class="content-tabs">
            <div class="tab-buttons">
                <button class="tab-button active" onclick="showTab('contenidos')">
                    <i class="fas fa-film"></i> Contenidos
                </button>
                <button class="tab-button" onclick="showTab('categorias')">
                    <i class="fas fa-tags"></i> Categorías
                </button>
                <button class="tab-button" onclick="showTab('resenas')">
                    <i class="fas fa-star"></i> Reseñas
                </button>
                <button class="tab-button" onclick="showTab('estadisticas')">
                    <i class="fas fa-chart-bar"></i> Estadísticas
                </button>
            </div>

            <!-- Tab Contenidos -->
            <div id="contenidos-tab" class="tab-content active">
                <div class="action-buttons">
                    <button class="btn btn-primary" onclick="openModal('add-content-modal')">
                        <i class="fas fa-plus"></i> Agregar Contenido
                    </button>
                    <button class="btn btn-secondary" onclick="exportarContenidos()">
                        <i class="fas fa-download"></i> Exportar
                    </button>
                    <button class="btn btn-success" onclick="sincronizarAPIs()">
                        <i class="fas fa-sync"></i> Sincronizar APIs
                    </button>
                </div>

                <div class="search-filters">
                    <input type="text" id="search-contenidos" placeholder="Buscar por título..." onkeyup="filtrarContenidos()">
                    <select id="filter-tipo" onchange="filtrarContenidos()">
                        <option value="">Todos los tipos</option>
                        <option value="PELICULA">Películas</option>
                        <option value="SERIE">Series</option>
                    </select>
                    <select id="filter-genero" onchange="filtrarContenidos()">
                        <option value="">Todos los géneros</option>
                    </select>
                    <select id="filter-disponibilidad" onchange="filtrarContenidos()">
                        <option value="">Toda disponibilidad</option>
                        <option value="true">Disponible</option>
                        <option value="false">No disponible</option>
                    </select>
                </div>

                <table class="content-table" id="contenidos-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Título</th>
                            <th>Tipo</th>
                            <th>Género</th>
                            <th>Año</th>
                            <th>Disponible</th>
                            <th>Precio</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody id="contenidos-tbody">
                        <!-- Se llena dinámicamente -->
                    </tbody>
                </table>
            </div>

            <!-- Tab Categorías -->
            <div id="categorias-tab" class="tab-content">
                <div class="action-buttons">
                    <button class="btn btn-primary" onclick="openModal('add-category-modal')">
                        <i class="fas fa-plus"></i> Nueva Categoría
                    </button>
                </div>

                <div class="categories-section">
                    <div class="category-filters">
                        <button class="category-type-btn active" onclick="filtrarCategoriasPorTipo('TODOS')">Todas</button>
                        <button class="category-type-btn" onclick="filtrarCategoriasPorTipo('GENERO')">Géneros</button>
                        <button class="category-type-btn" onclick="filtrarCategoriasPorTipo('TAG')">Tags</button>
                        <button class="category-type-btn" onclick="filtrarCategoriasPorTipo('CLASIFICACION')">Clasificaciones</button>
                    </div>

                    <div class="categories-grid" id="categories-grid">
                        <!-- Se llena dinámicamente -->
                    </div>
                </div>
            </div>

            <!-- Tab Reseñas -->
            <div id="resenas-tab" class="tab-content">
                <div class="search-filters">
                    <input type="text" id="search-resenas" placeholder="Buscar por contenido..." onkeyup="filtrarResenas()">
                    <select id="filter-calificacion" onchange="filtrarResenas()">
                        <option value="">Todas las calificaciones</option>
                        <option value="5">5 estrellas</option>
                        <option value="4">4+ estrellas</option>
                        <option value="3">3+ estrellas</option>
                        <option value="2">2+ estrellas</option>
                        <option value="1">1+ estrella</option>
                    </select>
                </div>

                <table class="content-table" id="resenas-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Usuario</th>
                            <th>Contenido</th>
                            <th>Calificación</th>
                            <th>Título</th>
                            <th>Fecha</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody id="resenas-tbody">
                        <!-- Se llena dinámicamente -->
                    </tbody>
                </table>
            </div>

            <!-- Tab Estadísticas -->
            <div id="estadisticas-tab" class="tab-content">
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="icon" style="color: #dc143c;"><i class="fas fa-video"></i></div>
                        <div class="number" id="stat-peliculas">-</div>
                        <div class="label">Películas</div>

