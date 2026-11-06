// Carga los datos del producto en el modal Agregar o Editar
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('productoModal');
    if (!modal) {
        return;
    }
    const form = document.getElementById('productoForm');
    const header = document.getElementById('productoModalHeader');
    const titulo = document.getElementById('productoModalTitulo');
    const id = document.getElementById('productoId');
    const categoria = document.getElementById('productoCategoria');
    const descripcion = document.getElementById('productoDescripcion');
    const detalle = document.getElementById('productoDetalle');
    const precio = document.getElementById('productoPrecio');
    const existencias = document.getElementById('productoExistencias');
    const stockMinimo = document.getElementById('productoStockMinimo');
    const tipoAlerta = document.getElementById('productoTipoAlerta');
    const activo = document.getElementById('productoActivo');
    const rutaImagen = document.getElementById('productoRutaImagen');

    rutaImagen.addEventListener('change', function () {
        mostrarImagenUrl(this, 'productoPreview');
    });

    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        if (!button) {
            return;
        }
        const modo = button.getAttribute('data-modo');
        // Limpia información de una apertura anterior
        form.reset();
        id.value = '';
        rutaImagen.value = '';
        titulo.textContent = button.getAttribute('data-titulo') || '';
        header.classList.remove('bg-info', 'bg-warning', 'text-white');
        if (modo === 'editar') {
            id.value = button.getAttribute('data-id') || '';
            categoria.value = button.getAttribute('data-categoria') || '';
            descripcion.value = button.getAttribute('data-descripcion') || '';
            detalle.value = button.getAttribute('data-detalle') || '';
            precio.value = button.getAttribute('data-precio') || '';
            existencias.value = button.getAttribute('data-existencias') || '';
            stockMinimo.value = button.getAttribute('data-stockminimo') || '';
            tipoAlerta.value = button.getAttribute('data-tipoalerta') || 'Media';
            activo.checked = button.getAttribute('data-activo') === 'true';
            rutaImagen.value = button.getAttribute('data-imagen') || '';
            header.classList.add('bg-warning');
        } else {
            // Un producto nuevo inicia activo, con stock mínimo 5 y alerta media
            activo.checked = true;
            stockMinimo.value = 5;
            tipoAlerta.value = 'Media';
            header.classList.add('bg-info', 'text-white');
        }
        mostrarImagenUrl(rutaImagen, 'productoPreview');
    });
});
