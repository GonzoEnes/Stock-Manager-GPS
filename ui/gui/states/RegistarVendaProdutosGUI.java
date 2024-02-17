package gestorInventario.ui.gui.states;

import gestorInventario.logic.GestorInventarioObservable;
import gestorInventario.logic.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static gestorInventario.ui.gui.Constants.STATE_CHANGE;

public class RegistarVendaProdutosGUI extends HBox {
    private final GestorInventarioObservable gestorInventarioObservable;
    private RegistarVendaGUI registarVendaGuiContainer;
    private TextField quantTextField;
    private TextField codBarrasTextField;
    private Product productToAdd;
    private Label totalLabel;
    private Button addProductBtn;
    private ObservableList<Product> cartList = FXCollections.observableArrayList();
    private TableView tableView;
    private Button removeBtn;
    private Button confirmSell;

    public RegistarVendaProdutosGUI(GestorInventarioObservable gestorInventarioObservable, RegistarVendaGUI registarVendaGuiContainer) {
        this.gestorInventarioObservable = gestorInventarioObservable;
        this.registarVendaGuiContainer = registarVendaGuiContainer;
        createViewAndRegisterListeners();
        registerObservable();
        update();
    }

    private void registerObservable(){
        this.gestorInventarioObservable.addPropertyChangeListener(STATE_CHANGE, evt -> update());
    }

    private void enableAddButton() {
        if(productToAdd == null) return;

        Product tmpProduct = new Product(
                productToAdd.getId(), "", null, 0.0f,
                Integer.parseInt(quantTextField.getText()), null);
        addProductBtn.setDisable(
                tmpProduct.getStock() <= 0
                || !isProductStockValid(tmpProduct)
                || tmpProduct.getStock() > productToAdd.getStock()
                || quantTextField.getText().isEmpty()
                || codBarrasTextField.getText().isEmpty());
    }

    private void handleAddProductBtn() {
        productToAdd.setStock(Integer.parseInt(quantTextField.getText()));
        addProductToCart(productToAdd);
        productToAdd = null;
        quantTextField.clear();
        codBarrasTextField.clear();
        updateTotal();
        disableConfirmBtn();
    }

    private void disableConfirmBtn() {
        confirmSell.setDisable(cartList.size() == 0);
    }

    private boolean isProductStockValid(Product newProduct) {
        Product originalProduct = gestorInventarioObservable.checkBarCode(newProduct.getId());
        int totalProductStock = 0;

        for(Product product : cartList) {
            if(newProduct.getId().equals(product.getId()))
                totalProductStock += product.getStock();
        }

        return totalProductStock + newProduct.getStock() <= originalProduct.getStock();
    }

    private void addProductToCart(Product newProduct) {
        if(isProductStockValid(newProduct))
            cartList.add(newProduct);
    }

    private void updateTotal() {
        float total = 0.0f;

        for(Product product : cartList) {
            total += product.getPrice() * product.getStock();
        }

        totalLabel.setText("Total: " + total + "€");
    }

    private void handleTableRowFocus() {
        Product focusedProduct = (Product) tableView.getFocusModel().getFocusedItem();
        removeBtn.setDisable(focusedProduct == null);

        if(focusedProduct == null) return;
    }

    private VBox createAddProductContainer() {
        Label quantLabel = new Label("Quantidade: ");
        quantTextField = new TextField();
        Label codBarrasLabel = new Label("Código de barras: ");
        codBarrasTextField = new TextField();
        addProductBtn = new Button("Adicionar");
        VBox addProductContainer = new VBox();
        HBox quantContainer = new HBox();
        HBox codBarrasContainer = new HBox();

        quantTextField.setDisable(true);

        codBarrasContainer.getChildren().addAll(codBarrasLabel, codBarrasTextField);
        quantContainer.getChildren().addAll(quantLabel, quantTextField);

        addProductContainer.getChildren().addAll(codBarrasContainer, quantContainer, addProductBtn);
        addProductContainer.setPadding(new Insets(20, 20, 20, 20));
        addProductContainer.setSpacing(16);

        codBarrasTextField.textProperty().addListener(e -> {
            try {
                productToAdd = gestorInventarioObservable.checkBarCode(codBarrasTextField.getText().trim());
            } catch(Exception ignored) { }
            System.out.println(productToAdd);
            quantTextField.setDisable(productToAdd == null);
        });

        quantTextField.textProperty().addListener(e -> enableAddButton());

        addProductBtn.setOnAction(e -> handleAddProductBtn());

        return addProductContainer;
    }

    private VBox createProductListView() {
        VBox container = new VBox();
        tableView = new TableView();
        TableColumn<Product, String> nameCol = new TableColumn<>("Nome");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Product, String> priceCol = new TableColumn<>("Preço");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Product, Integer> quantCol = new TableColumn<>("Quantidade");
        quantCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<Product, Integer> barCodeCol = new TableColumn<>("Código de barras");
        barCodeCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        confirmSell = new Button("Confirmar venda");
        tableView.setItems(cartList);
        tableView.getColumns().addAll(barCodeCol, nameCol, priceCol, quantCol);
        totalLabel = new Label("Total: 0.0€");
        removeBtn = new Button("Remover");
        removeBtn.setDisable(true);

        VBox.setMargin(removeBtn, new Insets(0, 0 , 16, 0));
        VBox.setMargin(confirmSell, new Insets(16, 0 , 0, 0));
        container.setAlignment(Pos.TOP_RIGHT);
        container.getChildren().addAll(
                removeBtn,
                tableView,
                totalLabel,
                confirmSell
        );
        container.setPadding(new Insets(16, 0, 16, 0));

        // Create Listeners
        tableView.focusedProperty().addListener(e -> handleTableRowFocus());
        removeBtn.setOnAction(e -> {
            cartList.remove(tableView.getFocusModel().getFocusedIndex());
            removeBtn.setDisable(true);
            updateTotal();
            disableConfirmBtn();
        });
        confirmSell.setOnAction(e -> {
            registarVendaGuiContainer.setCartList(cartList);
            registarVendaGuiContainer.changeState(1);
            gestorInventarioObservable.firePropertyChange(STATE_CHANGE, null, null);
        });
        return container;
    }

    private void createViewAndRegisterListeners(){
        VBox addProductContainer = createAddProductContainer();
        VBox tableViewContainer = createProductListView();
        disableConfirmBtn();

        // Create view
        setAlignment(Pos.CENTER);
        setSpacing(10);
        getChildren().addAll(
                addProductContainer,
                tableViewContainer
        );
    }

    private void update() {
        this.setVisible(registarVendaGuiContainer.getInternalState() == 0);
    }

}
