package gestorInventario.logic.states;

import gestorInventario.logic.model.*;

import java.sql.Timestamp;

public interface IState {
    IState goToPaginaInicial();
    IState submeterVenda(Venda purchase);
    IState goToRegistaVenda();
    IState selecionarVenda(Venda venda);
    IState efetuarPesquisaVendas(String nif, User seller, int priceSorting, Timestamp timestamp);
    IState efetuarPesquisaProdutos(String searchBar, Category category, Fornecedor fornecedor, int priceSorting);
    IState goToEditarProdutos(Product product);
    IState editarProduto(Product product);
    IState selecionarProduto(Product produto);
    IState adicionarProduto(Product product);
    IState goToAdicionarProdutos();
    IState goToAdicionarUtilizador();
    IState adicionarUtilizador(User user);
    IState submeteLogin(String email, String pw);
    IState logout();
    State getState();
}