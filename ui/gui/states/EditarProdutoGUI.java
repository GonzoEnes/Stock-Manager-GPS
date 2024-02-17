package gestorInventario.ui.gui.states;

import gestorInventario.logic.GestorInventarioObservable;
import gestorInventario.logic.model.Category;
import gestorInventario.logic.model.Fornecedor;
import gestorInventario.logic.model.Product;
import gestorInventario.logic.states.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import static gestorInventario.ui.gui.Constants.STATE_CHANGE;

public class EditarProdutoGUI extends BorderPane {
    private final GestorInventarioObservable gestorInventarioObservable;
    private ComboBox categoryComboBox;
    private ComboBox fornecedorComboBox;
    TextField nameText = new TextField();
    TextField precoText = new TextField();
    TextField stockText = new TextField();


    public EditarProdutoGUI(GestorInventarioObservable gestorInventarioObservable) {
        this.gestorInventarioObservable = gestorInventarioObservable;
        createViewAndRegisterListeners();
        registerObservable();
        update();
    }

    private void registerObservable() {
        this.gestorInventarioObservable.addPropertyChangeListener(STATE_CHANGE, evt -> update());
    }

    private void displayErrorMsg() {
        if(gestorInventarioObservable.getErrorMsg() == null) return;

        Alert errorAlert = new Alert(Alert.AlertType.ERROR, gestorInventarioObservable.getErrorMsg());
        errorAlert.show();

        gestorInventarioObservable.clearErrorMsg();
    }

    private void createViewAndRegisterListeners() {
        if(gestorInventarioObservable.getSelectedProduct()==null) return;

        Label codigoBarrasLabel = new Label("Código de barras: " + gestorInventarioObservable.getSelectedProduct().getId());
        Label nameLabel = new Label("Name:");
        Label categoriaLabel = new Label("Categoria:");
        Label precoLabel = new Label("Preço:");
        Label stockLabel = new Label("Stock:");
        Label idFornecedorLabel = new Label("Id Fornecedor:");
        Button editButton = new Button("Editar");
        nameText = new TextField(gestorInventarioObservable.getSelectedProduct().getName());
        precoText = new TextField(gestorInventarioObservable.getSelectedProduct().getPrice() + "");
        stockText = new TextField(gestorInventarioObservable.getSelectedProduct().getStock()+"");

        ObservableList<Category> categoriesOptions = FXCollections.observableArrayList(gestorInventarioObservable.getCategoriesList());
        categoryComboBox = new ComboBox(categoriesOptions);
        categoryComboBox.getSelectionModel().select(gestorInventarioObservable.getSelectedProduct().getCategory());
        ObservableList<Fornecedor> fornecedoresOptions = FXCollections.observableArrayList(gestorInventarioObservable.getFornecedoresList());
        fornecedorComboBox = new ComboBox(fornecedoresOptions);
        fornecedorComboBox.getSelectionModel().select(gestorInventarioObservable.getSelectedProduct().getFornecedor());

        VBox container = new VBox();
        container.getChildren().addAll(
                codigoBarrasLabel,
                nameLabel,
                nameText,
                categoriaLabel,
                categoryComboBox,
                precoLabel,
                precoText,
                stockLabel,
                stockText,
                idFornecedorLabel,
                fornecedorComboBox,
                editButton
        );

//        container.setAlignment(Pos.CENTER);
        setPadding(new Insets(30, 100, 30, 100));

        // Create Listeners
        editButton.setOnAction(e -> {
            float price;
            int stock;

            try { price = Float.parseFloat(precoText.getText().trim()); }
            catch (NumberFormatException ignored) { price = -1.0f; }

            try { stock = Integer.parseInt(stockText.getText().trim()); }
            catch (NumberFormatException ignored) { stock = -1; }

            gestorInventarioObservable.editarProduto(new Product(
                    gestorInventarioObservable.getSelectedProduct().getId(), nameText.getText().trim(),
                    (Category) categoryComboBox.getSelectionModel().getSelectedItem(),
                    price,
                    stock,
                    (Fornecedor) fornecedorComboBox.getSelectionModel().getSelectedItem()
            ));
        });

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
        this.setVisible(gestorInventarioObservable.getCurrentState() == State.EDITAR_PRODUTO);
        createViewAndRegisterListeners();
        displayErrorMsg();
    }
}

