// funcion para hacer un preview de una imagen a partir de la ruta (URL) en la nube
function mostrarImagenUrl(input, previewId) {
    const preview = document.getElementById(previewId);
    const ruta = input.value.trim();
    if (ruta.startsWith('http')) {
        preview.src = ruta;
        preview.classList.remove('d-none');
    } else {
        preview.removeAttribute('src');
        preview.classList.add('d-none');
    }
}

//Para insertar información en el modal según el registro...
document.addEventListener('DOMContentLoaded', function () {
    const confirmModal = document.getElementById('confirmModal');
    if (!confirmModal) {
        return;
    }
    confirmModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        document.getElementById('modalId').value = button.getAttribute('data-bs-id');
        document.getElementById('modalDescripcion').textContent = button.getAttribute('data-bs-descripcion');
    });
});

//Para quitar toast
document.addEventListener('DOMContentLoaded', function () {
    setTimeout(() => {
        document.querySelectorAll('.toast').forEach(toast =>
            toast.classList.remove('show'));
    }, 4000);
});
