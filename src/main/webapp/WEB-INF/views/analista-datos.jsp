<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Analista de Datos - CineArchive</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <%@ include file="fragments/header.jsp" %>

    <div class="container">
        <div class="admin-panel">
            <h1>Panel de Analítica y Reportes</h1>

            <!-- KPIs Principales -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon">📊</div>
                    <div class="stat-content">
                        <h3>Alquileres del Mes</h3>
                        <p class="stat-number" id="alquileres-mes">2,847</p>
                        <span class="stat-change positive">+18% vs mes anterior</span>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon">👥</div>
                    <div class="stat-content">
                        <h3>Usuarios Activos</h3>
                        <p class="stat-number" id="usuarios-activos">892</p>
                        <span class="stat-change positive">+12% esta semana</span>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon">⭐</div>
                    <div class="stat-content">
                        <h3>Calificación Promedio</h3>
                        <p class="stat-number" id="calificacion-promedio">4.3/5</p>
                        <span class="stat-change">En todo el catálogo</span>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon">🔥</div>
                    <div class="stat-content">
                        <h3>Tasa de Retención</h3>
                        <p class="stat-number" id="tasa-retencion">78%</p>
                        <span class="stat-change positive">+5% este trimestre</span>
                    </div>
                </div>
            </div>

            <!-- Generador de Reportes -->
            <section class="admin-section">
                <div class="section-header">
                    <h2>📄 Generador de Reportes</h2>
                </div>

                <div class="report-generator">
                    <div class="form-grid">
                        <div class="form-group">
                            <label for="tipo-reporte">Tipo de Reporte:</label>
                            <select id="tipo-reporte">
                                <option value="">Seleccionar tipo...</option>
                                <option value="mas-alquilados">Contenido Más Alquilado</option>
                                <option value="rendimiento-generos">Rendimiento por Género</option>
                                <option value="comportamiento-usuarios">Comportamiento de Usuarios</option>
                                <option value="tendencias">Tendencias Temporales</option>
                                <option value="demografico">Análisis Demográfico</option>
                                <option value="ingresos">Análisis de Ingresos</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="periodo-reporte">Período:</label>
                            <select id="periodo-reporte">
                                <option value="ultima-semana">Última Semana</option>
                                <option value="ultimo-mes" selected>Último Mes</option>
                                <option value="ultimo-trimestre">Último Trimestre</option>
                                <option value="ultimo-año">Último Año</option>
                                <option value="personalizado">Personalizado</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="fecha-inicio">Fecha Inicio:</label>
                            <input type="date" id="fecha-inicio">
                        </div>
                        <div class="form-group">
                            <label for="fecha-fin">Fecha Fin:</label>
                            <input type="date" id="fecha-fin">
                        </div>
                        <div class="form-group">
                            <label for="formato-reporte">Formato:</label>
                            <select id="formato-reporte">
                                <option value="pdf">PDF</option>
                                <option value="excel">Excel</option>
                                <option value="csv">CSV</option>
                                <option value="html">HTML</option>
                            </select>
                        </div>
                    </div>
                    <button class="btn-primary" onclick="generarReporte()">📊 Generar Reporte</button>
                </div>
            </section>

            <!-- Top 10 Más Alquilados -->
            <section class="admin-section">
                <div class="section-header">
                    <h2>🏆 Top 10 Contenido Más Alquilado</h2>
                    <label for="filtro-top" style="display:inline-block; margin-right:10px;">Filtrar por:</label>
                    <select class="filter-select" id="filtro-top">
                        <option value="mes" selected>Este Mes</option>
                        <option value="trimestre">Este Trimestre</option>
                        <option value="año">Este Año</option>
                    </select>
                </div>

                <div class="table-container">
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Título</th>
                                <th>Tipo</th>
                                <th>Género</th>
                                <th>Alquileres</th>
                                <th>Ingresos</th>
                                <th>Calificación</th>
                                <th>Tendencia</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>1</td>
                                <td><strong>Oppenheimer</strong></td>
                                <td><span class="badge badge-movie">Película</span></td>
                                <td>Drama</td>
                                <td>342</td>
                                <td>$1,364.58</td>
                                <td>★★★★★ 4.9</td>
                                <td><span class="trend-up">📈 +25%</span></td>
                            </tr>
                            <tr>
                                <td>2</td>
                                <td><strong>Breaking Bad T1</strong></td>
                                <td><span class="badge badge-series">Serie</span></td>
                                <td>Drama</td>
                                <td>298</td>
                                <td>$1,784.02</td>
                                <td>★★★★★ 5.0</td>
                                <td><span class="trend-stable">➡️ +2%</span></td>
                            </tr>
                            <tr>
                                <td>3</td>
                                <td><strong>Barbie</strong></td>
                                <td><span class="badge badge-movie">Película</span></td>
                                <td>Comedia</td>
                                <td>276</td>
                                <td>$1,101.24</td>
                                <td>★★★★☆ 4.5</td>
                                <td><span class="trend-down">📉 -8%</span></td>
                            </tr>
                            <tr>
                                <td>4</td>
                                <td><strong>Interstellar</strong></td>
                                <td><span class="badge badge-movie">Película</span></td>
                                <td>Ciencia Ficción</td>
                                <td>254</td>
                                <td>$1,013.46</td>
                                <td>★★★★★ 4.8</td>
                                <td><span class="trend-stable">➡️ +1%</span></td>
                            </tr>
                            <tr>
                                <td>5</td>
                                <td><strong>The Last of Us</strong></td>
                                <td><span class="badge badge-series">Serie</span></td>
                                <td>Drama/Terror</td>
                                <td>243</td>
                                <td>$1,699.57</td>
                                <td>★★★★★ 4.9</td>
                                <td><span class="trend-up">📈 +32%</span></td>
                            </tr>
                            <tr>
                                <td>6</td>
                                <td><strong>John Wick 4</strong></td>
                                <td><span class="badge badge-movie">Película</span></td>
                                <td>Acción</td>
                                <td>231</td>
                                <td>$1,152.69</td>
                                <td>★★★★☆ 4.6</td>
                                <td><span class="trend-up">📈 +15%</span></td>
                            </tr>
                            <tr>
                                <td>7</td>
                                <td><strong>Stranger Things T4</strong></td>
                                <td><span class="badge badge-series">Serie</span></td>
                                <td>Ciencia Ficción</td>
                                <td>218</td>
                                <td>$1,305.82</td>
                                <td>★★★★★ 4.7</td>
                                <td><span class="trend-stable">➡️ +3%</span></td>
                            </tr>
                            <tr>
                                <td>8</td>
                                <td><strong>Top Gun: Maverick</strong></td>
                                <td><span class="badge badge-movie">Película</span></td>
                                <td>Acción</td>
                                <td>207</td>
                                <td>$825.93</td>
                                <td>★★★★★ 4.8</td>
                                <td><span class="trend-down">📉 -5%</span></td>
                            </tr>
                            <tr>
                                <td>9</td>
                                <td><strong>Wednesday</strong></td>
                                <td><span class="badge badge-series">Serie</span></td>
                                <td>Comedia/Terror</td>
                                <td>189</td>
                                <td>$1,131.11</td>
                                <td>★★★★☆ 4.4</td>
                                <td><span class="trend-up">📈 +19%</span></td>
                            </tr>
                            <tr>
                                <td>10</td>
                                <td><strong>Dune: Part Two</strong></td>
                                <td><span class="badge badge-movie">Película</span></td>
                                <td>Ciencia Ficción</td>
                                <td>178</td>
                                <td>$888.22</td>
                                <td>★★★★★ 4.9</td>
                                <td><span class="trend-up">📈 +45%</span></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </section>

            <!-- Análisis por Género -->
            <section class="admin-section">
                <div class="section-header">
                    <h2>🎭 Rendimiento por Género</h2>
                </div>

                <div class="genre-analysis-grid">
                    <div class="genre-card">
                        <h3>🎬 Acción</h3>
                        <div class="genre-stats">
                            <div class="genre-stat">
                                <span class="stat-label">Alquileres:</span>
                                <span class="stat-value">892</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Ingresos:</span>
                                <span class="stat-value">$3,568</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Calif. Prom:</span>
                                <span class="stat-value">★★★★☆ 4.4</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Tendencia:</span>
                                <span class="trend-up">+12%</span>
                            </div>
                        </div>
                    </div>

                    <div class="genre-card">
                        <h3>😂 Comedia</h3>
                        <div class="genre-stats">
                            <div class="genre-stat">
                                <span class="stat-label">Alquileres:</span>
                                <span class="stat-value">654</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Ingresos:</span>
                                <span class="stat-value">$2,616</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Calif. Prom:</span>
                                <span class="stat-value">★★★★☆ 4.1</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Tendencia:</span>
                                <span class="trend-down">-3%</span>
                            </div>
                        </div>
                    </div>

                    <div class="genre-card">
                        <h3>🎭 Drama</h3>
                        <div class="genre-stats">
                            <div class="genre-stat">
                                <span class="stat-label">Alquileres:</span>
                                <span class="stat-value">1,234</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Ingresos:</span>
                                <span class="stat-value">$4,936</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Calif. Prom:</span>
                                <span class="stat-value">★★★★★ 4.7</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Tendencia:</span>
                                <span class="trend-up">+22%</span>
                            </div>
                        </div>
                    </div>

                    <div class="genre-card">
                        <h3>🚀 Ciencia Ficción</h3>
                        <div class="genre-stats">
                            <div class="genre-stat">
                                <span class="stat-label">Alquileres:</span>
                                <span class="stat-value">987</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Ingresos:</span>
                                <span class="stat-value">$3,948</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Calif. Prom:</span>
                                <span class="stat-value">★★★★★ 4.6</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Tendencia:</span>
                                <span class="trend-up">+28%</span>
                            </div>
                        </div>
                    </div>

                    <div class="genre-card">
                        <h3>😱 Terror</h3>
                        <div class="genre-stats">
                            <div class="genre-stat">
                                <span class="stat-label">Alquileres:</span>
                                <span class="stat-value">432</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Ingresos:</span>
                                <span class="stat-value">$1,728</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Calif. Prom:</span>
                                <span class="stat-value">★★★☆☆ 3.9</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Tendencia:</span>
                                <span class="trend-stable">+1%</span>
                            </div>
                        </div>
                    </div>

                    <div class="genre-card">
                        <h3>❤️ Romance</h3>
                        <div class="genre-stats">
                            <div class="genre-stat">
                                <span class="stat-label">Alquileres:</span>
                                <span class="stat-value">567</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Ingresos:</span>
                                <span class="stat-value">$2,268</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Calif. Prom:</span>
                                <span class="stat-value">★★★★☆ 4.2</span>
                            </div>
                            <div class="genre-stat">
                                <span class="stat-label">Tendencia:</span>
                                <span class="trend-up">+7%</span>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <!-- Análisis Demográfico -->
            <section class="admin-section">
                <div class="section-header">
                    <h2>👥 Análisis Demográfico de Usuarios</h2>
                </div>

                <div class="demographic-grid">
                    <div class="demographic-card">
                        <h3>Distribución por Edad</h3>
                        <div class="demographic-item">
                            <span class="age-range">18-24 años</span>
                            <div class="progress-bar">
                                <div class="progress-fill" style="width: 25%"></div>
                            </div>
                            <span class="percentage">25%</span>
                        </div>
                        <div class="demographic-item">
                            <span class="age-range">25-34 años</span>
                            <div class="progress-bar">
                                <div class="progress-fill" style="width: 35%"></div>
                            </div>
                            <span class="percentage">35%</span>
                        </div>
                        <div class="demographic-item">
                            <span class="age-range">35-44 años</span>
                            <div class="progress-bar">
                                <div class="progress-fill" style="width: 20%"></div>
                            </div>
                            <span class="percentage">20%</span>
                        </div>
                        <div class="demographic-item">
                            <span class="age-range">45-54 años</span>
                            <div class="progress-bar">
                                <div class="progress-fill" style="width: 12%"></div>
                            </div>
                            <span class="percentage">12%</span>
                        </div>
                        <div class="demographic-item">
                            <span class="age-range">55+ años</span>
                            <div class="progress-bar">
                                <div class="progress-fill" style="width: 8%"></div>
                            </div>
                            <span class="percentage">8%</span>
                        </div>
                    </div>

                    <div class="demographic-card">
                        <h3>Preferencias por Edad</h3>
                        <div class="preference-item">
                            <strong>18-24:</strong> Acción (32%), Terror (28%), Comedia (22%)
                        </div>
                        <div class="preference-item">
                            <strong>25-34:</strong> Drama (35%), Ciencia Ficción (25%), Acción (20%)
                        </div>
                        <div class="preference-item">
                            <strong>35-44:</strong> Drama (40%), Thriller (25%), Comedia (18%)
                        </div>
                        <div class="preference-item">
                            <strong>45-54:</strong> Drama (45%), Romance (22%), Thriller (18%)
                        </div>
                        <div class="preference-item">
                            <strong>55+:</strong> Drama (50%), Clásicos (25%), Documental (15%)
                        </div>
                    </div>
                </div>
            </section>

            <!-- Recomendaciones Estratégicas -->
            <section class="admin-section">
                <div class="section-header">
                    <h2>💡 Recomendaciones Estratégicas</h2>
                </div>

                <div class="recommendations-container">
                    <div class="recommendation-item success">
                        <span class="rec-icon">✅</span>
                        <div class="rec-content">
                            <h4>Aumentar Inventario - Ciencia Ficción</h4>
                            <p>El género de Ciencia Ficción muestra un crecimiento del 28% y alta demanda. Recomendación: Aumentar el catálogo en un 20% en los próximos 30 días.</p>
                        </div>
                    </div>
                    <div class="recommendation-item info">
                        <span class="rec-icon">ℹ️</span>
                        <div class="rec-content">
                            <h4>Enfoque en Demografía 25-34</h4>
                            <p>El segmento de 25-34 años representa el 35% de usuarios y prefiere Drama y Ciencia Ficción. Oportunidad para campañas de marketing dirigidas.</p>
                        </div>
                    </div>
                    <div class="recommendation-item warning">
                        <span class="rec-icon">⚠️</span>
                        <div class="rec-content">
                            <h4>Revisar Estrategia de Comedia</h4>
                            <p>El género Comedia muestra una tendencia negativa del -3%. Considerar renovar el catálogo o ajustar precios para mejorar el rendimiento.</p>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>

    <%@ include file="fragments/footer.jsp" %>

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
    <script>
        function generarReporte() {
            const tipo = document.getElementById('tipo-reporte').value;
            const formato = document.getElementById('formato-reporte').value;

            if (!tipo) {
                alert('Por favor selecciona un tipo de reporte');
                return;
            }

            alert('Generando reporte de tipo: ' + tipo + ' en formato ' + formato);
        }
    </script>
</body>
</html>

