// Cargar productos disponibles para obtener precios
let productosDisponibles = [];

async function cargarProductosDisponibles() {
  try {
    const response = await fetch("/api/productos");
    productosDisponibles = await response.json();
  } catch (error) {
    console.error("Error cargando productos:", error);
  }
}

// Obtener precio de un producto por nombre
function obtenerPrecioProducto(nombreProducto) {
  const producto = productosDisponibles.find(p => p.nombre === nombreProducto);
  return producto ? producto.precio : 0;
}

// Cargar el carrito desde el servidor
async function cargarCarrito() {
  try {
    const response = await fetch("/api/carrito");
    const carrito = await response.json();

    if (carrito.length === 0) {
      // Mostrar mensaje de carrito vacío
      document.getElementById("carrito-vacio").style.display = "block";
      document.getElementById("carrito-contenido").style.display = "none";
      actualizarContadorCarrito(0);
      return;
    }

    // Mostrar contenido del carrito
    document.getElementById("carrito-vacio").style.display = "none";
    document.getElementById("carrito-contenido").style.display = "block";

    const tbody = document.querySelector("table tbody");
    tbody.innerHTML = ""; // Limpiar tabla

    let totalGeneral = 0;
    let totalItems = 0;

    carrito.forEach(item => {
      const precioUnitario = obtenerPrecioProducto(item.producto);
      const cantidad = item.cantidad;
      const subtotal = precioUnitario * cantidad;
      totalGeneral += subtotal;
      totalItems += cantidad;

      const fila = document.createElement("tr");
      fila.innerHTML = `
        <td>${item.producto}</td>
        <td>$${precioUnitario.toFixed(2)}</td>
        <td>${cantidad}</td>
        <td>$${subtotal.toFixed(2)}</td>
      `;

      tbody.appendChild(fila);
    });

    // Actualizar total
    document.getElementById("total-precio").textContent = totalGeneral.toFixed(2);

    // Actualizar contador del carrito
    actualizarContadorCarrito(totalItems);

  } catch (error) {
    console.error("Error cargando carrito:", error);
    alert("Error al cargar el carrito");
  }
}

// Actualizar el contador del carrito en la navegación
function actualizarContadorCarrito(cantidad) {
  const carritoLink = document.getElementById("carrito-link");
  if (carritoLink) {
    carritoLink.textContent = `Carrito (${cantidad})`;
  }
}

// Procesar la compra
async function procesarCompra() {
  try {
    const response = await fetch("/api/carrito");
    const carrito = await response.json();

    if (carrito.length === 0) {
      alert("El carrito está vacío");
      return;
    }

    // Aquí puedes agregar la lógica para procesar la compra
    // Por ahora, solo mostramos confirmación y vaciamos el carrito

    const totalItems = carrito.reduce((sum, item) => sum + item.cantidad, 0);
    let totalPrecio = 0;

    carrito.forEach(item => {
      const precio = obtenerPrecioProducto(item.producto);
      totalPrecio += precio * item.cantidad;
    });

    const confirmar = confirm(
      `¿Confirmar compra?\n\n` +
      `Total de productos: ${totalItems}\n` +
      `Total a pagar: $${totalPrecio.toFixed(2)}`
    );

    if (confirmar) {
      // Vaciar el carrito en el servidor
      const vaciarResponse = await fetch("/api/carrito/vaciar", {
        method: "POST"
      });

      if (vaciarResponse.ok) {
        alert("¡Compra procesada exitosamente!");
        window.location.href = "productos.html";
      }
    }

  } catch (error) {
    console.error("Error procesando compra:", error);
    alert("Error al procesar la compra");
  }
}

// Event listeners
document.addEventListener("DOMContentLoaded", async () => {
  // Cargar productos disponibles primero (para obtener precios)
  await cargarProductosDisponibles();

  // Luego cargar el carrito
  await cargarCarrito();

  // Asignar evento al botón de procesar compra
  const btnProcesar = document.getElementById("btn-procesar-compra");
  if (btnProcesar) {
    btnProcesar.addEventListener("click", procesarCompra);
  }
});