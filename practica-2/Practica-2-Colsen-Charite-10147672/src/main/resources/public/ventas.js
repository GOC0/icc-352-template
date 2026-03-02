
async function cargarContadorCarrito() {
  try {
    const res = await fetch("/api/carrito/cantidad");
    const data = await res.json();
    actualizarContadorCarrito(data.totalItems);
  } catch (error) {
    console.error("Error cargando contador del carrito:", error);
    actualizarContadorCarrito(0);
  }
}
function actualizarContadorCarrito(cantidad) {
  const carritoLink = document.getElementById("carrito-link");
  if (carritoLink) {
    carritoLink.textContent = `Carrito (${cantidad})`;
  }
}

async function cargarVentas() {
  try {
    const response = await fetch("/api/ventas");
    const ventas = await response.json();

    if (ventas.length === 0) {
      document.getElementById("ventas-vacio").style.display = "block";
      document.getElementById("ventas-contenido").style.display = "none";
      return;
    }

    document.getElementById("ventas-vacio").style.display = "none";
    document.getElementById("ventas-contenido").style.display = "block";

    const tbody = document.querySelector("table tbody");
    tbody.innerHTML = ""; // Limpiar tabla

    ventas.forEach(venta => {
      const fila = document.createElement("tr");

      const fecha = new Date(venta.fecha);
      const fechaFormateada = fecha.toLocaleDateString('es-DO', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });

      let productosTexto = venta.productos.map(p =>
        `${p.producto} (x${p.cantidad})`
      ).join(', ');

      fila.innerHTML = `
        <td>${fechaFormateada}</td>
        <td>${productosTexto}</td>
        <td>${venta.cantidadTotal}</td>
        <td>$${venta.total.toFixed(2)}</td>
      `;

      tbody.appendChild(fila);
    });

  } catch (error) {
    console.error("Error cargando ventas:", error);
    alert("Error al cargar las ventas");
  }
}

document.addEventListener("DOMContentLoaded", async () => {
  await cargarContadorCarrito();
  await cargarVentas();
});