// alquiler.js
// Manejo de formularios y acciones de alquiler sin depender de JSON

console.log('alquiler.js loaded');

function endpoint(path) {
  if (window.APP_CTX && path.startsWith('/')) return window.APP_CTX + path;
  return path;
}

// Adjunta manejador al formulario de alquiler en detalle.jsp (si existe)
document.addEventListener('DOMContentLoaded', () => {
  const forms = document.querySelectorAll('form[action$="/alquilar"][method="post"]');
  forms.forEach(form => { form.addEventListener('submit', onAlquilerSubmit); });
});

async function onAlquilerSubmit(event) {
  event.preventDefault();
  const form = event.target;
  const params = new URLSearchParams(new FormData(form));
  // Asegurar método de pago por defecto si no viene
  if (!params.get('metodoPago')) {
    params.set('metodoPago', 'TARJETA');
  }
  try {
    const resp = await fetch(endpoint('/alquilar'), {
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
      window.location.href = endpoint('/mis-alquileres');
    } else {
      if (typeof window.showToast === 'function') showToast('No se pudo confirmar el alquiler', 'error');
    }
  } catch (err) {
    console.error('Error de red al alquilar:', err);
    if (typeof window.showToast === 'function') showToast('Error de red al procesar el alquiler', 'error');
  }
}

// Utilidad para alquilar programáticamente (por ejemplo, desde botones en catálogo)
// Nota: no espera JSON; redirige a mis-alquileres en éxito
async function alquilar(contenidoId, periodo = 3, metodoPago = 'TARJETA') {
  try {
    const resp = await fetch(endpoint('/alquilar'), {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: `contenidoId=${encodeURIComponent(contenidoId)}&periodo=${encodeURIComponent(periodo)}&metodoPago=${encodeURIComponent(metodoPago)}`
    });
    if (resp.redirected) {
      window.location.href = resp.url;
      return;
    }
    if (resp.ok) {
      window.location.href = endpoint('/mis-alquileres');
    } else {
      if (typeof window.showToast === 'function') showToast('No se pudo realizar el alquiler', 'error');
    }
  } catch (e) {
    console.error(e);
    if (typeof window.showToast === 'function') showToast('Error de red al realizar el alquiler', 'error');
  }
}

window.alquilar = alquilar;

// Autoslide para carruseles en detalle
(function setupDetailAutoSlide(){
  if (!document.body.classList.contains('detail-page')) return;
  const sliders = Array.from(document.querySelectorAll('.detail-slide'));
  if (!sliders.length) return;

  sliders.forEach(slider => {
    const state = { paused: false };
    let intervalId = null;
    const STEP_CARDS = 1;
    const TICK_MS = 4000;

    function cardMetrics(){
      const card = slider.querySelector('.movie-card');
      if (!card) return { w:0, gap:0 };
      const next = card.nextElementSibling;
      let gap = 20;
      if (next) gap = Math.max(0, next.getBoundingClientRect().left - card.getBoundingClientRect().right);
      return { w: card.offsetWidth, gap };
    }
    function visibleCards(){
      const { w, gap } = cardMetrics();
      if (!w) return 0;
      return Math.max(1, Math.floor((slider.clientWidth + gap) / (w + gap)));
    }
    function totalCards(){ return slider.querySelectorAll('.movie-card').length; }
    function shouldRun(){
      if (state.paused) return false;
      if (totalCards() <= visibleCards()) return false;
      return true;
    }
    function nextStep(){
      if (!shouldRun()) return;
      const { w, gap } = cardMetrics();
      const delta = (w + gap) * STEP_CARDS;
      const maxScroll = slider.scrollWidth - slider.clientWidth;
      let target = slider.scrollLeft + delta;
      if (target > maxScroll + 4) target = 0;
      slider.scrollTo({ left: target, behavior: 'smooth' });
    }
    function start(){ if (!intervalId) intervalId = setInterval(nextStep, TICK_MS); }
    function stop(){ if (intervalId){ clearInterval(intervalId); intervalId=null; } }

    function attachCardHover(){
      slider.querySelectorAll('.movie-card').forEach(card => {
        card.addEventListener('mouseenter', () => { state.paused = true; });
        card.addEventListener('mouseleave', () => { state.paused = false; });
      });
    }

    const mo = new MutationObserver(() => { attachCardHover(); });
    mo.observe(slider, { childList:true, subtree:true });
    attachCardHover();
    start();
  });
})();
