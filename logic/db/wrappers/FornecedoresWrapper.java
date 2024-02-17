package gestorInventario.logic.db.wrappers;

import gestorInventario.logic.db.DB;
import gestorInventario.logic.model.Fornecedor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FornecedoresWrapper {
    private final DB db;

    public FornecedoresWrapper(DB db) {
        this.db = db;
    }

    public List<Fornecedor> getAll() {
        try{
            return db.listFornecedores();
        }catch(SQLException throwables){
            throwables.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Fornecedor getById(Integer id){
        try{
            return db.listFornecedorById(id);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            return null;
        }
    }
}
