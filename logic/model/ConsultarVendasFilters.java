package gestorInventario.logic.model;

import java.sql.Timestamp;
import java.util.Objects;

public class ConsultarVendasFilters {
    private String nif = "";
    private User seller = null;
    private int priceSorting = 0;
    private Timestamp timestamp = null;

    public ConsultarVendasFilters() {
    }
    public ConsultarVendasFilters(String nif, User seller, int priceSorting, Timestamp timestamp) {
        this.nif = nif;
        this.seller = seller;
        this.priceSorting = priceSorting;
        this.timestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getNif() {
        return nif;
    }

    public User getSeller() {
        return seller;
    }

    public int getPriceSorting() {
        return priceSorting;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsultarVendasFilters that = (ConsultarVendasFilters) o;
        return priceSorting == that.priceSorting && Objects.equals(nif, that.nif) && Objects.equals(seller, that.seller);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nif, seller, priceSorting);
    }
}
