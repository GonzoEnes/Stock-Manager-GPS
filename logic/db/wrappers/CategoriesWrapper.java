package gestorInventario.logic.db.wrappers;

import gestorInventario.logic.db.DB;
import gestorInventario.logic.model.Category;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriesWrapper {
    private DB db;

    public CategoriesWrapper(DB db) { this.db = db; }

    public List<Category> getAll() {
        try {
            return db.listCategories();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new ArrayList<>();
        }
    }
}
