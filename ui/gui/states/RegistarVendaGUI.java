package gestorInventario.ui.gui.states;

import gestorInventario.logic.GestorInventarioObservable;
import gestorInventario.logic.model.Product;
import gestorInventario.logic.states.State;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.List;

import static gestorInventario.ui.gui.Constants.STATE_CHANGE;

public class RegistarVendaGUI extends BorderPane {
    private final GestorInventarioObservable gestorInventarioObservable;
    private Integer internalState = 0;
    private List<Product> cartList;

    public RegistarVendaGUI(GestorInventarioObservable gestorInventarioObservable) {
        this.gestorInventarioObservable = gestorInventarioObservable;
        createViewAndRegisterListeners();
        registerObservable();
        update();
    }

    public void setCartList(List<Product> cartList) {
        this.cartList = cartList;
    }

    public List<Product> getCartList() {
        return cartList;
    }

    public int getInternalState() { return internalState; }

    private void registerObservable(){
        this.gestorInventarioObservable.addPropertyChangeListener(STATE_CHANGE, evt -> update());
    }

    public void changeState(int newInternalState) {
        internalState = newInternalState;
    }

    private void displayErrorMsg() {
        if(gestorInventarioObservable.getErrorMsg() == null) return;

        Alert errorAlert = new Alert(Alert.AlertType.ERROR, gestorInventarioObservable.getErrorMsg());
        errorAlert.show();

        gestorInventarioObservable.clearErrorMsg();
    }

    private void createViewAndRegisterListeners(){
        StackPane panes = new StackPane(
                new RegistarVendaProdutosGUI(this.gestorInventarioObservable, this),
                new RegistarVendaFinalizarGUI(this.gestorInventarioObservable, this)
        );

        Button backBtn = new Button("Voltar");
        backBtn.setOnAction(e -> gestorInventarioObservable.goToPaginaInicial());
        backBtn.setAlignment(Pos.BOTTOM_LEFT);
        HBox bottomContainer = new HBox();
        bottomContainer.getChildren().add(backBtn);
        HBox.setMargin(backBtn, new Insets(16, 16, 16, 16));

        setCenter(panes);
        setBottom(bottomContainer);
    }


    private void update() {
        this.setVisible(gestorInventarioObservable.getCurrentState() == State.REGISTAR_VENDA);
    }
}
