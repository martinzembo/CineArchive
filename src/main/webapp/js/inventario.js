
/**
 * Cargar géneros en filtro
 */
function cargarGenerosEnFiltro() {
    const generos = [...new Set(contenidosData.map(c => c.genero).filter(g => g))];
    const select = document.getElementById('filter-genero');

    select.innerHTML = '<option value="">Todos los géneros</option>' +
        generos.map(genero => `<option value="${genero}">${genero}</option>`).join('');
}

/**
 * Cargar estadísticas detalladas
 */
async function cargarEstadisticasDetalladas() {
    // Esta función se llamaría cuando se abre la pestaña de estadísticas
    showInfo('Cargando estadísticas detalladas...');
}

// =====================================
// FUNCIONES DE UTILIDAD
// =====================================

function showLoading(elementId) {
    document.getElementById(elementId).innerHTML = '<i class="fas fa-spinner fa-spin"></i>';
}

function showTableLoading(elementId) {
    document.getElementById(elementId).innerHTML =
        '<tr><td colspan="8" style="text-align: center; padding: 40px;"><i class="fas fa-spinner fa-spin"></i> Cargando...</td></tr>';
}

function setDefaultValues() {
    document.getElementById('total-contenidos').textContent = '0';
    document.getElementById('contenidos-disponibles').textContent = '0';
    document.getElementById('total-categorias').textContent = '0';
    document.getElementById('total-resenas').textContent = '0';
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('es-AR', {
        style: 'currency',
        currency: 'ARS'
    }).format(amount);
}

function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString('es-AR');
}

function generateStars(rating) {
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 !== 0;
    let starsHtml = '';

    for (let i = 0; i < fullStars; i++) {
        starsHtml += '<i class="fas fa-star" style="color: #ffc107;"></i>';
    }

    if (hasHalfStar) {
        starsHtml += '<i class="fas fa-star-half-alt" style="color: #ffc107;"></i>';
    }

    const remainingStars = 5 - fullStars - (hasHalfStar ? 1 : 0);
    for (let i = 0; i < remainingStars; i++) {
        starsHtml += '<i class="far fa-star" style="color: #ffc107;"></i>';
    }

    return starsHtml;
}

function getBadgeColorByType(tipo) {
    switch (tipo) {
        case 'GENERO': return 'primary';
        case 'TAG': return 'success';
        case 'CLASIFICACION': return 'warning';
        default: return 'secondary';
    }
}

// =====================================
// FUNCIONES DE NOTIFICACIÓN
// =====================================

function showSuccess(message) {
    // Implementar sistema de notificaciones
    alert('✅ ' + message);
}

function showError(message) {
    alert('❌ ' + message);
}

function showInfo(message) {
    alert('ℹ️ ' + message);
}

// =====================================
// EVENT LISTENERS
// =====================================

// Cerrar modales al hacer clic fuera
window.onclick = function(event) {
    if (event.target.classList.contains('modal')) {
        event.target.style.display = 'none';
    }
};

// Funciones adicionales que se pueden implementar
function gestionarCategorias(contenidoId) {
    showInfo(`Gestionar categorías para contenido ID: ${contenidoId} (Funcionalidad pendiente)`);
}

function verResenaCompleta(resenaId) {
    showInfo(`Ver reseña completa ID: ${resenaId} (Funcionalidad pendiente)`);
}

function eliminarResena(resenaId) {
    if (confirm('¿Estás seguro de que deseas eliminar esta reseña?')) {
        showInfo(`Eliminar reseña ID: ${resenaId} (Funcionalidad pendiente)`);
    }
}
/**
 * CineArchive - Gestión de Inventario
 * JavaScript para el módulo de gestión de inventario del Developer 3
 * Funcionalidades: CRUD de contenidos, categorías, reseñas y estadísticas
 */

// =====================================
// VARIABLES GLOBALES
// =====================================
let contenidosData = [];
let categoriasData = [];
let resenasData = [];
let estadisticasData = {};

// =====================================
// INICIALIZACIÓN Y CARGA DE DATOS
// =====================================

/**
 * Cargar estadísticas del dashboard principal
 */
async function cargarEstadisticasDashboard() {
    try {
        showLoading('total-contenidos');
        showLoading('contenidos-disponibles');
        showLoading('total-categorias');
        showLoading('total-resenas');

        // Cargar estadísticas generales
        const response = await fetch('/cinearchive/inventario/api/estadisticas', {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const stats = await response.json();
            estadisticasData = stats;

            // Actualizar elementos del dashboard
            document.getElementById('total-contenidos').textContent = stats.totalContenidos || 0;
            document.getElementById('contenidos-disponibles').textContent = stats.contenidosDisponibles || 0;
            document.getElementById('total-categorias').textContent = stats.totalCategorias || 0;
            document.getElementById('total-resenas').textContent = stats.totalResenas || 0;

            // Estadísticas detalladas
            document.getElementById('stat-peliculas').textContent = stats.totalPeliculas || 0;
            document.getElementById('stat-series').textContent = stats.totalSeries || 0;
            document.getElementById('stat-visualizaciones').textContent = stats.totalAlquileres || 0;
            document.getElementById('stat-ingresos').textContent = formatCurrency(stats.ingresosTotales || 0);
        } else {
            console.warn('No se pudieron cargar las estadísticas');
            // Mostrar datos por defecto
            setDefaultValues();
        }
    } catch (error) {
        console.error('Error al cargar estadísticas:', error);
        setDefaultValues();
    }
}

/**
 * Cargar todos los contenidos
 */
async function cargarContenidos() {
    try {
        showTableLoading('contenidos-tbody');

        const response = await fetch('/cinearchive/inventario/api/contenidos');

        if (response.ok) {
            contenidosData = await response.json();
            renderizarContenidos(contenidosData);
            cargarGenerosEnFiltro();
        } else {
            showError('No se pudieron cargar los contenidos');
        }
    } catch (error) {
        console.error('Error al cargar contenidos:', error);
        showError('Error de conexión al cargar contenidos');
    }
}

/**
 * Cargar todas las categorías
 */
async function cargarCategorias() {
    try {
        const response = await fetch('/cinearchive/api/categorias');

        if (response.ok) {
            categoriasData = await response.json();
            renderizarCategorias(categoriasData);
        } else {
            showError('No se pudieron cargar las categorías');
        }
    } catch (error) {
        console.error('Error al cargar categorías:', error);
        showError('Error de conexión al cargar categorías');
    }
}

/**
 * Cargar todas las reseñas
 */
async function cargarResenas() {
    try {
        showTableLoading('resenas-tbody');

        // Simulación de datos hasta que el endpoint esté disponible
        const resenasSimuladas = [
            {
                id: 1,
                usuario: { nombre: 'Juan Pérez' },
                contenido: { titulo: 'Inception' },
                calificacion: 4.5,
                titulo: 'Excelente película',
                fechaCreacion: '2024-01-15'
            },
            {
                id: 2,
                usuario: { nombre: 'María García' },
                contenido: { titulo: 'El Padrino' },
                calificacion: 5.0,
                titulo: 'Una obra maestra',
                fechaCreacion: '2024-01-20'
            }
        ];

        resenasData = resenasSimuladas;
        renderizarResenas(resenasData);
    } catch (error) {
        console.error('Error al cargar reseñas:', error);
        showError('Error de conexión al cargar reseñas');
    }
}

// =====================================
// RENDERIZADO DE DATOS
// =====================================

/**
 * Renderizar tabla de contenidos
 */
function renderizarContenidos(contenidos) {
    const tbody = document.getElementById('contenidos-tbody');

    if (!contenidos || contenidos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" style="text-align: center; padding: 40px; color: #666;">No hay contenidos disponibles</td></tr>';
        return;
    }

    tbody.innerHTML = contenidos.map(contenido => `
        <tr>
            <td>${contenido.id}</td>
            <td>
                <strong>${contenido.titulo}</strong>
                ${contenido.descripcion ? `<br><small style="color: #666;">${contenido.descripcion.substring(0, 50)}...</small>` : ''}
            </td>
            <td>
                <span class="badge badge-${contenido.tipo === 'PELICULA' ? 'primary' : 'info'}">
                    ${contenido.tipo}
                </span>
            </td>
            <td>${contenido.genero || '-'}</td>
            <td>${contenido.anio || '-'}</td>
            <td>
                <span class="badge badge-${contenido.disponibleParaAlquiler ? 'success' : 'secondary'}">
                    ${contenido.disponibleParaAlquiler ? 'Disponible' : 'No disponible'}
                </span>
            </td>
            <td>${formatCurrency(contenido.precioAlquiler || 0)}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn btn-sm btn-info" onclick="editarContenido(${contenido.id})" title="Editar">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-sm btn-success" onclick="gestionarCategorias(${contenido.id})" title="Categorías">
                        <i class="fas fa-tags"></i>
                    </button>
                    <button class="btn btn-sm btn-danger" onclick="eliminarContenido(${contenido.id})" title="Eliminar">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

/**
 * Renderizar grid de categorías
 */
function renderizarCategorias(categorias) {
    const grid = document.getElementById('categories-grid');

    if (!categorias || categorias.length === 0) {
        grid.innerHTML = '<p style="text-align: center; padding: 40px; color: #666;">No hay categorías disponibles</p>';
        return;
    }

    grid.innerHTML = categorias.map(categoria => `
        <div class="category-card" data-tipo="${categoria.tipo}">
            <h4>${categoria.nombre}</h4>
            <p class="category-type">
                <span class="badge badge-${getBadgeColorByType(categoria.tipo)}">${categoria.tipo}</span>
            </p>
            ${categoria.descripcion ? `<p class="category-description">${categoria.descripcion}</p>` : ''}
            <div class="category-actions" style="margin-top: 15px;">
                <button class="btn btn-sm btn-info" onclick="editarCategoria(${categoria.id})" title="Editar">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-sm btn-danger" onclick="eliminarCategoria(${categoria.id})" title="Eliminar">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        </div>
    `).join('');
}

/**
 * Renderizar tabla de reseñas
 */
function renderizarResenas(resenas) {
    const tbody = document.getElementById('resenas-tbody');

    if (!resenas || resenas.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" style="text-align: center; padding: 40px; color: #666;">No hay reseñas disponibles</td></tr>';
        return;
    }

    tbody.innerHTML = resenas.map(resena => `
        <tr>
            <td>${resena.id}</td>
            <td>${resena.usuario?.nombre || 'Usuario desconocido'}</td>
            <td>${resena.contenido?.titulo || 'Contenido desconocido'}</td>
            <td>
                <div class="rating">
                    ${generateStars(resena.calificacion)}
                    <span class="rating-number">${resena.calificacion}</span>
                </div>
            </td>
            <td>
                <strong>${resena.titulo}</strong>
                ${resena.texto ? `<br><small style="color: #666;">${resena.texto.substring(0, 50)}...</small>` : ''}
            </td>
            <td>${formatDate(resena.fechaCreacion)}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn btn-sm btn-info" onclick="verResenaCompleta(${resena.id})" title="Ver completa">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-danger" onclick="eliminarResena(${resena.id})" title="Eliminar">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

// =====================================
// FUNCIONES DE GESTIÓN DE PESTAÑAS
// =====================================

/**
 * Mostrar pestaña específica
 */
function showTab(tabName) {
    // Ocultar todas las pestañas
    const tabs = document.querySelectorAll('.tab-content');
    tabs.forEach(tab => tab.classList.remove('active'));

    // Remover clase active de todos los botones
    const buttons = document.querySelectorAll('.tab-button');
    buttons.forEach(btn => btn.classList.remove('active'));

    // Mostrar pestaña seleccionada
    document.getElementById(`${tabName}-tab`).classList.add('active');
    event.target.classList.add('active');

    // Cargar datos específicos si es necesario
    if (tabName === 'estadisticas') {
        cargarEstadisticasDetalladas();
    }
}

// =====================================
// FUNCIONES DE FILTRADO Y BÚSQUEDA
// =====================================

/**
 * Filtrar contenidos basado en criterios de búsqueda
 */
function filtrarContenidos() {
    const searchTerm = document.getElementById('search-contenidos').value.toLowerCase();
    const tipoFilter = document.getElementById('filter-tipo').value;
    const generoFilter = document.getElementById('filter-genero').value;
    const disponibilidadFilter = document.getElementById('filter-disponibilidad').value;

    let contenidosFiltrados = contenidosData.filter(contenido => {
        const matchSearch = contenido.titulo.toLowerCase().includes(searchTerm) ||
                           (contenido.descripcion && contenido.descripcion.toLowerCase().includes(searchTerm));

        const matchTipo = !tipoFilter || contenido.tipo === tipoFilter;
        const matchGenero = !generoFilter || contenido.genero === generoFilter;
        const matchDisponibilidad = disponibilidadFilter === '' ||
                                   contenido.disponibleParaAlquiler.toString() === disponibilidadFilter;

        return matchSearch && matchTipo && matchGenero && matchDisponibilidad;
    });

    renderizarContenidos(contenidosFiltrados);
}

/**
 * Filtrar categorías por tipo
 */
function filtrarCategoriasPorTipo(tipo) {
    // Actualizar botones activos
    document.querySelectorAll('.category-type-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    event.target.classList.add('active');

    // Filtrar categorías
    let categoriasFiltradas = categoriasData;
    if (tipo !== 'TODOS') {
        categoriasFiltradas = categoriasData.filter(cat => cat.tipo === tipo);
    }

    renderizarCategorias(categoriasFiltradas);
}

/**
 * Filtrar reseñas
 */
function filtrarResenas() {
    const searchTerm = document.getElementById('search-resenas').value.toLowerCase();
    const calificacionFilter = document.getElementById('filter-calificacion').value;

    let resenasFiltradas = resenasData.filter(resena => {
        const matchSearch = resena.contenido?.titulo.toLowerCase().includes(searchTerm) ||
                           resena.titulo.toLowerCase().includes(searchTerm);

        const matchCalificacion = !calificacionFilter || resena.calificacion >= parseFloat(calificacionFilter);

        return matchSearch && matchCalificacion;
    });

    renderizarResenas(resenasFiltradas);
}

// =====================================
// FUNCIONES DE MODALES
// =====================================

/**
 * Abrir modal
 */
function openModal(modalId) {
    document.getElementById(modalId).style.display = 'block';
}

/**
 * Cerrar modal
 */
function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';

    // Limpiar formularios
    const form = document.querySelector(`#${modalId} form`);
    if (form) {
        form.reset();
    }
}

// =====================================
// FUNCIONES CRUD - CONTENIDOS
// =====================================

/**
 * Agregar nuevo contenido
 */
async function agregarContenido(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const contenidoData = {
        titulo: formData.get('titulo'),
        tipo: formData.get('tipo'),
        genero: formData.get('genero'),
        anio: parseInt(formData.get('anio')),
        descripcion: formData.get('descripcion'),
        precioAlquiler: parseFloat(formData.get('precioAlquiler')),
        copiasTotales: parseInt(formData.get('copiasTotales')),
        copiasDisponibles: parseInt(formData.get('copiasTotales')), // Inicialmente iguales
        disponibleParaAlquiler: true
    };

    try {
        const response = await fetch('/cinearchive/inventario/api/contenidos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(contenidoData)
        });

        if (response.ok) {
            showSuccess('Contenido agregado exitosamente');
            closeModal('add-content-modal');
            cargarContenidos();
            cargarEstadisticasDashboard();
        } else {
            showError('Error al agregar el contenido');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión al agregar contenido');
    }
}

/**
 * Editar contenido
 */
async function editarContenido(id) {
    // Implementar modal de edición
    const contenido = contenidosData.find(c => c.id === id);
    if (!contenido) return;

    // Aquí se abriría un modal de edición prellenado
    showInfo(`Editando contenido: ${contenido.titulo} (Funcionalidad pendiente de implementar)`);
}

/**
 * Eliminar contenido
 */
async function eliminarContenido(id) {
    if (!confirm('¿Estás seguro de que deseas eliminar este contenido?')) {
        return;
    }

    try {
        const response = await fetch(`/cinearchive/inventario/api/contenidos/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showSuccess('Contenido eliminado exitosamente');
            cargarContenidos();
            cargarEstadisticasDashboard();
        } else {
            showError('Error al eliminar el contenido');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión al eliminar contenido');
    }
}

// =====================================
// FUNCIONES CRUD - CATEGORÍAS
// =====================================

/**
 * Agregar nueva categoría
 */
async function agregarCategoria(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const categoriaData = {
        nombre: formData.get('nombre'),
        tipo: formData.get('tipo'),
        descripcion: formData.get('descripcion')
    };

    try {
        const response = await fetch('/cinearchive/inventario/api/categorias', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(categoriaData)
        });

        if (response.ok) {
            showSuccess('Categoría creada exitosamente');
            closeModal('add-category-modal');
            cargarCategorias();
            cargarEstadisticasDashboard();
        } else {
            showError('Error al crear la categoría');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión al crear categoría');
    }
}

/**
 * Editar categoría
 */
async function editarCategoria(id) {
    const categoria = categoriasData.find(c => c.id === id);
    if (!categoria) return;

    showInfo(`Editando categoría: ${categoria.nombre} (Funcionalidad pendiente de implementar)`);
}

/**
 * Eliminar categoría
 */
async function eliminarCategoria(id) {
    if (!confirm('¿Estás seguro de que deseas eliminar esta categoría?')) {
        return;
    }

    try {
        const response = await fetch(`/cinearchive/inventario/api/categorias/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showSuccess('Categoría eliminada exitosamente');
            cargarCategorias();
            cargarEstadisticasDashboard();
        } else {
            showError('Error al eliminar la categoría');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión al eliminar categoría');
    }
}

// =====================================
// FUNCIONES AUXILIARES
// =====================================

/**
 * Exportar contenidos a CSV
 */
async function exportarContenidos() {
    try {
        const response = await fetch('/cinearchive/inventario/api/contenidos/export');
        const blob = await response.blob();

        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'contenidos_cinearchive.csv';
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);

        showSuccess('Contenidos exportados exitosamente');
    } catch (error) {
        console.error('Error al exportar:', error);
        showError('Error al exportar contenidos');
    }
}

/**
 * Sincronizar con APIs externas
 */
async function sincronizarAPIs() {
    try {
        showInfo('Iniciando sincronización con APIs externas...');

        const response = await fetch('/cinearchive/api-integracion/api/sincronizar', {
            method: 'POST'
        });

        if (response.ok) {
            showSuccess('Sincronización completada exitosamente');
            cargarContenidos();
        } else {
            showError('Error durante la sincronización');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión durante la sincronización');
    }
}
