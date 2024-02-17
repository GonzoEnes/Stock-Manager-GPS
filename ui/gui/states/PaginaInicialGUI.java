package gestorInventario.ui.gui.states;

import gestorInventario.logic.GestorInventarioObservable;
import gestorInventario.logic.states.State;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import static gestorInventario.ui.gui.Constants.STATE_CHANGE;

public class PaginaInicialGUI extends VBox {
    private final GestorInventarioObservable gestorInventarioObservable;

    public PaginaInicialGUI(GestorInventarioObservable gestorInventarioObservable) {
        this.gestorInventarioObservable = gestorInventarioObservable;
        createViewAndRegisterListeners();
        registerObservable();
        update();
    }

    private void registerObservable(){
        this.gestorInventarioObservable.addPropertyChangeListener(STATE_CHANGE, evt -> update());
    }

    private void createViewAndRegisterListeners(){
        getChildren().clear();

        // Create view
        Button listProductsBtn = new Button("Consultar produtos");
        Button addProductBtn = new Button("Adicionar produto");
        Button listPurchasesBtn = new Button("Consultar vendas");
        Button registerPurchaseBtn = new Button("Registar venda");
        Button addUserBtn = new Button("Adicionar utilizador");
        Button logoutBtn = new Button("Logout");

        setAlignment(Pos.CENTER);
        setSpacing(10);
        getChildren().addAll(
                listProductsBtn,
                addProductBtn,
                listPurchasesBtn,
                registerPurchaseBtn
        );

        if(gestorInventarioObservable.getLoggedInUser() != null && gestorInventarioObservable.getLoggedInUser().isAdmin())
            getChildren().add(addUserBtn);

        getChildren().add(
                logoutBtn
        );

        // Create Listeners
        listProductsBtn.setOnAction(e -> gestorInventarioObservable.goToListaProdutos());
        addProductBtn.setOnAction(e -> gestorInventarioObservable.goToAdicionarProdutos());
        listPurchasesBtn.setOnAction(e -> gestorInventarioObservable.goToConsultarVendas());
        registerPurchaseBtn.setOnAction(e -> gestorInventarioObservable.goToRegistaVenda());
        addUserBtn.setOnAction(e -> gestorInventarioObservable.goToAdicionarUtilizador());
        logoutBtn.setOnAction(e -> gestorInventarioObservable.logout());
    }

    private void update() {
        this.setVisible(gestorInventarioObservable.getCurrentState() == State.PAGINA_INICIAL);
        createViewAndRegisterListeners();
    }
}
