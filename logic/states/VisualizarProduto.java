package gestorInventario.logic.states;

import gestorInventario.logic.db.DB;
import gestorInventario.logic.model.GestorInventario;
import gestorInventario.logic.model.Product;

import java.sql.SQLException;
import java.util.List;

public class VisualizarProduto extends StateAdapter {

    public VisualizarProduto(GestorInventario gestorInventario) {
        super(gestorInventario);
    }

    @Override
    public IState goToPaginaInicial() { return new PaginaInicial(gestorInventario); }

    @Override
    public State getState() { /* return this state */ return State.VISUALIZAR_PRODUTO; }
}