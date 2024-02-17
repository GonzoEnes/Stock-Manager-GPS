package gestorInventario.logic.model;

import gestorInventario.logic.db.DB;
import gestorInventario.logic.db.wrappers.*;
import gestorInventario.logic.db.DBCondition;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GestorInventario {
    private Venda selectedPurchase;
    private final UsersWrapper usersWrapper;
    private final ProductsWrapper productsWrapper;
    private List<Product> products;
    private final VendasWrapper vendasWrapper;
    private final CategoriesWrapper categoriesWrapper;
    private final FornecedoresWrapper fornecedoresWrappers;
    private List<Venda> purchasesList = new ArrayList<>();
    private List<Product> productsList = new ArrayList<>();
    private ConsultarVendasFilters consultarVendasFilters = new ConsultarVendasFilters();
    private ConsultarProdutosFilters consultarProdutosFilters = new ConsultarProdutosFilters();
    private Product selectedProduct;
    //    private List<Category> categoriesList;
    private String errorMsg = null;
    private String successMsg = null;
    private User selectedUser;
    private User loggedInUser;

    public GestorInventario(DB db) {
        usersWrapper = new UsersWrapper(db);
        productsWrapper = new ProductsWrapper(db);
        products = new ArrayList();
        vendasWrapper = new VendasWrapper(db);
        categoriesWrapper = new CategoriesWrapper(db);
        fornecedoresWrappers = new FornecedoresWrapper(db);
    }

    public GestorInventario(UsersWrapper usersWrapper, ProductsWrapper productsWrapper, VendasWrapper vendasWrapper, CategoriesWrapper categoriesWrapper, FornecedoresWrapper fornecedoresWrappers) {
        this.usersWrapper = usersWrapper;
        this.productsWrapper = productsWrapper;
        this.vendasWrapper = vendasWrapper;
        this.categoriesWrapper = categoriesWrapper;
        this.fornecedoresWrappers = fornecedoresWrappers;
    }

    public String getSuccessMsg() {
        return successMsg;
    }

    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void clearSelectedItems() {
        selectedUser = null;
        selectedProduct = null;
        selectedPurchase = null;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public ConsultarProdutosFilters getConsultarProdutosFilters() {
        return consultarProdutosFilters;
    }

    public List<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<Product> productsList) {
        this.productsList = productsList;
    }

    public List<Product> getProductsByFilters(ConsultarProdutosFilters consultarProdutosFilters) {
        return productsWrapper.getByFilters(consultarProdutosFilters);
    }

    public void setConsultarProdutosFilters(ConsultarProdutosFilters consultarProdutosFilters) {
        this.consultarProdutosFilters = consultarProdutosFilters;
    }

    public ConsultarVendasFilters getConsultarVendasFilters() {
        return consultarVendasFilters;
    }

    public void setConsultarVendasFilters(ConsultarVendasFilters consultarVendasFilters) {
        this.consultarVendasFilters = consultarVendasFilters;
    }

    public List<User> getUsers() {
        return usersWrapper.getAll();
    }

    public void setPurchasesList(List<Venda> purchasesList) {
        this.purchasesList = purchasesList;
    }


    public List<Venda> getPurchasesByNifSellerPriceSorting(String nif, User seller, int priceOrder, Timestamp timestamp) {
        return vendasWrapper.getByNifSellerPrice(nif, seller, priceOrder, timestamp);
    }

    public List<Venda> getPurchasesList() {
        return purchasesList;
    }


    public List<Category> getCategoriesList() {
        return categoriesWrapper.getAll();
    }

    public Venda getSelectedPurchase() {
        return selectedPurchase;
    }

    public void setSelectedPurchase(Venda purchase) {
        this.selectedPurchase = purchase;
    }

    public User getLoggedInUser() { return loggedInUser; }

    public void setLoggedInUser(User user) { loggedInUser = user; }

    public User getUserById(int id) { return usersWrapper.getById(id); }

    public User getUserByCredentials(String email, String password) {
        List<User> users = usersWrapper.getBy(List.of(
                new DBCondition("", "email", "=", email),
                new DBCondition("AND", "password", "=", password)
        ));

        if(users.size() > 0) return users.get(0);
        return null;
    }

    public Product checkBarCode(String barCode) {
        return productsWrapper.getById(barCode);
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void clearErrorMsg() {
        this.errorMsg = null;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public User createUser(User user) {
        user.generateRandomPassword();
        User newUser = usersWrapper.create(user.getName(), user.getEmail(), user.getPassword(), user.isAdmin());

        try {
            Email.send(newUser);
        } catch (Exception e) {
            System.out.println("Email not sent");
        }
        return newUser;
    }

    public List<User> getUsersByEmail(String email) {
        List<DBCondition> conditions = List.of(
                new DBCondition("", "email", "=", email)
        );

        return usersWrapper.getBy(conditions);
    }

    public boolean editProduct(Product product) {
        return productsWrapper.editProduct(product);
    }

    public Venda registerPurchage(Venda venda) {
        return vendasWrapper.createPurchase(venda);
    }

    public Product createProduct(Product product) {
        return productsWrapper.create(product);
    }

    public User getSelectedUser() { return selectedUser; }
    public List<Fornecedor> getFornecedoresList() { return fornecedoresWrappers.getAll(); }
    public void setSelectedUser(User selectedUser) { this.selectedUser = selectedUser; }

}

