// Carga los datos de la categoría en el modal Agregar o Editar
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('categoriaModal');
    if (!modal) {
        return;
    }
    const form = document.getElementById('categoriaForm');
    const header = document.getElementById('categoriaModalHeader');
    const titulo = document.getElementById('categoriaModalTitulo');
    const id = document.getElementById('categoriaId');
    const descripcion = document.getElementById('categoriaDescripcion');
    const activo = document.getElementById('categoriaActivo');
    const rutaImagen = document.getElementById('categoriaRutaImagen');

    rutaImagen.addEventListener('change', function () {
        mostrarImagenUrl(this, 'categoriaPreview');
    });

    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        if (!button) {
            return;
        }
        const modo = button.getAttribute('data-modo');
        // Se limpia cualquier información de una apertura anterior
        form.reset();
        id.value = '';
        rutaImagen.value = '';
        titulo.textContent = button.getAttribute('data-titulo') || '';
        header.classList.remove('bg-info', 'bg-warning', 'text-white');
        if (modo === 'editar') {
            id.value = button.getAttribute('data-id') || '';
            descripcion.value = button.getAttribute('data-descripcion') || '';
            activo.checked = button.getAttribute('data-activo') === 'true';
            rutaImagen.value = button.getAttribute('data-imagen') || '';
            header.classList.add('bg-warning');
        } else {
            // Una categoría nueva inicia activa
            activo.checked = true;
            header.classList.add('bg-info', 'text-white');
        }
        mostrarImagenUrl(rutaImagen, 'categoriaPreview');
    });
});
