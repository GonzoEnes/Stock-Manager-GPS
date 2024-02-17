package gestorInventario.logic;


import gestorInventario.logic.model.Product;
import gestorInventario.logic.model.User;
import gestorInventario.logic.model.*;
import gestorInventario.logic.states.State;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Timestamp;
import java.util.List;

import static gestorInventario.ui.gui.Constants.STATE_CHANGE;

public class GestorInventarioObservable {
    private final StateMachine stateMachine;
    private final PropertyChangeSupport propertyChangeSupport;

    public GestorInventarioObservable(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
        propertyChangeSupport = new PropertyChangeSupport(stateMachine);
    }

    public Product getSelectedProduct(){
        return stateMachine.getSelectedProduct();
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    public List<User> getUsers() {
        return stateMachine.getUsers();
    }

    public List<Category> getCategoriesList() {
        return stateMachine.getCategoriesList();
    }

    public Product checkBarCode(String barCode) {
        return this.stateMachine.checkBarCode(barCode);
    }

    public User getLoggedInUser() { return stateMachine.getLoggedInUser(); }

    public Venda getSelectedPurchase() {
        return stateMachine.getSelectedPurchase();
    }

    public void submeteLogin(String email, String password) {
        this.stateMachine.submeteLogin(email, password);
        firePropertyChange(STATE_CHANGE, null, null);
    }

    public void registerPurchase(Venda purchase) {
        this.stateMachine.submeterVenda(purchase);
        firePropertyChange(STATE_CHANGE, null, null);
    }

    public void createUser(String name, String email, boolean isAdmin) {
        User userData = new User(name, email, "", isAdmin);
        this.stateMachine.createUser(userData);
        firePropertyChange(STATE_CHANGE, null, null);
    }

    public void createProduct(String codigoBarras, String name, Category category, float price, int stock, Fornecedor fornecedor) {
        Product productData = new Product(codigoBarras, name, category, price, stock, fornecedor);
        this.stateMachine.createProduct(productData);
        firePropertyChange(STATE_CHANGE, null, null);
    }

    public void efetuarPesquisaVendas(String nif, User seller, int priceOrder, Timestamp timestamp) {
        this.stateMachine.efetuarPesquisaVendas(nif, seller, priceOrder, timestamp);
        firePropertyChange(STATE_CHANGE, null, null);
    }

    public void selecionarProduto(Product product) {
        stateMachine.selecionarProduto(product);
        firePropertyChange(STATE_CHANGE, null, null);
    }

    public void selecionarVenda(Venda venda) {
        stateMachine.selecionarVenda(venda);
        firePropertyChange(STATE_CHANGE, null, null);
    }

    public ConsultarVendasFilters getConsultarVendasFilters() { return stateMachine.getConsultarVendasFilters(); }
    
    public String getErrorMsg() {
        return stateMachine.getErrorMsg();
    }
    public String getSuccessMsg() { return stateMachine.getSuccessMsg(); }

    public void clearErrorMsg() {
        stateMachine.clearErrorMsg();
    }
    public void clearSuccessMsg() {
        stateMachine.clearSuccessMsg();
    }

    public User getSelectedUser() { return stateMachine.getSelectedUser(); }

    public void goToRegistaVenda() {
        this.stateMachine.goToRegistaVenda();
        firePropertyChange(STATE_CHANGE, null, null);
    }
    public void goToConsultarVendas() {
        this.stateMachine.goToConsultarVendas();
        firePropertyChange(STATE_CHANGE, null, null);
    }
    public void goToListaProdutos() {
        this.stateMachine.goToListaProdutos();
        firePropertyChange(STATE_CHANGE, null, null);
    }
    public void goToAdicionarProdutos() {
        this.stateMachine.goToAdicionarProdutos();
        firePropertyChange(STATE_CHANGE, null, null);
    }
    public void goToAdicionarUtilizador() {
        this.stateMachine.goToAdicionarUtilizador();
        firePropertyChange(STATE_CHANGE, null, null);
    }
    public void goToPaginaInicial() {
        this.stateMachine.goToPaginaInicial();
        firePropertyChange(STATE_CHANGE, null, null);
    }
    public void goToEditarProduct(Product product) {
        this.stateMachine.goToEditarProdutos(product);
        firePropertyChange(STATE_CHANGE, null, null);
    }

    public void logout() {
        this.stateMachine.logout();
        firePropertyChange(STATE_CHANGE, null, null);
    }

    public State getCurrentState() { return this.stateMachine.getCurrentState(); }

    public List<Venda> getPurchasesList() {
        return stateMachine.getPurchasesList();
    }
    public List<Fornecedor> getFornecedoresList() {
        return stateMachine.getFornecedoresList();
    }

    public List<Product> getProductsList() {
        return stateMachine.getProductsList();
    }
    public void efetuarPesquisaProdutos(String searchBar, Category category, Fornecedor fornecedor, int priceSorting) {
        stateMachine.efetuarPesquisaProdutos(searchBar, category, fornecedor, priceSorting);
        firePropertyChange(STATE_CHANGE, null, null);
    }

    public void editarProduto(Product product) {
        stateMachine.editarProduto(product);
        firePropertyChange(STATE_CHANGE, null, null);
    }

    public ConsultarProdutosFilters getConsultarProdutosFilters() { return stateMachine.getConsultarProdutosFilters(); }
}
