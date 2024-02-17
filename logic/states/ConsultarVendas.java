package gestorInventario.logic.states;

import gestorInventario.logic.model.ConsultarVendasFilters;
import gestorInventario.logic.model.GestorInventario;
import gestorInventario.logic.model.User;
import gestorInventario.logic.model.Venda;

import java.sql.Timestamp;
import java.util.List;

public class ConsultarVendas extends StateAdapter {

    public ConsultarVendas(GestorInventario gestorInventario) {
        super(gestorInventario);
    }

    @Override
    public IState goToPaginaInicial() { return new PaginaInicial(gestorInventario); }

    @Override
    public IState efetuarPesquisaVendas(String nif, User seller, int priceSorting, Timestamp timestamp) {
        List<Venda> purchasesList = gestorInventario.getPurchasesByNifSellerPriceSorting(nif, seller, priceSorting, timestamp);
        ConsultarVendasFilters consultarVendasFilters = new ConsultarVendasFilters(nif, seller, priceSorting, timestamp);
        gestorInventario.setConsultarVendasFilters(consultarVendasFilters);
        gestorInventario.setPurchasesList(purchasesList);

        return new ConsultarVendas(gestorInventario);
    }

    @Override
    public IState selecionarVenda(Venda venda) {
        gestorInventario.setSelectedPurchase(venda);
        return new VisualizarVenda(gestorInventario);
    }

    @Override
    public State getState() { /* return this state */ return State.CONSULTAR_VENDAS; }
}