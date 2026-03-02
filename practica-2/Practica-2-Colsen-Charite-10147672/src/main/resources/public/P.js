async function cargarProductos() {
  try {
    const response = await fetch("/api/productos");
    const productos = await response.json();

    const tbody = document.querySelector("table tbody");
    tbody.innerHTML = ""; // limpiar tabla

    productos.forEach(p => {
      const fila = document.createElement("tr");

      fila.innerHTML = `
        <td>${p.nombre}</td>
        <td>$${p.precio.toFixed(2)}</td>
        <td><input type="number" value="1" min="1" class="cantidad-input"></td>
        <td><button class="btn-agregar">Agregar</button></td>
      `;


      fila.querySelector(".btn-agregar").addEventListener("click", async () => {
        const cantidad = parseInt(fila.querySelector(".cantidad-input").value);

        if (cantidad < 1) {
          alert("La cantidad debe ser al menos 1");
          return;
        }

        const formData = new URLSearchParams();
        formData.append("producto", p.nombre);
        formData.append("cantidad", cantidad);

        try {
          const res = await fetch("/api/carrito/agregar", {
            method: "POST",
            body: formData
          });

          const data = await res.json();

          if (data.success) {

            actualizarContadorCarrito(data.carritoSize);
            alert(`${cantidad} x ${p.nombre} agregado al carrito`);
            fila.querySelector(".cantidad-input").value = 1;
          }
        } catch (error) {
          console.error("Error agregando al carrito:", error);
          alert("Error al agregar producto al carrito");
        }
      });

      tbody.appendChild(fila);
    });
  } catch (error) {
    console.error("Error cargando productos:", error);
    alert("Error al cargar productos");
  }
}


function actualizarContadorCarrito(cantidad) {
  const carritoLink = document.getElementById("carrito-link");
  if (carritoLink) {
    carritoLink.textContent = `Carrito (${cantidad})`;
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


document.addEventListener("DOMContentLoaded", async () => {
  await cargarContadorCarrito();


  await cargarProductos();
});