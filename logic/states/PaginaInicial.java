package gestorInventario.logic.states;

import gestorInventario.logic.model.GestorInventario;
import gestorInventario.logic.model.Product;
import gestorInventario.logic.model.User;
import gestorInventario.logic.model.*;

import java.sql.Timestamp;
import java.util.List;

public class PaginaInicial extends StateAdapter {

    public PaginaInicial(GestorInventario gestorInventario) {
        super(gestorInventario);
    }

    @Override
    public IState goToRegistaVenda() { return new RegistarVenda(gestorInventario); }

    @Override
    public IState efetuarPesquisaVendas(String nif, User seller, int priceSorting, Timestamp timestamp) {
        List<Venda> purchasesList = gestorInventario.getPurchasesByNifSellerPriceSorting(nif, seller, priceSorting, timestamp);
        ConsultarVendasFilters consultarVendasFilters = new ConsultarVendasFilters(nif, seller, priceSorting, timestamp);
        gestorInventario.setConsultarVendasFilters(consultarVendasFilters);
        gestorInventario.setPurchasesList(purchasesList);

        return new ConsultarVendas(gestorInventario);
    }

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
    public IState goToAdicionarProdutos() { return new AdicionarProduto(gestorInventario); }

    @Override
    public IState goToAdicionarUtilizador() {
        if(gestorInventario.getLoggedInUser().isAdmin())
            return new AdicionarUser(gestorInventario);
        else
            return new PaginaInicial(gestorInventario);
    }

    @Override
    public IState logout() {
        gestorInventario.setLoggedInUser(null);
        return new Login(gestorInventario);
    }

    @Override
    public State getState() { /* return this state */ return State.PAGINA_INICIAL; }
}