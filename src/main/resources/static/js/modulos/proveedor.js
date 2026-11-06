// Carga los datos del proveedor en el modal Agregar o Editar
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('proveedorModal');
    if (!modal) {
        return;
    }
    const form = document.getElementById('proveedorForm');
    const header = document.getElementById('proveedorModalHeader');
    const titulo = document.getElementById('proveedorModalTitulo');
    const id = document.getElementById('proveedorId');
    const nombre = document.getElementById('proveedorNombre');
    const contacto = document.getElementById('proveedorContacto');
    const telefono = document.getElementById('proveedorTelefono');
    const correo = document.getElementById('proveedorCorreo');
    const productos = document.getElementById('proveedorProductos');
    const activo = document.getElementById('proveedorActivo');

    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        if (!button) {
            return;
        }
        const modo = button.getAttribute('data-modo');
        // Se limpia cualquier información de una apertura anterior
        form.reset();
        id.value = '';
        titulo.textContent = button.getAttribute('data-titulo') || '';
        header.classList.remove('bg-info', 'bg-warning', 'text-white');
        if (modo === 'editar') {
            id.value = button.getAttribute('data-id') || '';
            nombre.value = button.getAttribute('data-nombre') || '';
            contacto.value = button.getAttribute('data-contacto') || '';
            telefono.value = button.getAttribute('data-telefono') || '';
            correo.value = button.getAttribute('data-correo') || '';
            productos.value = button.getAttribute('data-productos') || '';
            activo.checked = button.getAttribute('data-activo') === 'true';
            header.classList.add('bg-warning');
        } else {
            // Un proveedor nuevo inicia activo
            activo.checked = true;
            header.classList.add('bg-info', 'text-white');
        }
    });
});
