package gestorInventario.logic.states;

import gestorInventario.logic.model.*;

import java.util.List;

import java.util.List;

public class ListaProdutos extends StateAdapter{

    public ListaProdutos(GestorInventario gestorInventario) {
        super(gestorInventario);
    }

    @Override
    public IState selecionarProduto(Product produto){
        gestorInventario.setSelectedProduct(produto);
        return new VisualizarProduto(gestorInventario);
    }

    @Override
    public IState goToEditarProdutos(Product product) {
        gestorInventario.setSelectedProduct(product);
        return new EditarProduto(gestorInventario);
    }

    @Override
    public IState goToPaginaInicial() { return new PaginaInicial(gestorInventario); }

    @Override
    public IState efetuarPesquisaProdutos(String searchBar, Category category, Fornecedor fornecedor, int priceSorting) {
        ConsultarProdutosFilters consultarProdutosFilters = new ConsultarProdutosFilters(
                searchBar, category, fornecedor, priceSorting
        );
        List<Product> productList = gestorInventario.getProductsByFilters(consultarProdutosFilters);
        gestorInventario.setConsultarProdutosFilters(consultarProdutosFilters);
        gestorInventario.setProductsList(productList);

        return new ListaProdutos(gestorInventario);
    }

    @Override
    public State getState() { /* return this state */ return State.LISTA_PRODUTOS; }
}
