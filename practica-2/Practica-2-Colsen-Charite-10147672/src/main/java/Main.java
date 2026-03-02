import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    static List<Usuario> usuarios = new ArrayList<>();
    static List<Producto> productos = new ArrayList<>();
    static List<Map<String, Object>> ventas = new ArrayList<>(); // Cambiar a Map

    public static void main(String[] args) {

        usuarios.add(new Usuario("admin", "admin", true));

        productos.add(new Producto("RAM 8GB", 1500));
        productos.add(new Producto("Computadora", 5000));
        productos.add(new Producto("Laptop", 6000));

        var app = Javalin.create(config -> {
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/public";
                staticFileConfig.location = Location.CLASSPATH;
            });
        }).start(8000);


        app.before("/*", ctx -> {
            String path = ctx.path();
            if (path.equals("/login") ||
                    path.equals("/procesar-login") ||
                    path.endsWith(".html") ||
                    path.endsWith(".css") ||
                    path.endsWith(".js") ||
                    path.startsWith("/images") ||
                    path.startsWith("/api")) {
                return;
            }
            Usuario usuario = ctx.sessionAttribute("usuario");
            if (usuario == null) {
                ctx.redirect("/productos.html");
            }
        });


        app.get("/api/productos", ctx -> ctx.json(productos));

        app.get("/api/verificar-admin", ctx -> {
            Usuario usuario = ctx.sessionAttribute("usuario");
            boolean isAdmin = usuario != null && usuario.isAdmin();
            ctx.json(Map.of("isAdmin", isAdmin));
        });


        app.post("/api/carrito/agregar", ctx -> {
            String nombreProducto = ctx.formParam("producto");
            int cantidad = Integer.parseInt(ctx.formParam("cantidad"));

            List<Map<String, Object>> carrito = ctx.sessionAttribute("carrito");
            if (carrito == null) {
                carrito = new ArrayList<>();
            }

            boolean encontrado = false;
            for (Map<String, Object> item : carrito) {
                if (item.get("producto").equals(nombreProducto)) {
                    int cantidadActual = (int) item.get("cantidad");
                    item.put("cantidad", cantidadActual + cantidad);
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                Map<String, Object> item = new HashMap<>();
                item.put("producto", nombreProducto);
                item.put("cantidad", cantidad);
                carrito.add(item);
            }

            ctx.sessionAttribute("carrito", carrito);

            int totalItems = carrito.stream()
                    .mapToInt(item -> (int) item.get("cantidad"))
                    .sum();

            ctx.json(Map.of("carritoSize", totalItems, "success", true));
        });

        app.get("/api/carrito", ctx -> {
            List<Map<String, Object>> carrito = ctx.sessionAttribute("carrito");
            if (carrito == null) {
                carrito = new ArrayList<>();
            }
            ctx.json(carrito);
        });

        app.get("/api/carrito/cantidad", ctx -> {
            List<Map<String, Object>> carrito = ctx.sessionAttribute("carrito");
            int totalItems = 0;
            if (carrito != null) {
                totalItems = carrito.stream()
                        .mapToInt(item -> (int) item.get("cantidad"))
                        .sum();
            }
            ctx.json(Map.of("totalItems", totalItems));
        });


        app.post("/api/carrito/vaciar", ctx -> {
            List<Map<String, Object>> carrito = ctx.sessionAttribute("carrito");

            if (carrito != null && !carrito.isEmpty()) {
                double total = 0;
                int cantidadTotal = 0;
                List<Map<String, Object>> productosVenta = new ArrayList<>();

                for (Map<String, Object> item : carrito) {
                    String nombreProducto = (String) item.get("producto");
                    int cantidad = (int) item.get("cantidad");


                    Producto producto = productos.stream()
                            .filter(p -> p.getNombre().equals(nombreProducto))
                            .findFirst()
                            .orElse(null);

                    if (producto != null) {
                        double subtotal = producto.getPrecio() * cantidad;
                        total += subtotal;
                        cantidadTotal += cantidad;

                        Map<String, Object> productoVenta = new HashMap<>();
                        productoVenta.put("producto", nombreProducto);
                        productoVenta.put("cantidad", cantidad);
                        productoVenta.put("precioUnitario", producto.getPrecio());
                        productoVenta.put("subtotal", subtotal);
                        productosVenta.add(productoVenta);
                    }
                }


                Map<String, Object> venta = new HashMap<>();
                venta.put("fecha", LocalDateTime.now().toString());
                venta.put("productos", productosVenta);
                venta.put("cantidadTotal", cantidadTotal);
                venta.put("total", total);

                ventas.add(venta);
            }


            ctx.sessionAttribute("carrito", new ArrayList<>());
            ctx.json(Map.of("success", true));
        });


        app.get("/api/ventas", ctx -> {
            ctx.json(ventas);
        });


        app.post("/api/productos/crear", ctx -> {
            Usuario usuario = ctx.sessionAttribute("usuario");
            if (usuario == null || !usuario.isAdmin()) {
                ctx.status(403).result("Acceso denegado");
                return;
            }
            String nombre = ctx.formParam("nombre");
            double precio = Double.parseDouble(ctx.formParam("precio"));
            productos.add(new Producto(nombre, precio));
            ctx.json(productos);
        });

        app.post("/api/productos/eliminar", ctx -> {
            Usuario usuario = ctx.sessionAttribute("usuario");
            if (usuario == null || !usuario.isAdmin()) {
                ctx.status(403).result("Acceso denegado");
                return;
            }
            String nombre = ctx.formParam("nombre");
            productos.removeIf(p -> p.getNombre().equals(nombre));
            ctx.json(productos);
        });

        // Login
        app.post("/procesar-login", ctx -> {
            String nombre = ctx.formParam("usuario");
            String contrasena = ctx.formParam("password");
            if ("admin".equals(nombre) && "admin".equals(contrasena)) {
                ctx.sessionAttribute("usuario", new Usuario(nombre, contrasena, true));
                ctx.redirect("/productos.html");
            } else {
                ctx.redirect("/login.html");
            }
        });

        // Main endpoints
        app.get("/", ctx -> ctx.redirect("/productos.html"));
        app.get("/login", ctx -> ctx.redirect("/login.html"));
    }
}