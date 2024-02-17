package gestorInventario.ui.gui.states;

import gestorInventario.logic.GestorInventarioObservable;
import gestorInventario.logic.model.Category;
import gestorInventario.logic.model.Fornecedor;
import gestorInventario.logic.states.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static gestorInventario.ui.gui.Constants.STATE_CHANGE;

public class AdicionarProdutoGUI extends BorderPane {
    private final GestorInventarioObservable gestorInventarioObservable;
    private TextField idProdutoField, nameField, categoriaField, precoField, stockField, idFornecedorField;
    private Button saveButton;
    private ComboBox categoryComboBox;
    private ComboBox fornecedorComboBox;

    public AdicionarProdutoGUI(GestorInventarioObservable gestorInventarioObservable) {
        this.gestorInventarioObservable = gestorInventarioObservable;
        createViewAndRegisterListeners();
        registerObservable();
        update();
    }

    private void registerObservable(){
        this.gestorInventarioObservable.addPropertyChangeListener(STATE_CHANGE, evt -> update());
    }

    private boolean isIdProductValid() {
        return !idProdutoField.getText().trim().isEmpty()
                && Long.parseLong(idProdutoField.getText().trim()) > 0
                && idProdutoField.getText().trim().length() == 13;
    }

    private boolean isNameFieldValid() {
        return !nameField.getText().trim().isEmpty();
    }

    private boolean isStockFieldValid() {
        return Integer.parseInt(stockField.getText()) >= 0;
    }
    private boolean isPriceFieldValid() {
        return Float.parseFloat(precoField.getText()) >= 0;
    }


    private boolean validateForm() {
        try {
            return isIdProductValid() && isNameFieldValid() && isStockFieldValid()
                    && isStockFieldValid() && isPriceFieldValid() && categoryComboBox.getSelectionModel().getSelectedItem() != null
                    && fornecedorComboBox.getSelectionModel().getSelectedItem() != null;
        }catch (NumberFormatException e) {
            return false;
        }
    }

    private void onTextFieldChange() {
        saveButton.setDisable(!validateForm());
    }

    private void displayErrorMsg() {
        if(gestorInventarioObservable.getErrorMsg() == null) return;

        Alert errorAlert = new Alert(Alert.AlertType.ERROR, gestorInventarioObservable.getErrorMsg());
        errorAlert.show();

        gestorInventarioObservable.clearErrorMsg();
    }

    private void createViewAndRegisterListeners(){
        idProdutoField = new TextField();
        nameField = new TextField();
        categoriaField = new TextField();
        precoField = new TextField();
        stockField = new TextField();
        idFornecedorField = new TextField();
        saveButton = new Button("Save");

        ObservableList<Category> categoriesOptions = FXCollections.observableArrayList(gestorInventarioObservable.getCategoriesList());
        categoryComboBox = new ComboBox(categoriesOptions);

        ObservableList<Fornecedor> fornecedoresOptions = FXCollections.observableArrayList(gestorInventarioObservable.getFornecedoresList());
        fornecedorComboBox = new ComboBox(fornecedoresOptions);

        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(10);
        container.getChildren().addAll(
                new VBox(new Label("Código de barras:  "), idProdutoField),
                new VBox(new Label("Nome:  "), nameField),
                new VBox(new Label("Categoria:  "), categoryComboBox),
                new VBox(new Label("Preço:  "), precoField),
                new VBox(new Label("Stock:  "), stockField),
                new VBox(new Label("Fornecedor:  "), fornecedorComboBox),
                saveButton
        );

        setPadding(new Insets(30, 100, 30, 100));

        idProdutoField.textProperty().addListener(e -> onTextFieldChange());
        nameField.textProperty().addListener(e -> onTextFieldChange());
        precoField.textProperty().addListener(e -> onTextFieldChange());
        stockField.textProperty().addListener(e -> onTextFieldChange());
        categoryComboBox.setOnAction(e -> onTextFieldChange());
        fornecedorComboBox.setOnAction(e -> onTextFieldChange());
        saveButton.setOnAction(e -> gestorInventarioObservable.createProduct(
                idProdutoField.getText().trim(),
                nameField.getText().trim(),
                (Category) categoryComboBox.getSelectionModel().getSelectedItem(),
                Float.parseFloat(precoField.getText().trim()),
                Integer.parseInt(stockField.getText().trim()),
                (Fornecedor) fornecedorComboBox.getSelectionModel().getSelectedItem()
        ));

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
        this.setVisible(gestorInventarioObservable.getCurrentState() == State.ADICIONAR_PRODUTO);
        idProdutoField.clear();
        nameField.clear();
        categoriaField.clear();
        precoField.clear();
        stockField.clear();
        idFornecedorField.clear();
        displayErrorMsg();
    }
}
