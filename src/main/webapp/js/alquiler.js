// alquiler.js
// Manejo de formularios y acciones de alquiler sin depender de JSON

console.log('alquiler.js loaded');

// Adjunta manejador al formulario de alquiler en detalle.jsp (si existe)
document.addEventListener('DOMContentLoaded', () => {
  const forms = document.querySelectorAll('form[action="/alquilar"][method="post"]');
  forms.forEach(form => {
    form.addEventListener('submit', onAlquilerSubmit);
  });
});

async function onAlquilerSubmit(event) {
  event.preventDefault();
  const form = event.target;
  const params = new URLSearchParams(new FormData(form));
  try {
    const resp = await fetch('/alquilar', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: params.toString()
    });

    // El controlador devuelve redirect:/mis-alquileres
    if (resp.redirected) {
      window.location.href = resp.url;
      return;
    }
    if (resp.ok) {
      window.location.href = '/mis-alquileres';
    } else {
      alert('No se pudo confirmar el alquiler. Intenta nuevamente.');
    }
  } catch (err) {
    console.error('Error de red al alquilar:', err);
    alert('Error de red al procesar el alquiler');
  }
}

// Utilidad para alquilar programáticamente (por ejemplo, desde botones en catálogo)
// Nota: no espera JSON; redirige a mis-alquileres en éxito
export async function alquilar(contenidoId, periodo = 3) {
  try {
    const resp = await fetch('/alquilar', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: `contenidoId=${encodeURIComponent(contenidoId)}&periodo=${encodeURIComponent(periodo)}`
    });
    if (resp.redirected) {
      window.location.href = resp.url;
      return;
    }
    if (resp.ok) {
      window.location.href = '/mis-alquileres';
    } else {
      alert('No se pudo realizar el alquiler');
    }
  } catch (e) {
    console.error(e);
    alert('Error de red al realizar el alquiler');
  }
}
