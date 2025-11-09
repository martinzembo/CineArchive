// Placeholder JS for catalog interactions
console.log('catalogo.js loaded');

function rentNow(contenidoId) {
    // Usar la utilidad global de alquiler para mantener comportamiento consistente
    if (typeof window.alquilar === 'function') {
        window.alquilar(contenidoId, 3);
        return;
    }
    // Fallback: POST directo
    fetch('/alquilar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `contenidoId=${encodeURIComponent(contenidoId)}&periodo=3`
    })
    .then(resp => {
        if (resp.redirected) { window.location.href = resp.url; return; }
        if (resp.ok) { window.location.href = '/mis-alquileres'; return; }
        throw new Error('Error en la petición de alquiler');
    })
    .catch(err => {
        console.error(err);
        alert('No se pudo realizar el alquiler');
    });
}

function addToList(contenidoId, lista) {
    const url = `/lista/add`;
    fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `contenidoId=${encodeURIComponent(contenidoId)}&lista=${encodeURIComponent(lista)}`
    })
    .then(resp => {
        if (resp.ok) return resp.json();
        throw new Error('Error agregando a la lista');
    })
    .then(data => {
        alert('Agregado a ' + lista);
    })
    .catch(err => {
        console.error(err);
        alert('No se pudo agregar a la lista');
    });
}
