// Muestra el resumen del pedido solo si los datos para el retiro están completos (HU-04)
document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('pedidoForm');
    if (!form) {
        return;
    }
    const btnRevisar = document.getElementById('btnRevisar');
    const modal = document.getElementById('resumenModal');

    btnRevisar.addEventListener('click', function () {
        // El navegador marca los campos obligatorios que falten
        if (!form.reportValidity()) {
            form.classList.add('was-validated');
            return;
        }
        const metodo = form.querySelector('input[name="metodoPago"]:checked');
        document.getElementById('resumenNombre').textContent = document.getElementById('pedidoNombre').value;
        document.getElementById('resumenTelefono').textContent = document.getElementById('pedidoTelefono').value;
        document.getElementById('resumenMetodo').textContent =
                metodo ? form.querySelector('label[for="' + metodo.id + '"]').textContent : '';
        bootstrap.Modal.getOrCreateInstance(modal).show();
    });
});
