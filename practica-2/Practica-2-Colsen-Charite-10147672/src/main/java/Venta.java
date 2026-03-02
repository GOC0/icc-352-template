import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Venta {
    private LocalDateTime fecha;
    private List<Map<String, Object>> productos;
    private int cantidadTotal;
    private double total;

    public Venta(LocalDateTime fecha, List<Map<String, Object>> productos, int cantidadTotal, double total) {
        this.fecha = fecha;
        this.productos = productos;
        this.cantidadTotal = cantidadTotal;
        this.total = total;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public List<Map<String, Object>> getProductos() {
        return productos;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public double getTotal() {
        return total;
    }
}