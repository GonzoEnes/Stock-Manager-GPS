package gestorInventario.logic.db;

import gestorInventario.logic.model.*;
import javafx.collections.FXCollections;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DB {
    private String DATABASE_URL = "jdbc:mysql://ugomes.com:3306/gps_gestor_inventario";
    private final String USERNAME = "pd_user";
    private final String PASSWORD = "123-sigaPD-321";
    private Connection connection;
    private boolean testing;

    public DB() throws SQLException {
        connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);         //Conexão com a base de dados
    }

    public DB(boolean testing) throws SQLException {
        this.testing = testing;
        if(this.testing)
            DATABASE_URL = "jdbc:mysql://ugomes.com:3306/gps_gestor_inventario_testes";
        connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);         //Conexão com a base de dados
    }

    public void close() throws SQLException {
        if (connection != null)
            connection.close();
    }

    public void resetTestDb() throws SQLException {
        if(!testing) return;
        clearDb();

        Category cat1 = new Category(4, "Memórias RAM");
        Category cat2 = new Category(5, "Motherboards");
        Category cat3 = new Category(6, "Discos");
        Category cat4 = new Category(7, "Teclados");
        Fornecedor forn1 = new Fornecedor(4, "Kingston");
        Fornecedor forn2 = new Fornecedor(5, "Samsung");
        Fornecedor forn3 = new Fornecedor(6, "PCDiga");
        List<Product> products = List.of(
                new Product(
                        "1111111111111",
                        "Memória RAM Kingston 16GB DDR4",
                        cat1,
                        85.0f,
                        15,
                        forn1
                ), new Product(
                        "2222222222222",
                        "Teclado Logitech",
                        cat4,
                        15.12f,
                        11,
                        forn3
                ), new Product(
                        "3333333333333",
                        "Teclado Corsair com luzinhas",
                        cat4,
                        85.00f,
                        5,
                        forn1
                ), new Product(
                        "9781108480722",
                        "Produto exemplo 4",
                        cat2,
                        21.00f,
                        11,
                        forn3
                )
        );
        User seller = new User(1, "admin", "admin@gmail.com", "123", true);
        User seller2 = new User(2, "normal user", "normal_user@gmail.com", "123", false);
        List<Venda> vendas = List.of(
                new Venda(seller, "", "", 127.0f, Timestamp.valueOf(LocalDateTime.of(2022, 1, 11, 0, 0, 0)), List.of(
                        new Product("9781108480722", 2),
                        new Product("1111111111111", 1)
                )),
                new Venda(seller2, "123321123", "Cliente 1", 85.0f, Timestamp.from(Instant.now()), List.of(
                        new Product("1111111111111", 1)
                )),
                new Venda(seller, "111111111", "Cliente 2", 45.36f, Timestamp.from(Instant.now()), List.of(
                        new Product("2222222222222", 3)
                ))
        );

        createUser("admin", "admin@gmail.com", "123", true);
        createUser("normal user", "normal_user@gmail.com", "123", false);
        createUser("Acceptance Tests", "tests_gps@gmail.com", "123", false);
        createCategory(cat1.getCategory());
        createCategory(cat2.getCategory());
        createCategory(cat3.getCategory());
        createCategory(cat4.getCategory());
        createFornecedor(forn1.getName());
        createFornecedor(forn2.getName());
        createFornecedor(forn3.getName());
        for(Product product : products)
            createProduct(product);

        for(Venda venda : vendas)
            createPurchase(venda);
    }

    public void clearDb() throws SQLException {
        if(!testing) return;

        List<String> deleteQueries = List.of("DELETE FROM gps_gestor_inventario_testes.produto_has_venda WHERE venda_prod_id > 0;",
                "DELETE FROM gps_gestor_inventario_testes.produto WHERE cod_barras > 0;",
                "DELETE FROM gps_gestor_inventario_testes.fornecedor WHERE id > 0;",
                "DELETE FROM gps_gestor_inventario_testes.category WHERE id > 0;",
                "DELETE FROM gps_gestor_inventario_testes.venda WHERE id > 0;",
                "DELETE FROM gps_gestor_inventario_testes.users WHERE id > 0;",
                "ALTER TABLE gps_gestor_inventario_testes.users AUTO_INCREMENT = 1;",
                "ALTER TABLE gps_gestor_inventario_testes.category AUTO_INCREMENT = 4;",
                "ALTER TABLE gps_gestor_inventario_testes.fornecedor AUTO_INCREMENT = 4;",
                "ALTER TABLE gps_gestor_inventario_testes.venda AUTO_INCREMENT = 1;",
                "ALTER TABLE gps_gestor_inventario_testes.produto_has_venda AUTO_INCREMENT = 1;");
        for(String query : deleteQueries) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            statement.close();
        }
    }

    public User listUserById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        String sqlQuery = "SELECT * FROM users WHERE id = " + id;
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        User user;
        resultSet.next();
        user = new User(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getBoolean("admin")
        );

        //Close das Interfaces quando nao se precisar mais
        resultSet.close();
        statement.close();

        return user;
    }

    public List<User> listUsers() throws SQLException {
        Statement statement = connection.createStatement();
        String sqlQuery = "SELECT * FROM users";
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        List<User> users = this.buildUsersListFromResultSet(resultSet);

        //Close das Interfaces quando nao se precisar mais
        resultSet.close();
        statement.close();

        return users;
    }

    public List<User> listUsersBy(List<DBCondition> conditions) throws SQLException {
        Statement statement = connection.createStatement();
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM users");
        if(!conditions.isEmpty()) {
            sqlQuery.append(" WHERE ");

            for (DBCondition DBCondition : conditions)
                sqlQuery.append(DBCondition);
        }

        ResultSet resultSet = statement.executeQuery(sqlQuery.toString());
        List<User> users = this.buildUsersListFromResultSet(resultSet);

        //Close das Interfaces quando nao se precisar mais
        resultSet.close();
        statement.close();

        return users;
    }

    public User createUser(String name, String email, String password, boolean isAdmin) throws SQLException {
        Statement statement = connection.createStatement();

        String sqlQuery = "INSERT INTO users (name, email, password, admin) VALUES ('"
                + name + "', '"
                + email + "', '"
                + password + "', '"
                + (isAdmin ? 1 : 0) + "')";
        statement.executeUpdate(sqlQuery);
        statement.close();

        return new User(name, email, password, isAdmin);
    }

    public void createCategory(String category) throws SQLException {
        Statement statement = connection.createStatement();

        String sqlQuery = "INSERT INTO category (category) VALUES ('" + category + "')";
        statement.executeUpdate(sqlQuery);
        statement.close();
    }

    public void createFornecedor(String fornecedor) throws SQLException {
        Statement statement = connection.createStatement();

        String sqlQuery = "INSERT INTO fornecedor (name) VALUES ('" + fornecedor + "')";
        statement.executeUpdate(sqlQuery);
        statement.close();
    }

    private List<User> buildUsersListFromResultSet(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            boolean isAdmin = resultSet.getBoolean("admin");

            users.add(new User(id, name, email, password, isAdmin));
        }

        return users;
    }

    public List<Category> listCategories() throws SQLException {
        Statement statement = connection.createStatement();
        String sqlQuery = "SELECT * FROM category";
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        List<Category> categories = buildCategoriesListFromResultSet(resultSet);
        resultSet.close();
        statement.close();

        return categories;
    }

    //product

    public Product listProductById(String id) throws SQLException{
        Statement statement = connection.createStatement();
        String sqlQuery = "SELECT * FROM produto WHERE cod_barras = " + id;
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        Product product = null;

        if(resultSet.next()) {
            product = new Product(
                    resultSet.getString("cod_barras"), resultSet.getString("name"),
                    getCategoryById(resultSet.getInt("category_category_id")),
                    resultSet.getFloat("price"),
                    resultSet.getInt("stock"),
                    listFornecedorById(resultSet.getInt("fornecedor_id"))
            );
        }
        resultSet.close();
        statement.close();

        return product;
    }

    public Category getCategoryById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        String sqlQuery = "SELECT * FROM category WHERE id = " + id;
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        resultSet.next();
        Category categoria = new Category(
                resultSet.getInt("id"),
                resultSet.getString("category")
        );
        resultSet.close();
        statement.close();

        return categoria;
    }

    // public List<Product> listProducts(String searchTerm,filter,sorting) throws SQLException {
    public List<Product> listProducts() throws SQLException {
        Statement statement = connection.createStatement();
        String sqlQuery = "SELECT * FROM produto";
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        List<Product> products = this.buildProductsListFromResultSet(resultSet);

        //Close das Interfaces quando nao se precisar mais
        resultSet.close();
        statement.close();

        return products;
    }


    private List<Product> buildProductsListFromResultSet(ResultSet resultSet) throws SQLException {
        List<Product> products = new ArrayList<>();
        List<Fornecedor> fornecedores = listFornecedores();
        HashMap<Integer, Fornecedor> fornecedoresMap = new HashMap<>();

        for(Fornecedor fornecedor : fornecedores)
            fornecedoresMap.put(fornecedor.getId(), fornecedor);



        while (resultSet.next()) {
            String name = resultSet.getString("name");
            Category categoria = getCategoryById(resultSet.getInt("category_category_id"));
            String id = resultSet.getString("cod_barras");
            Integer stock = resultSet.getInt("stock");
            Float price = resultSet.getFloat("price");
            Fornecedor idFornecedor = fornecedoresMap.get(resultSet.getInt("fornecedor_id"));

            products.add(new Product(id, name,categoria, price,stock,idFornecedor));
        }

        return products;
    }

    public void createProduct(Product product) throws SQLException{
        Statement statement = connection.createStatement();

        String sqlQuery = "INSERT INTO produto (name, category_category_id, cod_barras, price, fornecedor_id, stock) VALUES ('"
            + product.getName() + "', "
            + product.getCategory().getId() + ", '"
            + product.getId() + "', "
            + product.getPrice() + ", "
            + product.getFornecedor().getId() + ", "
            + product.getStock() + ")";

         statement.executeUpdate(sqlQuery);
         statement.close();
        }
    
    public List<Product> listProductsByFilters(String searchBar, Category category, Fornecedor fornecedor, int priceSorting) throws SQLException {
        Statement statement = connection.createStatement();
        StringBuilder sqlQuery = new StringBuilder("Select * FROM produto ");
        List<String> conditions = new ArrayList<>();
        String condition = "";
        if(!searchBar.trim().isEmpty() || category != null || fornecedor != null) {
            if(!searchBar.trim().isEmpty()) {
                condition = "name LIKE '%" + searchBar + "%'";
                conditions.add(condition);
            }

            if(category != null) {
                condition = "category_category_id = " + category.getId();
                conditions.add(condition);
            }

            if(fornecedor != null) {
                condition = "fornecedor_id = " + fornecedor.getId();
                conditions.add(condition);
            }

            condition = "WHERE " + String.join(" AND ", conditions);
        }

        if(priceSorting > 0)
            condition += " ORDER BY price ASC";
        else if(priceSorting < 0)
            condition += " ORDER BY price DESC";

        sqlQuery.append(condition);

        ResultSet resultSet = statement.executeQuery(sqlQuery.toString());
        List<Product> products = this.buildProductsListFromResultSet(resultSet);
        resultSet.close();
        statement.close();

        return products;
    }

    public List<Fornecedor> listFornecedores() throws SQLException {
        Statement statement = connection.createStatement();
        String sqlQuery = "SELECT * FROM fornecedor";
        ResultSet resultSet = statement.executeQuery(sqlQuery);

        List<Fornecedor> fornecedoresList = buildFornecedoresListFromResultSet(resultSet);

        resultSet.close();
        statement.close();

        return fornecedoresList;
    }

    public void editProduct(Product product) throws SQLException{
        Statement statement = connection.createStatement();

        String sqlQuery = "UPDATE produto SET name='" + product.getName() + "', "
                + "category_category_id=" + product.getCategory().getId() +", "
                + "price=" + product.getPrice() + ", " + "stock=" + product.getStock() + ", "
                + "fornecedor_id=" + product.getFornecedor().getId() +
                " WHERE cod_barras = '" + product.getId() + "'";
        statement.executeUpdate(sqlQuery);
        statement.close();

    }



    public Fornecedor listFornecedorById(Integer id) throws SQLException {

        Statement statement = connection.createStatement();
        String sqlQuery = "SELECT * FROM fornecedor WHERE id = " + id;
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        Fornecedor fornecedor;
        resultSet.next();
        fornecedor = new Fornecedor(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
        resultSet.close();
        statement.close();

        return fornecedor;
    }

    public List<Venda> listVendas() throws SQLException {
        Statement statement = connection.createStatement();
        String sqlQuery = "SELECT * FROM venda";
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        List<Venda> vendas = this.buildVendasListFromResultSet(resultSet);

        resultSet.close();
        statement.close();

        return vendas;
    }

    public List<Product> listProductsInPurchase(int vendaId) throws SQLException {
        Statement statement = connection.createStatement();
        String sqlQuery = "SELECT * FROM produto_has_venda WHERE venda_id = " + vendaId;
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        List<Product> produtos = new ArrayList<>();

        while(resultSet.next()) {
            String prodId = resultSet.getString("produto_cod_barras");
            int prodQuant = resultSet.getInt("quantidade");
            Product product = listProductById(prodId);
            product.setStock(prodQuant);
            produtos.add(product);
        }

        resultSet.close();
        statement.close();

        return produtos;
    }

    private List<Venda> buildVendasListFromResultSet(ResultSet resultSet) throws SQLException {
        List<Venda> vendas = new ArrayList<>();
        List<User> users = listUsers();
        HashMap<Integer, User> usersMap = new HashMap<>();

        for(User user : users)
            usersMap.put(user.getId(), user);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
//            int vendedor = resultSet.getString("fornecedor.id");
            User seller = usersMap.get(resultSet.getInt("users_id"));
            float total = resultSet.getFloat("total");
            String nome_cliente = resultSet.getString("nome_cliente");
            String nif = resultSet.getString("nif_cliente");
            Timestamp timestamp = resultSet.getTimestamp("date");
            List<Product> products = listProductsInPurchase(id);

            vendas.add(new Venda(id, seller, nif, nome_cliente, total, timestamp, FXCollections.observableArrayList(products)));
        }

        return vendas;
    }


    private List<Category> buildCategoriesListFromResultSet(ResultSet resultSet) throws SQLException {
        List<Category> categories = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String category = resultSet.getString("category");
            categories.add(new Category(id, category));
        }

        return categories;

    }

    private List<Fornecedor> buildFornecedoresListFromResultSet(ResultSet resultSet) throws SQLException {
        List<Fornecedor> fornecedoresList = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            fornecedoresList.add(new Fornecedor(id, name));
        }

        return fornecedoresList;
    }


    public List<Venda> listVendasByNif(Integer nif) throws SQLException{
        Statement statement = connection.createStatement();
        String sqlQuery = "SELECT * FROM venda WHERE nif = " + nif;
        ResultSet resultSet = statement.executeQuery(sqlQuery);

        List<Venda> vendas = this.buildVendasListFromResultSet(resultSet);
        resultSet.close();
        statement.close();

        return vendas;
    }

    public List<Venda> listVendasByNifSellerSorting(String nif, User seller, int sorting, Timestamp timestamp) throws SQLException{
        Statement statement = connection.createStatement();
        StringBuilder sqlQuery = new StringBuilder("Select * FROM venda");

        if(!nif.trim().isEmpty() || seller != null || timestamp != null) {
            sqlQuery.append(" WHERE ");

            if(!nif.trim().isEmpty()) {
                sqlQuery.append("(nif_cliente LIKE '%").append(nif)
                        .append("%' OR nome_cliente LIKE '%").append(nif).append("%') ");
                if(seller != null || timestamp != null)
                    sqlQuery.append(" AND ");
            }

            if(seller != null) {
                sqlQuery.append("users_id = ").append(seller.getId());

                if(timestamp != null)
                    sqlQuery.append(" AND ");
            }

            if(timestamp != null) {
                LocalDateTime endOfDate = LocalTime.MAX.atDate(timestamp.toLocalDateTime().toLocalDate());
                sqlQuery.append("date >= '").append(timestamp)
                        .append("' AND date <= '").append(Timestamp.valueOf(endOfDate)).append("' ");
            }
        }

        if(sorting > 0)
            sqlQuery.append(" ORDER BY total ASC");
        else if(sorting < 0)
            sqlQuery.append(" ORDER BY total DESC");

        ResultSet resultSet = statement.executeQuery(sqlQuery.toString());

        List<Venda> vendas = this.buildVendasListFromResultSet(resultSet);
        resultSet.close();
        statement.close();

        return vendas;
    }

    public Venda createPurchase(Venda venda) throws SQLException{
//        Statement statement = connection.createStatement();

//        String sqlQuery = "INSERT INTO venda (total, date, users_id, nif_cliente, nome_cliente) VALUES ("
//                + venda.getTotal() + ", '"
//                + venda.getTimestamp() + "', "
//                + venda.getSeller().getId() + ", '"
//                + venda.getNif() + "', '"
//                + venda.getNomeCliente() + "')";
        String sqlQuery = "INSERT INTO venda (total, date, users_id, nif_cliente, nome_cliente) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
        statement.setFloat(1, venda.getTotal());
        statement.setTimestamp(2, venda.getTimestamp());
        statement.setInt(3, venda.getSeller().getId());
        statement.setString(4, venda.getNif());
        statement.setString(5, venda.getNomeCliente());
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        resultSet.next();

        Venda newPurchase = new Venda(
                resultSet.getInt(1),
                venda.getSeller(),
                venda.getNif(),
                venda.getNomeCliente(),
                venda.getTotal(),
                venda.getTimestamp(),
                venda.getProducts()
        );

        statement.close();

        if(newPurchase.getId() <= 0) return null;

        for(Product product : newPurchase.getProducts()) {
            // Associate product to purchase
            Statement prodStatement = connection.createStatement();
            sqlQuery = "INSERT INTO produto_has_venda (produto_cod_barras, venda_id, quantidade) VALUES ("
                    + product.getId() + ", "
                    + newPurchase.getId() + ", "
                    + product.getStock() + ")";
            prodStatement.executeUpdate(sqlQuery);
            prodStatement.close();

            // Update product stock
            Product prod = listProductById(product.getId());
            int updatedStock = prod.getStock() - product.getStock();
            if(updatedStock < 0) updatedStock = 0;
            prodStatement = connection.createStatement();
            sqlQuery = "UPDATE produto SET stock = " + updatedStock + " WHERE cod_barras=" + product.getId();
            prodStatement.executeUpdate(sqlQuery);
            prodStatement.close();
        }

        return newPurchase;
    }


    //Fornecedores
//    /**
//     * UPDATE data from the database
//     *
//     * @param id
//     * @param name
//     * @param birthdate
//     * @throws SQLException
//     */
//    public void updateUser(int id, String name, String birthdate) throws SQLException {
//        Statement statement = connection.createStatement();
//
//        String sqlQuery = "UPDATE users SET name='" + name + "', " +
//                "BIRTHDATE='" + birthdate + "' WHERE id=" + id;
//        statement.executeUpdate(sqlQuery);
//        statement.close();
//    }
//
//    /**
//     * DELETE data from database
//     *
//     * @param id
//     * @throws SQLException
//     */
//    public void deleteUser(int id) throws SQLException {
//        Statement statement = connection.createStatement();
//
//        String sqlQuery = "DELETE FROM users WHERE id=" + id;
//        statement.executeUpdate(sqlQuery);
//        statement.close();
//    }
}
