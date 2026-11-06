// Carga los datos del usuario en el modal Agregar o Editar
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('usuarioModal');
    if (!modal) {
        return;
    }
    const form = document.getElementById('usuarioForm');
    const header = document.getElementById('usuarioModalHeader');
    const titulo = document.getElementById('usuarioModalTitulo');
    const id = document.getElementById('usuarioId');
    const nombre = document.getElementById('usuarioNombre');
    const username = document.getElementById('usuarioUsername');
    const clave = document.getElementById('usuarioClave');
    const claveAyuda = document.getElementById('usuarioClaveAyuda');
    const rol = document.getElementById('usuarioRol');
    const activo = document.getElementById('usuarioActivo');

    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        if (!button) {
            return;
        }
        const modo = button.getAttribute('data-modo');
        // Se limpia cualquier información de una apertura anterior
        form.reset();
        id.value = '';
        clave.value = '';
        titulo.textContent = button.getAttribute('data-titulo') || '';
        header.classList.remove('bg-info', 'bg-warning', 'text-white');
        if (modo === 'editar') {
            id.value = button.getAttribute('data-id') || '';
            nombre.value = button.getAttribute('data-nombre') || '';
            username.value = button.getAttribute('data-username') || '';
            rol.value = button.getAttribute('data-rol') || '';
            activo.checked = button.getAttribute('data-activo') === 'true';
            // Al modificar, la contraseña es opcional: vacía conserva la anterior
            clave.required = false;
            claveAyuda.classList.remove('d-none');
            header.classList.add('bg-warning');
        } else {
            // Un usuario nuevo inicia activo y debe tener contraseña
            activo.checked = true;
            clave.required = true;
            claveAyuda.classList.add('d-none');
            header.classList.add('bg-info', 'text-white');
        }
    });
});
