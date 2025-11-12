            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px 0;
            border-bottom: 1px solid #f0f0f0;
        }

        .metric-row:last-child {
            border-bottom: none;
        }

        .metric-label {
            font-weight: 500;
            color: #666;
        }

        .metric-value {
            font-weight: bold;
            font-size: 1.1em;
            color: #1a1a1a;
        }

        .loading {
            text-align: center;
            padding: 40px;
            color: #666;
        }

        .loading i {
            font-size: 2em;
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        .export-section {
            background: white;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }

        .export-buttons {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
        }
    </style>
</head>
<body>
    <div class="analytics-dashboard">
        <!-- Header -->
        <div class="dashboard-header">
            <h1><i class="fas fa-chart-line"></i> Centro de Análisis de Datos</h1>
            <p>Dashboard integral para análisis de comportamiento, tendencias y métricas de negocio</p>
        </div>

        <!-- Controles de Filtros -->
        <div class="filter-controls">
            <h3><i class="fas fa-filter"></i> Filtros de Análisis</h3>
            <div class="filter-row">
                <div class="form-group">
                    <label>Período de Análisis:</label>
                    <select id="periodo-filter" onchange="actualizarAnalytics()">
                        <option value="7">Últimos 7 días</option>
                        <option value="30" selected>Últimos 30 días</option>
                        <option value="90">Últimos 3 meses</option>
                        <option value="365">Último año</option>
                        <option value="custom">Personalizado</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Fecha Inicio:</label>
                    <input type="date" id="fecha-inicio" onchange="actualizarAnalytics()">
                </div>
                <div class="form-group">
                    <label>Fecha Fin:</label>
                    <input type="date" id="fecha-fin" onchange="actualizarAnalytics()">
                </div>
                <div class="form-group">
                    <label>Tipo de Contenido:</label>
                    <select id="tipo-contenido-filter" onchange="actualizarAnalytics()">
                        <option value="">Todos</option>
                        <option value="PELICULA">Películas</option>
                        <option value="SERIE">Series</option>
                    </select>
                </div>
            </div>
            <div class="filter-row">
                <button class="btn btn-primary" onclick="actualizarAnalytics()">
                    <i class="fas fa-sync"></i> Actualizar Análisis
                </button>
                <button class="btn btn-secondary" onclick="resetearFiltros()">
                    <i class="fas fa-undo"></i> Resetear Filtros
                </button>
            </div>
        </div>

        <!-- KPIs Principales -->
        <div class="kpi-grid">
            <div class="kpi-card">
                <div class="icon"><i class="fas fa-eye"></i></div>
                <div class="number" id="kpi-visualizaciones">-</div>
                <div class="label">Total Visualizaciones</div>
                <div class="trend up" id="trend-visualizaciones">
                    <i class="fas fa-arrow-up"></i> +12% vs período anterior
                </div>
            </div>
            <div class="kpi-card">
                <div class="icon"><i class="fas fa-dollar-sign"></i></div>
                <div class="number" id="kpi-ingresos">-</div>
                <div class="label">Ingresos Totales</div>
                <div class="trend up" id="trend-ingresos">
                    <i class="fas fa-arrow-up"></i> +8% vs período anterior
                </div>
            </div>
            <div class="kpi-card">
                <div class="icon"><i class="fas fa-users"></i></div>
                <div class="number" id="kpi-usuarios-activos">-</div>
                <div class="label">Usuarios Activos</div>
                <div class="trend neutral" id="trend-usuarios">
                    <i class="fas fa-minus"></i> Sin cambios
                </div>
            </div>
            <div class="kpi-card">
                <div class="icon"><i class="fas fa-star"></i></div>
                <div class="number" id="kpi-calificacion-promedio">-</div>
                <div class="label">Calificación Promedio</div>
                <div class="trend up" id="trend-calificacion">
                    <i class="fas fa-arrow-up"></i> +0.2 vs período anterior
                </div>
            </div>
        </div>

        <!-- Sección Principal de Analytics -->
        <div class="analytics-sections">
            <div class="chart-section">
                <h3><i class="fas fa-chart-area"></i> Tendencias de Alquileres</h3>
                <div class="chart-container">
                    <canvas id="tendencias-chart"></canvas>
                </div>
            </div>

            <div class="reports-section">
                <div class="report-card">
                    <h4><i class="fas fa-trophy"></i> Top 5 Contenidos</h4>
                    <ul class="top-content-list" id="top-contenidos">
                        <!-- Se llena dinámicamente -->
                    </ul>
                    <div class="report-actions">
                        <button class="btn btn-info" onclick="verReporteCompleto('contenidos')">Ver Completo</button>
                    </div>
                </div>

                <div class="report-card">
                    <h4><i class="fas fa-tags"></i> Géneros Populares</h4>
                    <ul class="top-content-list" id="top-generos">
                        <!-- Se llena dinámicamente -->
                    </ul>
                    <div class="report-actions">
                        <button class="btn btn-info" onclick="verReporteCompleto('generos')">Ver Completo</button>
                    </div>
                </div>

                <div class="report-card">
                    <h4><i class="fas fa-chart-pie"></i> Análisis Rápido</h4>
                    <div class="metric-row">
                        <span class="metric-label">Tasa de Conversión:</span>
                        <span class="metric-value" id="tasa-conversion">-</span>
                    </div>
                    <div class="metric-row">
                        <span class="metric-label">Duración Promedio:</span>
                        <span class="metric-value" id="duracion-promedio">-</span>
                    </div>
                    <div class="metric-row">
                        <span class="metric-label">Retención:</span>
                        <span class="metric-value" id="retencion">-</span>
                    </div>
                </div>
            </div>
        </div>

        <!-- Analytics Detallados -->
        <div class="detailed-analytics">
            <div class="analytics-card">
                <h3><i class="fas fa-chart-bar"></i> Análisis Demográfico</h3>
                <div class="chart-container" style="height: 300px;">
                    <canvas id="demografico-chart"></canvas>
                </div>
            </div>

            <div class="analytics-card">
                <h3><i class="fas fa-chart-pie"></i> Distribución por Géneros</h3>
                <div class="chart-container" style="height: 300px;">
                    <canvas id="generos-chart"></canvas>
                </div>
            </div>
        </div>

        <!-- Sección de Reportes -->
        <div class="export-section">
            <h3><i class="fas fa-file-export"></i> Generación de Reportes</h3>
            <p>Genera reportes personalizados basados en los filtros actuales</p>
            <div class="export-buttons">
                <button class="btn btn-primary" onclick="generarReporte('mas-alquilados')">
                    <i class="fas fa-trophy"></i> Contenidos Más Alquilados
                </button>
                <button class="btn btn-success" onclick="generarReporte('demografico')">
                    <i class="fas fa-users"></i> Análisis Demográfico
                </button>
                <button class="btn btn-info" onclick="generarReporte('tendencias')">
                    <i class="fas fa-chart-line"></i> Tendencias Temporales
                </button>
                <button class="btn btn-secondary" onclick="generarReporte('comportamiento')">
                    <i class="fas fa-brain"></i> Comportamiento de Usuarios
                </button>
            </div>
        </div>

        <!-- Métricas Avanzadas -->
        <div class="detailed-analytics">
            <div class="analytics-card">
                <h3><i class="fas fa-clock"></i> Métricas Temporales</h3>
                <div class="metric-row">
                    <span class="metric-label">Hora pico de alquileres:</span>
                    <span class="metric-value" id="hora-pico">-</span>
                </div>
                <div class="metric-row">
                    <span class="metric-label">Día más activo:</span>
                    <span class="metric-value" id="dia-activo">-</span>
                </div>
                <div class="metric-row">
                    <span class="metric-label">Temporada alta:</span>
                    <span class="metric-value" id="temporada-alta">-</span>
                </div>
                <div class="metric-row">
                    <span class="metric-label">Duración promedio alquiler:</span>
                    <span class="metric-value" id="duracion-alquiler">-</span>
                </div>
            </div>

            <div class="analytics-card">
                <h3><i class="fas fa-bullseye"></i> Métricas de Negocio</h3>
                <div class="metric-row">
                    <span class="metric-label">Valor promedio por usuario:</span>
                    <span class="metric-value" id="valor-usuario">-</span>
                </div>
                <div class="metric-row">
                    <span class="metric-label">Tasa de retorno:</span>
                    <span class="metric-value" id="tasa-retorno">-</span>
                </div>
                <div class="metric-row">
                    <span class="metric-label">Satisfacción promedio:</span>
                    <span class="metric-value" id="satisfaccion">-</span>
                </div>
                <div class="metric-row">
                    <span class="metric-label">Crecimiento mensual:</span>
                    <span class="metric-value" id="crecimiento">-</span>
                </div>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script src="/cinearchive/js/reportes.js"></script>
    <script src="/cinearchive/js/charts.js"></script>
    <script>
        // Inicializar dashboard
        document.addEventListener('DOMContentLoaded', function() {
            inicializarFechas();
            cargarDashboardCompleto();
            inicializarCharts();
        });

        function inicializarFechas() {
            const hoy = new Date();
            const hace30dias = new Date(hoy.getTime() - (30 * 24 * 60 * 60 * 1000));

            document.getElementById('fecha-fin').value = hoy.toISOString().split('T')[0];
            document.getElementById('fecha-inicio').value = hace30dias.toISOString().split('T')[0];
        }

        function actualizarAnalytics() {
            mostrarLoading();
            cargarDashboardCompleto();
            actualizarCharts();
        }
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
        function mostrarLoading() {
            // Mostrar indicadores de carga
            const elementos = ['kpi-visualizaciones', 'kpi-ingresos', 'kpi-usuarios-activos', 'kpi-calificacion-promedio'];
            elementos.forEach(id => {
                document.getElementById(id).innerHTML = '<i class="fas fa-spinner fa-spin"></i>';
            });
        }

        function resetearFiltros() {
            document.getElementById('periodo-filter').value = '30';
            document.getElementById('tipo-contenido-filter').value = '';
            inicializarFechas();
            actualizarAnalytics();
        }
    </script>
</body>
</html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CineArchive - Analista de Datos</title>
    <link rel="stylesheet" href="/cinearchive/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        .analytics-dashboard {
            max-width: 1400px;
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
            position: relative;
        }

        .dashboard-header::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grain" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="25" cy="25" r="1" fill="rgba(255,255,255,0.1)"/><circle cx="75" cy="75" r="0.5" fill="rgba(255,255,255,0.05)"/></pattern></defs><rect width="100" height="100" fill="url(%23grain)"/></svg>');
            border-radius: 15px;
        }

        .dashboard-header h1, .dashboard-header p {
            position: relative;
            z-index: 1;
        }

        .kpi-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 25px;
            margin-bottom: 40px;
        }

        .kpi-card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
            text-align: center;
            transition: all 0.3s ease;
            border-left: 5px solid #dc143c;
        }

        .kpi-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 12px 35px rgba(0,0,0,0.15);
        }

        .kpi-card .icon {
            font-size: 3.5em;
            margin-bottom: 20px;
            color: #dc143c;
        }

        .kpi-card .number {
            font-size: 3em;
            font-weight: bold;
            margin-bottom: 10px;
            color: #1a1a1a;
        }

        .kpi-card .label {
            color: #666;
            font-size: 1.2em;
            font-weight: 500;
        }

        .kpi-card .trend {
            margin-top: 10px;
            font-size: 0.9em;
        }

        .trend.up { color: #28a745; }
        .trend.down { color: #dc3545; }
        .trend.neutral { color: #ffc107; }

        .analytics-sections {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 30px;
            margin-bottom: 30px;
        }

        .chart-section {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
        }

        .chart-section h3 {
            margin-bottom: 25px;
            color: #1a1a1a;
            font-size: 1.5em;
        }

        .chart-container {
            position: relative;
            height: 400px;
            margin-bottom: 20px;
        }

        .reports-section {
            display: grid;
            grid-template-columns: 1fr;
            gap: 25px;
        }

        .report-card {
            background: white;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 6px 20px rgba(0,0,0,0.1);
            transition: all 0.3s ease;
        }

        .report-card:hover {
            transform: translateY(-3px);
        }

        .report-card h4 {
            margin-bottom: 15px;
            color: #1a1a1a;
        }

        .report-actions {
            margin-top: 20px;
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .btn {
            padding: 12px 20px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: bold;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            font-size: 0.9em;
        }

        .btn-primary {
            background: linear-gradient(135deg, #dc143c, #b91c3c);
            color: white;
        }

        .btn-secondary {
            background: linear-gradient(135deg, #6c757d, #545b62);
            color: white;
        }

        .btn-success {
            background: linear-gradient(135deg, #28a745, #20c997);
            color: white;
        }

        .btn-info {
            background: linear-gradient(135deg, #17a2b8, #20c997);
            color: white;
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.2);
        }

        .top-content-list {
            list-style: none;
            padding: 0;
        }

        .top-content-list li {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 12px 0;
            border-bottom: 1px solid #eee;
        }

        .top-content-list li:last-child {
            border-bottom: none;
        }

        .content-title {
            font-weight: bold;
            color: #1a1a1a;
        }

        .content-metric {
            font-weight: bold;
            color: #dc143c;
        }

        .filter-controls {
            background: white;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }

        .filter-row {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #1a1a1a;
        }

        .form-group input,
        .form-group select {
            width: 100%;
            padding: 12px;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            transition: border-color 0.3s ease;
            box-sizing: border-box;
        }

        .form-group input:focus,
        .form-group select:focus {
            outline: none;
            border-color: #dc143c;
        }

        .detailed-analytics {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(450px, 1fr));
            gap: 30px;
            margin-bottom: 30px;
        }

        .analytics-card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
        }

        .analytics-card h3 {
            margin-bottom: 25px;
            color: #1a1a1a;
            font-size: 1.4em;
        }

        .metric-row {

