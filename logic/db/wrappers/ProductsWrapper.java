package gestorInventario.logic.db.wrappers;

import gestorInventario.logic.db.DB;
import gestorInventario.logic.model.ConsultarProdutosFilters;
import gestorInventario.logic.db.DBCondition;
import gestorInventario.logic.model.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductsWrapper {
    private final DB db;

    public ProductsWrapper(DB db) {
        this.db = db;
    }

    public Product getById(String id){
        try{
            return db.listProductById(id);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            return null;
        }
    }

    public List<Product> getAll(List<DBCondition> addProductConditions){
        try{
            return db.listProducts();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Product> getByFilters(ConsultarProdutosFilters consultarProdutosFilters) {
        try{
            return db.listProductsByFilters(
                    consultarProdutosFilters.getSearchBar(),
                    consultarProdutosFilters.getCategory(),
                    consultarProdutosFilters.getFornecedor(),
                    consultarProdutosFilters.getPriceSorting()
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Product create(Product product) {
        try{
            db.createProduct(product);
            return product;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean editProduct(Product product) {
        try {
            db.editProduct(product);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
