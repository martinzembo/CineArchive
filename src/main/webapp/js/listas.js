// listas.js
console.log('listas.js loaded');

function addToListAjax(contenidoId, listaName) {
    fetch('/lista/add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `contenidoId=${encodeURIComponent(contenidoId)}&lista=${encodeURIComponent(listaName)}`
    })
    .then(resp => {
        if (resp.ok) return resp.json();
        throw new Error('Error agregando a la lista');
    })
    .then(data => alert('Agregado a ' + listaName))
    .catch(err => { console.error(err); alert('No se pudo agregar a la lista'); });
}

function removeFromListAjax(contenidoId, listaName) {
    fetch('/lista/remove', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `contenidoId=${encodeURIComponent(contenidoId)}&lista=${encodeURIComponent(listaName)}`
    })
    .then(resp => {
        if (resp.ok) return resp.json();
        throw new Error('Error removiendo de la lista');
    })
    .then(data => alert('Removido de ' + listaName))
    .catch(err => { console.error(err); alert('No se pudo remover de la lista'); });
}
