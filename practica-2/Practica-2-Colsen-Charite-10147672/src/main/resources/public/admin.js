// Verificar que el usuario sea admin
async function verificarAdmin() {
  try {
    const response = await fetch("/api/verificar-admin");
    const data = await response.json();

    if (!data.isAdmin) {
      alert("Acceso denegado. no eres admin");
      window.location.href = "productos.html";
    }
  } catch (error) {
    console.error("Error verificando permisos:", error);
    window.location.href = "login.html";
  }
}
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
async function cargarProductos() {
  try {
    const response = await fetch("/api/productos");
    const productos = await response.json();

    const tbody = document.querySelector("table tbody");
    tbody.innerHTML = ""; // Limpiar tabla

    productos.forEach(producto => {
      const fila = document.createElement("tr");

      fila.innerHTML = `
        <td>${producto.nombre}</td>
        <td>$${producto.precio.toFixed(2)}</td>
        <td>
          <button class="btn-eliminar" data-nombre="${producto.nombre}">Eliminar</button>
        </td>
      `;

      tbody.appendChild(fila);
    });

    // Agregar eventos a los botones de eliminar
    document.querySelectorAll(".btn-eliminar").forEach(btn => {
      btn.addEventListener("click", eliminarProducto);
    });

  } catch (error) {
    console.error("Error cargando productos:", error);
    alert("Error al cargar productos");
  }
}


async function agregarProducto(event) {
  event.preventDefault();

  const nombre = document.getElementById("nuevo-nombre").value.trim();
  const precio = parseFloat(document.getElementById("nuevo-precio").value);

  if (!nombre || precio <= 0) {
    alert("Por favor ingrese un nombre válido y un precio mayor a 0");
    return;
  }

  const formData = new URLSearchParams();
  formData.append("nombre", nombre);
  formData.append("precio", precio);

  try {
    const response = await fetch("/api/productos/crear", {
      method: "POST",
      body: formData
    });

    if (response.status === 403) {
      alert("Acceso denegado");
      return;
    }

    if (response.ok) {
      alert(`Producto "${nombre}" agregado exitosamente`);


      document.getElementById("nuevo-nombre").value = "";
      document.getElementById("nuevo-precio").value = "";


      await cargarProductos();
    } else {
      alert("Error al agregar producto");
    }

  } catch (error) {
    console.error("Error agregando producto:", error);
    alert("Error al agregar producto");
  }
}

async function eliminarProducto(event) {
  const nombreProducto = event.target.dataset.nombre;

  const confirmar = confirm(`¿Está seguro que desea eliminar "${nombreProducto}"?`);

  if (!confirmar) {
    return;
  }

  const formData = new URLSearchParams();
  formData.append("nombre", nombreProducto);

  try {
    const response = await fetch("/api/productos/eliminar", {
      method: "POST",
      body: formData
    });

    if (response.status === 403) {
      alert("Acceso denegado");
      return;
    }

    if (response.ok) {
      alert(`Producto "${nombreProducto}" eliminado exitosamente`);


      await cargarProductos();
    } else {
      alert("Error al eliminar producto");
    }

  } catch (error) {
    console.error("Error eliminando producto:", error);
    alert("Error al eliminar producto");
  }
}


document.addEventListener("DOMContentLoaded", async () => {
  await verificarAdmin();
  await cargarContadorCarrito();
  await cargarProductos();
  document.getElementById("form-agregar").addEventListener("submit", agregarProducto);
});