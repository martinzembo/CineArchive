// Toggle menu móvil
document.addEventListener('DOMContentLoaded', function() {
    const menuToggle = document.querySelector('.menu-toggle');
    const navLinks = document.querySelector('.nav-links');

    if (menuToggle) {
        menuToggle.addEventListener('click', function() {
            navLinks.classList.toggle('active');
        });
    }

    // Sistema de calificación con estrellas
    const starsInput = document.querySelector('.stars-input');
    if (starsInput) {
        starsInput.addEventListener('click', function(e) {
            const rect = this.getBoundingClientRect();
            const clickX = e.clientX - rect.left;
            const starWidth = rect.width / 5;
            const rating = Math.ceil(clickX / starWidth);

            let stars = '';
            for (let i = 0; i < 5; i++) {
                stars += i < rating ? '★' : '☆';
            }
            this.textContent = stars;
            this.dataset.rating = rating;
        });
    }

    // Selección de opciones de alquiler
    const rentalOptions = document.querySelectorAll('.rental-option');
    rentalOptions.forEach(option => {
        option.addEventListener('click', function() {
            rentalOptions.forEach(opt => opt.classList.remove('selected'));
            this.classList.add('selected');
            const radio = this.querySelector('input[type="radio"]');
            if (radio) radio.checked = true;
        });
    });

    // Smooth scroll para movie-row
    const movieRows = document.querySelectorAll('.movie-row');
    movieRows.forEach(row => {
        let isDown = false;
        let startX;
        let scrollLeft;

        row.addEventListener('mousedown', (e) => {
            isDown = true;
            row.style.cursor = 'grabbing';
            startX = e.pageX - row.offsetLeft;
            scrollLeft = row.scrollLeft;
        });

        row.addEventListener('mouseleave', () => {
            isDown = false;
            row.style.cursor = 'grab';
        });

        row.addEventListener('mouseup', () => {
            isDown = false;
            row.style.cursor = 'grab';
        });

        row.addEventListener('mousemove', (e) => {
            if (!isDown) return;
            e.preventDefault();
            const x = e.pageX - row.offsetLeft;
            const walk = (x - startX) * 2;
            row.scrollLeft = scrollLeft - walk;
        });
    });

    // Confirmación para acciones de eliminación
    const deleteButtons = document.querySelectorAll('[title="Eliminar"], .btn-remove');
    deleteButtons.forEach(btn => {
        btn.addEventListener('click', function(e) {
            if (!confirm('¿Estás seguro de que deseas eliminar este elemento?')) {
                e.preventDefault();
                e.stopPropagation();
            }
        });
    });

    // Mensajes de éxito para acciones
    function showMessage(message, type = 'success') {
        const messageDiv = document.createElement('div');
        messageDiv.className = `notification notification-${type}`;
        messageDiv.textContent = message;
        messageDiv.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 15px 25px;
            background-color: ${type === 'success' ? '#28a745' : '#e50914'};
            color: white;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.3);
            z-index: 1000;
            animation: slideIn 0.3s ease;
        `;
        document.body.appendChild(messageDiv);

        setTimeout(() => {
            messageDiv.style.animation = 'slideOut 0.3s ease';
            setTimeout(() => messageDiv.remove(), 300);
        }, 3000);
    }

    // Re-exponer showMessage como window.showToast si no existe (para integraciones de listas/alquiler)
    if (typeof window.showToast !== 'function') {
        window.showToast = function(message, type='info'){ showMessage(message, type === 'error' ? 'error' : (type==='success'?'success':'info')); };
    }

    // Agregar a favoritos
    const addToFavButtons = document.querySelectorAll('.btn-add');
    addToFavButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            const movieTitle = this.closest('.movie-card').querySelector('.movie-title').textContent;
            showMessage(`"${movieTitle}" agregado a tu lista ✓`);
        });
    });

    // Botón de alquilar
    const rentButtons = document.querySelectorAll('.rent-btn-large');
    rentButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            const movieTitle = this.closest('.detail-info')?.querySelector('h1')?.textContent || 'Película';
            if (confirm(`¿Deseas alquilar "${movieTitle}"?`)) {
                showMessage(`¡"${movieTitle}" alquilado exitosamente! 🎬`);
                setTimeout(() => {
                    window.location.href = 'Index.html';
                }, 2000);
            }
        });
    });

    // Validación de formularios
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const inputs = form.querySelectorAll('input[required]');
            let isValid = true;

            inputs.forEach(input => {
                if (!input.value.trim()) {
                    isValid = false;
                    input.style.borderColor = '#dc3545';
                } else {
                    input.style.borderColor = '';
                }
            });

            if (!isValid) {
                e.preventDefault();
                showMessage('Por favor completa todos los campos requeridos', 'error');
            }
        });
    });

    // Búsqueda en tiempo real (simulada)
    const searchInputs = document.querySelectorAll('.search-input');
    searchInputs.forEach(input => {
        let timeout;
        input.addEventListener('input', function() {
            clearTimeout(timeout);
            const searchTerm = this.value.toLowerCase();

            if (searchTerm.length > 2) {
                timeout = setTimeout(() => {
                    console.log('Buscando:', searchTerm);
                    // Aquí iría la lógica de búsqueda real
                }, 500);
            }
        });
    });

    // Tabs para formularios
    const tabButtons = document.querySelectorAll('.tab-btn');
    tabButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            tabButtons.forEach(b => b.classList.remove('active'));
            this.classList.add('active');
        });
    });

    // Animaciones CSS
    const style = document.createElement('style');
    style.textContent = `
        @keyframes slideIn {
            from {
                transform: translateX(100%);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
        }

        @keyframes slideOut {
            from {
                transform: translateX(0);
                opacity: 1;
            }
            to {
                transform: translateX(100%);
                opacity: 0;
            }
        }

        .movie-row {
            cursor: grab;
        }

        .movie-row:active {
            cursor: grabbing;
        }
    `;
    document.head.appendChild(style);

    // Contador de caracteres para textarea
    const textareas = document.querySelectorAll('textarea');
    textareas.forEach(textarea => {
        const maxLength = 500;
        const counter = document.createElement('div');
        counter.style.cssText = 'text-align: right; color: #888; font-size: 12px; margin-top: 5px;';
        counter.textContent = `0 / ${maxLength}`;
        textarea.setAttribute('maxlength', maxLength);
        textarea.parentNode.insertBefore(counter, textarea.nextSibling);

        textarea.addEventListener('input', function() {
            counter.textContent = `${this.value.length} / ${maxLength}`;
        });
    });

    // Filtros dinámicos
    const filterSelects = document.querySelectorAll('.filter-select');
    filterSelects.forEach(select => {
        select.addEventListener('change', function() {
            console.log('Filtro aplicado:', this.value);
            // Aquí iría la lógica de filtrado real
            showMessage('Filtros aplicados ✓');
        });
    });

    // Animación de carga para tablas
    const tables = document.querySelectorAll('.admin-table tbody');
    tables.forEach(tbody => {
        const rows = tbody.querySelectorAll('tr');
        rows.forEach((row, index) => {
            row.style.opacity = '0';
            row.style.animation = `fadeIn 0.3s ease forwards ${index * 0.05}s`;
        });
    });

    // Añadir animación fadeIn
    const fadeInStyle = document.createElement('style');
    fadeInStyle.textContent = `
        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
    `;
    document.head.appendChild(fadeInStyle);

    // Modo de visualización para reportes
    const reportGenerator = document.querySelector('.report-generator');
    if (reportGenerator) {
        const generateBtn = reportGenerator.querySelector('.btn-primary');
        if (generateBtn) {
            generateBtn.addEventListener('click', function() {
                showMessage('Generando reporte... ⏳');
                setTimeout(() => {
                    showMessage('Reporte generado exitosamente 📊');
                }, 2000);
            });
        }
    }

    // Tooltips simples
    document.querySelectorAll('[title]').forEach(element => {
        element.addEventListener('mouseenter', function(e) {
            const tooltip = document.createElement('div');
            tooltip.className = 'tooltip';
            tooltip.textContent = this.getAttribute('title');
            tooltip.style.cssText = `
                position: absolute;
                background-color: #333;
                color: white;
                padding: 5px 10px;
                border-radius: 4px;
                font-size: 12px;
                z-index: 1000;
                pointer-events: none;
                white-space: nowrap;
            `;
            document.body.appendChild(tooltip);

            const rect = this.getBoundingClientRect();
            tooltip.style.left = rect.left + (rect.width / 2) - (tooltip.offsetWidth / 2) + 'px';
            tooltip.style.top = rect.top - tooltip.offsetHeight - 5 + 'px';

            this.addEventListener('mouseleave', () => tooltip.remove(), { once: true });
        });
    });
});

console.log('🎬 CineArchive - Sistema cargado correctamente');
