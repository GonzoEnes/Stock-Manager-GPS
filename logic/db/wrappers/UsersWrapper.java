package gestorInventario.logic.db.wrappers;

import gestorInventario.logic.db.DB;
import gestorInventario.logic.db.DBCondition;
import gestorInventario.logic.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersWrapper {
    private final DB db;

    public UsersWrapper(DB db) { this.db = db; }

    public User getById(int id) {
        try {
            return db.listUserById(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public List<User> getAll() {
        try {
            return db.listUsers();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<User> getBy(List<DBCondition> conditions) {
        try {
            return db.listUsersBy(conditions);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new ArrayList<>();
        }
    }

    public User create(String name, String email, String password, boolean isAdmin) {
        try {
            return db.createUser(name, email, password, isAdmin);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
