// Función para cargar las categorías
document.addEventListener('DOMContentLoaded', function() {
    cargarCategorias();
});

function cargarCategorias() {
    // Obtener el select de géneros
    const selectGeneros = document.getElementById('generos');

    // Llamar al endpoint para obtener los géneros
    fetch('/cinearchive/api/categorias/tipo/GENERO')
        .then(response => response.json())
        .then(generos => {
            // Limpiar las opciones existentes excepto la primera (Todos los géneros)
            while (selectGeneros.options.length > 1) {
                selectGeneros.remove(1);
            }

            // Agregar las nuevas opciones
            generos.forEach(genero => {
                const option = document.createElement('option');
                option.value = genero.nombre.toLowerCase();
                option.textContent = genero.nombre;
                selectGeneros.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error al cargar los géneros:', error);
        });
}

// Función para filtrar contenido por género
document.getElementById('generos').addEventListener('change', function() {
    const generoSeleccionado = this.value;
    if (generoSeleccionado) {
        // Aquí iría la lógica para filtrar el contenido por género
        console.log('Filtrando por género:', generoSeleccionado);
    }
});
