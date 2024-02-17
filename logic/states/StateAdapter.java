package gestorInventario.logic.states;

import gestorInventario.logic.model.*;

import java.sql.Timestamp;

abstract class StateAdapter implements IState{
    GestorInventario gestorInventario;

    public StateAdapter(GestorInventario gestorInventario) {
        this.gestorInventario = gestorInventario;
    }

    @Override
    public IState goToPaginaInicial() { return this; }

    @Override
    public IState submeterVenda(Venda purchase) { return this; }

    @Override
    public IState goToRegistaVenda() { return this; }

    @Override
    public IState efetuarPesquisaVendas(String nif, User seller, int priceSorting, Timestamp timestamp) { return this; }

    @Override
    public IState efetuarPesquisaProdutos(String searchBar, Category category, Fornecedor fornecedor, int priceSorting) { return this; }

    @Override
    public IState goToEditarProdutos(Product product) { return this; }

    @Override
    public IState editarProduto(Product product) { return this; }

    @Override
    public IState adicionarProduto(Product product) { return this; }

    @Override
    public IState goToAdicionarUtilizador() { return this; }

    @Override
    public IState adicionarUtilizador(User user) { return this; }

    @Override
    public IState submeteLogin(String email, String pw){return this; }

    @Override
    public IState logout() { return this; }

    @Override
    public IState selecionarProduto(Product product){ return this;}

    @Override
    public IState selecionarVenda(Venda venda){ return this;}

    @Override
    public IState goToAdicionarProdutos() { return this; }
}
