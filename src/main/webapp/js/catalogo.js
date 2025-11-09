// Placeholder JS for catalog interactions
console.log('catalogo.js loaded');

function rentNow(contenidoId) {
    // Redirigir al detalle o disparar un alquiler rápido
    const url = `/alquilar`;
    fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `contenidoId=${encodeURIComponent(contenidoId)}&periodo=3`
    })
    .then(resp => {
        if (resp.ok) return resp.json();
        throw new Error('Error en la petición de alquiler');
    })
    .then(data => {
        alert('Alquiler realizado correctamente');
        // opción: redirigir a mis-alquileres
        window.location.href = '/mis-alquileres';
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
