package gestorInventario.logic.model;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class Venda {
    private int id;
    private User seller;
    private String nif;
    private String nomeCliente;
    private float total;
    private Timestamp timestamp;
    private List<Product> products;

    public Venda(int id, User seller, String nif, String nomeCliente, float total) {
        this.id = id;
        this.seller = seller;
        this.total = total;
        this.nomeCliente = nomeCliente;
        this.nif = nif;
    }
    
    public Venda(int id, User seller, String nif, String nomeCliente, float total, Timestamp timestamp, List<Product> products) {
        this.id = id;
        this.seller = seller;
        this.nif = nif;
        this.nomeCliente = nomeCliente;
        this.total = total;
        this.timestamp = timestamp;
        this.products = products;
    }

    public Venda(User seller, String nif, String nomeCliente, float total, Timestamp timestamp, List<Product> products) {
        this.seller = seller;
        this.nif = nif;
        this.nomeCliente = nomeCliente;
        this.total = total;
        this.timestamp = timestamp;
        this.products = products;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setTotal(float total) { this.total = total; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSeller() {
        return seller;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getNif() {
        return nif;
    }

    public float getTotal() {
        return total;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venda venda = (Venda) o;
        return id == venda.id && Float.compare(venda.total, total) == 0 && seller.equals(venda.seller) && Objects.equals(nif, venda.nif) && Objects.equals(nomeCliente, venda.nomeCliente) && Objects.equals(timestamp, venda.timestamp) && products.equals(venda.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seller, nif, nomeCliente, total, products);
    }
}
