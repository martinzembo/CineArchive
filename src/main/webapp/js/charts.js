/**
 * CineArchive - Sistema de Gráficos
 * JavaScript para manejo de gráficos y visualizaciones del Developer 3
 * Utiliza Chart.js para crear gráficos interactivos y responsivos
 */

// =====================================
// VARIABLES GLOBALES PARA GRÁFICOS
// =====================================
let chartsInstances = {};
let chartColors = {
    primary: '#dc143c',
    secondary: '#6c757d',
    success: '#28a745',
    info: '#17a2b8',
    warning: '#ffc107',
    danger: '#dc3545',
    light: '#f8f9fa',
    dark: '#343a40'
};

let chartGradients = {};

// =====================================
// INICIALIZACIÓN DE GRÁFICOS
// =====================================

/**
 * Inicializar todos los gráficos del dashboard
 */
function inicializarCharts() {
    // Configuración global de Chart.js
    Chart.defaults.font.family = 'Arial, sans-serif';
    Chart.defaults.font.size = 12;
    Chart.defaults.color = '#666';

    // Inicializar cada gráfico
    inicializarTendenciasChart();
    inicializarDemograficoChart();
    inicializarGenerosChart();

    console.log('Gráficos inicializados correctamente');
}

/**
 * Actualizar todos los gráficos con nuevos datos
 */
function actualizarCharts() {
    actualizarTendenciasChart();
    actualizarDemograficoChart();
    actualizarGenerosChart();
}

// =====================================
// GRÁFICO DE TENDENCIAS
// =====================================

/**
 * Inicializar gráfico de tendencias de alquileres
 */
function inicializarTendenciasChart() {
    const ctx = document.getElementById('tendencias-chart');
    if (!ctx) return;

    // Crear gradiente
    const gradient = ctx.getContext('2d').createLinearGradient(0, 0, 0, 400);
    gradient.addColorStop(0, 'rgba(220, 20, 60, 0.8)');
    gradient.addColorStop(1, 'rgba(220, 20, 60, 0.1)');
    chartGradients.tendencias = gradient;

    // Datos iniciales
    const data = {
        labels: generarUltimos30Dias(),
        datasets: [{
            label: 'Alquileres',
            data: generarDatosAleatorios(30, 10, 80),
            borderColor: chartColors.primary,
            backgroundColor: gradient,
            borderWidth: 3,
            fill: true,
            tension: 0.4,
            pointBackgroundColor: chartColors.primary,
            pointBorderColor: '#fff',
            pointBorderWidth: 2,
            pointRadius: 4,
            pointHoverRadius: 6
        }, {
            label: 'Ingresos ($)',
            data: generarDatosAleatorios(30, 500, 3000),
            borderColor: chartColors.success,
            backgroundColor: 'rgba(40, 167, 69, 0.1)',
            borderWidth: 2,
            fill: false,
            tension: 0.4,
            yAxisID: 'y1'
        }]
    };

    const config = {
        type: 'line',
        data: data,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                mode: 'index',
                intersect: false
            },
            plugins: {
                title: {
                    display: false
                },
                legend: {
                    position: 'top',
                    labels: {
                        usePointStyle: true,
                        padding: 20
                    }
                },
                tooltip: {
                    backgroundColor: 'rgba(0, 0, 0, 0.8)',
                    titleColor: '#fff',
                    bodyColor: '#fff',
                    cornerRadius: 8,
                    displayColors: true,
                    callbacks: {
                        label: function(context) {
                            let label = context.dataset.label || '';
                            if (label) {
                                label += ': ';
                            }
                            if (context.datasetIndex === 1) {
                                label += formatCurrency(context.parsed.y);
                            } else {
                                label += context.parsed.y;
                            }
                            return label;
                        }
                    }
                }
            },
            scales: {
                x: {
                    display: true,
                    title: {
                        display: true,
                        text: 'Fecha'
                    },
                    grid: {
                        display: false
                    }
                },
                y: {
                    type: 'linear',
                    display: true,
                    position: 'left',
                    title: {
                        display: true,
                        text: 'Cantidad de Alquileres'
                    },
                    grid: {
                        color: 'rgba(0, 0, 0, 0.1)'
                    }
                },
                y1: {
                    type: 'linear',
                    display: true,
                    position: 'right',
                    title: {
                        display: true,
                        text: 'Ingresos (ARS)'
                    },
                    grid: {
                        drawOnChartArea: false
                    },
                    ticks: {
                        callback: function(value) {
                            return '$' + value.toLocaleString();
                        }
                    }
                }
            },
            animation: {
                duration: 1000,
                easing: 'easeInOutQuart'
            }
        }
    };

    chartsInstances.tendencias = new Chart(ctx, config);
}

/**
 * Actualizar datos del gráfico de tendencias
 */
function actualizarTendenciasChart() {
    if (!chartsInstances.tendencias) return;

    // Simular nuevos datos
    const nuevosAlquileres = generarDatosAleatorios(30, 10, 80);
    const nuevosIngresos = generarDatosAleatorios(30, 500, 3000);

    chartsInstances.tendencias.data.datasets[0].data = nuevosAlquileres;
    chartsInstances.tendencias.data.datasets[1].data = nuevosIngresos;

    chartsInstances.tendencias.update('active');
}

// =====================================
// GRÁFICO DEMOGRÁFICO
// =====================================

/**
 * Inicializar gráfico demográfico (barras)
 */
function inicializarDemograficoChart() {
    const ctx = document.getElementById('demografico-chart');
    if (!ctx) return;

    const data = {
        labels: ['18-25', '26-35', '36-45', '46-55', '56-65', '65+'],
        datasets: [{
            label: 'Usuarios por Edad',
            data: [320, 450, 380, 290, 180, 120],
            backgroundColor: [
                'rgba(220, 20, 60, 0.8)',
                'rgba(40, 167, 69, 0.8)',
                'rgba(23, 162, 184, 0.8)',
                'rgba(255, 193, 7, 0.8)',
                'rgba(108, 117, 125, 0.8)',
                'rgba(220, 53, 69, 0.8)'
            ],
            borderColor: [
                'rgba(220, 20, 60, 1)',
                'rgba(40, 167, 69, 1)',
                'rgba(23, 162, 184, 1)',
                'rgba(255, 193, 7, 1)',
                'rgba(108, 117, 125, 1)',
                'rgba(220, 53, 69, 1)'
            ],
            borderWidth: 2,
            borderRadius: 8,
            borderSkipped: false
        }]
    };

    const config = {
        type: 'bar',
        data: data,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                title: {
                    display: false
                },
                legend: {
                    display: false
                },
                tooltip: {
                    backgroundColor: 'rgba(0, 0, 0, 0.8)',
                    titleColor: '#fff',
                    bodyColor: '#fff',
                    cornerRadius: 8,
                    callbacks: {
                        label: function(context) {
                            return `${context.parsed.y} usuarios`;
                        }
                    }
                }
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Rango de Edad'
                    },
                    grid: {
                        display: false
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: 'Cantidad de Usuarios'
                    },
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(0, 0, 0, 0.1)'
                    }
                }
            },
            animation: {
                duration: 1200,
                easing: 'easeOutBounce'
            }
        }
    };

    chartsInstances.demografico = new Chart(ctx, config);
}

/**
 * Actualizar gráfico demográfico
 */
function actualizarDemograficoChart() {
    if (!chartsInstances.demografico) return;

    const nuevosdatos = generarDatosAleatorios(6, 100, 500);
    chartsInstances.demografico.data.datasets[0].data = nuevosdatos;
    chartsInstances.demografico.update('active');
}

// =====================================
// GRÁFICO DE GÉNEROS (PIE/DOUGHNUT)
// =====================================

/**
 * Inicializar gráfico de distribución por géneros
 */
function inicializarGenerosChart() {
    const ctx = document.getElementById('generos-chart');
    if (!ctx) return;

    const data = {
        labels: ['Acción', 'Drama', 'Comedia', 'Ciencia Ficción', 'Terror', 'Romance'],
        datasets: [{
            data: [34, 28, 22, 18, 15, 12],
            backgroundColor: [
                '#dc143c',
                '#28a745',
                '#ffc107',
                '#17a2b8',
                '#6c757d',
                '#e83e8c'
            ],
            borderColor: '#fff',
            borderWidth: 3,
            hoverOffset: 10
        }]
    };

    const config = {
        type: 'doughnut',
        data: data,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '60%',
            plugins: {
                title: {
                    display: false
                },
                legend: {
                    position: 'bottom',
                    labels: {
                        padding: 20,
                        usePointStyle: true,
                        generateLabels: function(chart) {
                            const data = chart.data;
                            if (data.labels.length && data.datasets.length) {
                                return data.labels.map((label, i) => {
                                    const dataset = data.datasets[0];
                                    const value = dataset.data[i];
                                    const total = dataset.data.reduce((a, b) => a + b, 0);
                                    const percentage = ((value / total) * 100).toFixed(1);

                                    return {
                                        text: `${label} (${percentage}%)`,
                                        fillStyle: dataset.backgroundColor[i],
                                        strokeStyle: dataset.borderColor,
                                        lineWidth: dataset.borderWidth,
                                        pointStyle: 'circle',
                                        hidden: isNaN(value),
                                        index: i
                                    };
                                });
                            }
                            return [];
                        }
                    }
                },
                tooltip: {
                    backgroundColor: 'rgba(0, 0, 0, 0.8)',
                    titleColor: '#fff',
                    bodyColor: '#fff',
                    cornerRadius: 8,
                    callbacks: {
                        label: function(context) {
                            const total = context.dataset.data.reduce((a, b) => a + b, 0);
                            const value = context.parsed;
                            const percentage = ((value / total) * 100).toFixed(1);
                            return `${context.label}: ${value} (${percentage}%)`;
                        }
                    }
                }
            },
            animation: {
                animateRotate: true,
                animateScale: true,
                duration: 1500,
                easing: 'easeInOutQuart'
            }
        }
    };

    chartsInstances.generos = new Chart(ctx, config);
}

/**
 * Actualizar gráfico de géneros
 */
function actualizarGenerosChart() {
    if (!chartsInstances.generos) return;

    const nuevosGeneros = generarDatosAleatorios(6, 10, 40);
    chartsInstances.generos.data.datasets[0].data = nuevosGeneros;
    chartsInstances.generos.update('active');
}

// =====================================
// FUNCIONES DE UTILIDAD
// =====================================

/**
 * Generar fechas de los últimos N días
 */
function generarUltimos30Dias() {
    const fechas = [];
    const hoy = new Date();

    for (let i = 29; i >= 0; i--) {
        const fecha = new Date(hoy);
        fecha.setDate(fecha.getDate() - i);
        fechas.push(fecha.toLocaleDateString('es-AR', {
            month: 'short',
            day: 'numeric'
        }));
    }

    return fechas;
}

/**
 * Generar datos aleatorios para gráficos
 */
function generarDatosAleatorios(cantidad, min, max) {
    const datos = [];
    for (let i = 0; i < cantidad; i++) {
        datos.push(Math.floor(Math.random() * (max - min + 1)) + min);
    }
    return datos;
}

/**
 * Formatear moneda para gráficos
 */
function formatCurrency(amount) {
    return new Intl.NumberFormat('es-AR', {
        style: 'currency',
        currency: 'ARS'
    }).format(amount);
}

/**
 * Redimensionar gráficos cuando cambia el tamaño de ventana
 */
function redimensionarCharts() {
    Object.values(chartsInstances).forEach(chart => {
        if (chart) {
            chart.resize();
        }
    });
}

/**
 * Destruir gráfico específico
 */
function destruirChart(nombre) {
    if (chartsInstances[nombre]) {
        chartsInstances[nombre].destroy();
        delete chartsInstances[nombre];
    }
}

/**
 * Destruir todos los gráficos
 */
function destruirTodosLosCharts() {
    Object.keys(chartsInstances).forEach(nombre => {
        destruirChart(nombre);
    });
}

// =====================================
// GRÁFICOS DINÁMICOS ADICIONALES
// =====================================

/**
 * Crear gráfico de líneas temporal
 */
function crearGraficoTemporal(canvasId, datos, opciones = {}) {
    const ctx = document.getElementById(canvasId);
    if (!ctx) return null;

    const configuracion = {
        type: 'line',
        data: datos,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'top'
                }
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            },
            ...opciones
        }
    };

    return new Chart(ctx, configuracion);
}

/**
 * Crear gráfico de barras horizontal
 */
function crearGraficoBarrasHorizontal(canvasId, datos, opciones = {}) {
    const ctx = document.getElementById(canvasId);
    if (!ctx) return null;

    const configuracion = {
        type: 'bar',
        data: datos,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            indexAxis: 'y',
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                x: {
                    beginAtZero: true
                }
            },
            ...opciones
        }
    };

    return new Chart(ctx, configuracion);
}

/**
 * Actualizar gráfico con nuevos datos
 */
function actualizarGrafico(chart, nuevosDatos, animacion = true) {
    if (!chart) return;

    chart.data = nuevosDatos;
    chart.update(animacion ? 'active' : 'none');
}

// =====================================
// TEMAS Y PERSONALIZACIÓN
// =====================================

/**
 * Aplicar tema oscuro a los gráficos
 */
function aplicarTemaOscuro() {
    Chart.defaults.color = '#e9ecef';
    Chart.defaults.borderColor = 'rgba(255, 255, 255, 0.1)';
    Chart.defaults.backgroundColor = 'rgba(255, 255, 255, 0.1)';

    // Reinicializar gráficos con el nuevo tema
    destruirTodosLosCharts();
    setTimeout(() => {
        inicializarCharts();
    }, 100);
}

/**
 * Aplicar tema claro a los gráficos
 */
function aplicarTemaClaro() {
    Chart.defaults.color = '#666';
    Chart.defaults.borderColor = 'rgba(0, 0, 0, 0.1)';
    Chart.defaults.backgroundColor = 'rgba(0, 0, 0, 0.1)';

    // Reinicializar gráficos con el nuevo tema
    destruirTodosLosCharts();
    setTimeout(() => {
        inicializarCharts();
    }, 100);
}

// =====================================
// EXPORTACIÓN DE GRÁFICOS
// =====================================

/**
 * Exportar gráfico como imagen
 */
function exportarGraficoComoImagen(nombreChart, formato = 'png') {
    const chart = chartsInstances[nombreChart];
    if (!chart) return;

    const url = chart.toBase64Image();
    const link = document.createElement('a');
    link.download = `grafico_${nombreChart}_${Date.now()}.${formato}`;
    link.href = url;
    link.click();
}

/**
 * Exportar todos los gráficos
 */
function exportarTodosLosGraficos() {
    Object.keys(chartsInstances).forEach(nombre => {
        setTimeout(() => {
            exportarGraficoComoImagen(nombre);
        }, 500); // Delay para evitar bloquear el navegador
    });
}

// =====================================
// EVENT LISTENERS
// =====================================

// Redimensionar gráficos cuando cambia el tamaño de ventana
window.addEventListener('resize', redimensionarCharts);

// Limpiar gráficos al salir de la página
window.addEventListener('beforeunload', destruirTodosLosCharts);

// =====================================
// EXPORTACIÓN DE FUNCIONES GLOBALES
// =====================================

// Hacer funciones disponibles globalmente
window.inicializarCharts = inicializarCharts;
window.actualizarCharts = actualizarCharts;
window.crearGraficoTemporal = crearGraficoTemporal;
window.crearGraficoBarrasHorizontal = crearGraficoBarrasHorizontal;
window.exportarGraficoComoImagen = exportarGraficoComoImagen;
window.aplicarTemaOscuro = aplicarTemaOscuro;
window.aplicarTemaClaro = aplicarTemaClaro;

console.log('Sistema de gráficos Chart.js inicializado');
