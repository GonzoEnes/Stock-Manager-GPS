package gestorInventario.ui.gui;

import gestorInventario.logic.GestorInventarioObservable;
import gestorInventario.ui.gui.states.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import static gestorInventario.ui.gui.Constants.STATE_CHANGE;

public class Root extends BorderPane {
    final private GestorInventarioObservable gestorInventarioObservable;

    public Root(GestorInventarioObservable gestorInventarioObservable) {
        this.gestorInventarioObservable = gestorInventarioObservable;

        createView();
        registerObservable();
        update();
    }

    private void registerObservable() {
        gestorInventarioObservable.addPropertyChangeListener(STATE_CHANGE, evt -> update());
    }

    private void createView() {
        StackPane panes = new StackPane(
            new LoginGUI(this.gestorInventarioObservable),
            new AdicionarUserGUI(this.gestorInventarioObservable),
            new AdicionarProdutoGUI(this.gestorInventarioObservable),
            new ConsultarVendasGUI(this.gestorInventarioObservable),
            new EditarProdutoGUI(this.gestorInventarioObservable),
            new ListaProdutosGUI(this.gestorInventarioObservable),
            new PaginaInicialGUI(this.gestorInventarioObservable),
            new RegistarVendaGUI(this.gestorInventarioObservable),
            new StatusUserGUI(this.gestorInventarioObservable),
            new VisualizarProdutoGUI(this.gestorInventarioObservable),
            new VisualizarVendaGUI(this.gestorInventarioObservable)
        );

        setCenter(panes);
    }

    private void update() { }
}
