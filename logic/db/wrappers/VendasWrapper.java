package gestorInventario.logic.db.wrappers;

import gestorInventario.logic.db.DB;
import gestorInventario.logic.model.User;
import gestorInventario.logic.model.Venda;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import gestorInventario.logic.model.Venda;

import java.sql.SQLException;

public class VendasWrapper {
    private final DB db;

    public VendasWrapper(DB db) {
        this.db = db;
    }

    public List<Venda> getByNif(Integer nif){
        try{
            return db.listVendasByNif(nif);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            return null;
        }
    }

    public List<Venda> getByNifSellerPrice(String nif, User seller, int priceOrder, Timestamp timestamp) {
        try{
            return db.listVendasByNifSellerSorting(nif, seller, priceOrder, timestamp);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            return null;
        }
    }

    public Venda createPurchase(Venda venda) {
        try {
            return db.createPurchase(venda);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
