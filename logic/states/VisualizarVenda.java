package gestorInventario.logic.states;

import gestorInventario.logic.model.GestorInventario;

public class VisualizarVenda extends StateAdapter {

    public VisualizarVenda(GestorInventario gestorInventario) {
        super(gestorInventario);
    }

    @Override
    public IState goToPaginaInicial() {
        gestorInventario.setSelectedPurchase(null);
        return new PaginaInicial(gestorInventario);
    }

    @Override
    public State getState() { return State.VISUALIZAR_VENDAS; }
}