package gestorInventario.ui.gui.states;

import gestorInventario.logic.GestorInventarioObservable;
import gestorInventario.logic.model.Product;
import gestorInventario.logic.model.User;
import gestorInventario.logic.states.State;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static gestorInventario.ui.gui.Constants.STATE_CHANGE;

public class VisualizarProdutoGUI extends BorderPane {
    private final GestorInventarioObservable gestorInventarioObservable;

    public VisualizarProdutoGUI(GestorInventarioObservable gestorInventarioObservable) {
        this.gestorInventarioObservable = gestorInventarioObservable;
        createViewAndRegisterListeners();
        registerObservable();
        update();
    }

    private void registerObservable(){
        this.gestorInventarioObservable.addPropertyChangeListener(STATE_CHANGE, evt -> update());
    }

    private void createViewAndRegisterListeners(){
        if(gestorInventarioObservable.getSelectedProduct() == null) return;
        Product selectedProduct = gestorInventarioObservable.getSelectedProduct();

        Label codBarrasLabel = new Label("Código de barras: " + selectedProduct.getId());
        Label nameLabel = new Label("Nome: " + selectedProduct.getName());
        Label priceLabel = new Label("Preço: " + selectedProduct.getPrice());
        Label stockLabel = new Label("Stock: " + selectedProduct.getStock());
        Label categoryLabel = new Label("Categoria: " + selectedProduct.getCategory().getCategory());
        Label fornecedorLabel = new Label("Fornecedor: " + selectedProduct.getFornecedor().getName());
        Button editProductBtn = new Button("Editar");
        editProductBtn.setOnAction(e -> gestorInventarioObservable.goToEditarProduct(selectedProduct) );

        VBox container = new VBox();
        container.getChildren().addAll(
                codBarrasLabel,
                nameLabel,
                priceLabel,
                stockLabel,
                categoryLabel,
                fornecedorLabel,
                editProductBtn
        );
        container.setSpacing(10);
        container.setPadding(new Insets(100, 200, 100, 200));

        Button backBtn = new Button("Voltar à página inicial");
        backBtn.setOnAction(e -> gestorInventarioObservable.goToPaginaInicial());
        backBtn.setAlignment(Pos.BOTTOM_LEFT);
        HBox bottomContainer = new HBox();
        bottomContainer.getChildren().add(backBtn);
        HBox.setMargin(backBtn, new Insets(16, 16, 16, 16));

        setCenter(container);
        setBottom(bottomContainer);
    }

    private void update() {
        this.setVisible(gestorInventarioObservable.getCurrentState() == State.VISUALIZAR_PRODUTO);
        createViewAndRegisterListeners();
    }
}
