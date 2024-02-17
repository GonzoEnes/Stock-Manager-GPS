package gestorInventario.logic;


import gestorInventario.logic.model.*;
import gestorInventario.logic.states.*;
import gestorInventario.logic.model.GestorInventario;
import gestorInventario.logic.model.Product;
import gestorInventario.logic.model.User;
import gestorInventario.logic.model.Venda;
import gestorInventario.logic.states.IState;
import gestorInventario.logic.states.State;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static gestorInventario.ui.gui.Constants.STATE_CHANGE;

public class StateMachine implements Serializable {
    private IState currentState;
    private GestorInventario gestorInventario;

    public StateMachine(GestorInventario gestorInventario) throws SQLException {
        this.gestorInventario = gestorInventario;
        currentState = new Login(gestorInventario);
        // Initialize appModel
    }

    public Product getSelectedProduct(){
        return gestorInventario.getSelectedProduct();
    }

    public void editarProduto(Product product) {
        currentState = currentState.editarProduto(product);
    }

    public List<User> getUsers() {
        return gestorInventario.getUsers();
    }

    public List<Category> getCategoriesList() {
        return gestorInventario.getCategoriesList();
    }

    public String getErrorMsg() {
        return gestorInventario.getErrorMsg();
    }

    public void clearErrorMsg() {
        gestorInventario.clearErrorMsg();
    }

    // State Change actions to be called by the GUI
    public void submeteLogin(String email, String pw) {
        this.currentState = this.currentState.submeteLogin(email, pw);
    }

    public void createUser(User user) {
        this.currentState = this.currentState.adicionarUtilizador(user);
    }
    public User getSelectedUser() { return gestorInventario.getSelectedUser(); }

    public Product checkBarCode(String barCode) { return this.gestorInventario.checkBarCode(barCode); }

    public User getLoggedInUser() { return gestorInventario.getLoggedInUser(); }

    public void submeterVenda(Venda purchase) {
        this.currentState = this.currentState.submeterVenda(purchase);
    }

    public String getSuccessMsg() { return gestorInventario.getSuccessMsg(); }
    public void clearSuccessMsg() {
        gestorInventario.setSuccessMsg(null);
    }

    public Venda getSelectedPurchase() {
        return gestorInventario.getSelectedPurchase();
    }

    public void goToRegistaVenda() { this.currentState = this.currentState.goToRegistaVenda(); }
    public void goToConsultarVendas() { this.currentState = this.currentState.efetuarPesquisaVendas("", null, 0, null); }
    public void goToListaProdutos() { this.currentState = this.currentState.efetuarPesquisaProdutos("", null, null, 0); }
    public void goToAdicionarProdutos() { this.currentState = this.currentState.goToAdicionarProdutos(); }
    public void goToAdicionarUtilizador() { this.currentState = this.currentState.goToAdicionarUtilizador(); }
    public void goToEditarProdutos(Product product) { this.currentState = this.currentState.goToEditarProdutos(product); }
    public void goToPaginaInicial() {
        gestorInventario.clearSelectedItems();
        this.currentState = this.currentState.goToPaginaInicial();
    }
    public void logout() { this.currentState = this.currentState.logout(); }

    public void efetuarPesquisaVendas(String nif, User seller, int priceOrder, Timestamp timestamp) {
        this.currentState = this.currentState.efetuarPesquisaVendas(nif, seller, priceOrder, timestamp);
    }

    public void selecionarProduto(Product product) { currentState = currentState.selecionarProduto(product); }
    public void selecionarVenda(Venda venda) { currentState = currentState.selecionarVenda(venda); }

    public ConsultarVendasFilters getConsultarVendasFilters() { return gestorInventario.getConsultarVendasFilters(); }

    // State Change actions to be called by the GUI
    public State getCurrentState() {
        return this.currentState.getState();
    }

    public void createProduct(Product product) {
        this.currentState = this.currentState.adicionarProduto(product);
    }

    public List<Fornecedor> getFornecedoresList() {
        return gestorInventario.getFornecedoresList();
    }
    public List<Venda> getPurchasesList() {
        return gestorInventario.getPurchasesList();
    }

    public List<Product> getProductsList() {
        return gestorInventario.getProductsList();
    }

    public void efetuarPesquisaProdutos(String searchBar, Category category, Fornecedor fornecedor, int priceSorting) {
        this.currentState = this.currentState.efetuarPesquisaProdutos(searchBar, category, fornecedor, priceSorting);
    }

    public ConsultarProdutosFilters getConsultarProdutosFilters() { return gestorInventario.getConsultarProdutosFilters(); }
}
