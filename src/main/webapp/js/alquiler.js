explanation: Implementar funciones mínimas para procesar formularios de alquiler y manejar redirección o mensajes.

// nuevo contenido
console.log('alquiler.js loaded');

function submitAlquilerForm(form) {
    const formData = new FormData(form);
    fetch('/alquilar', {
        method: 'POST',
        body: new URLSearchParams(formData)
    })
    .then(resp => {
        if (resp.ok) return resp.json();
        throw new Error('Error al procesar alquiler');
    })
    .then(data => {
        alert('Alquiler confirmado');
        window.location.href = '/mis-alquileres';
    })
    .catch(err => {
        console.error(err);
        alert('No se pudo completar el alquiler');
    });
    return false; // evitar submit tradicional
}

