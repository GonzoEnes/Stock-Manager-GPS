package gestorInventario.ui.gui.states;

import gestorInventario.logic.GestorInventarioObservable;
import gestorInventario.logic.model.Product;
import gestorInventario.logic.model.Venda;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Timestamp;
import java.time.Instant;

import static gestorInventario.ui.gui.Constants.STATE_CHANGE;

public class RegistarVendaFinalizarGUI extends HBox {
    private final GestorInventarioObservable gestorInventarioObservable;
    private RegistarVendaGUI registarVendaGuiContainer;
    private TextField nifTextField;
    private TextField nomeClienteTextField;
    private Button finishBtn;
    private TableView tableView;

    public RegistarVendaFinalizarGUI(GestorInventarioObservable gestorInventarioObservable, RegistarVendaGUI registarVendaGuiContainer) {
        this.gestorInventarioObservable = gestorInventarioObservable;
        this.registarVendaGuiContainer = registarVendaGuiContainer;
        createViewAndRegisterListeners();
        registerObservable();
        update();
    }

    private void registerObservable() {
        this.gestorInventarioObservable.addPropertyChangeListener(STATE_CHANGE, evt -> update());
    }

    private void handleFinishSellBtn() {
        Venda purchase = new Venda(
                gestorInventarioObservable.getLoggedInUser(),
                nifTextField.getText(),
                nomeClienteTextField.getText(),
                getTotalPrice(),
                Timestamp.from(Instant.now()),
                registarVendaGuiContainer.getCartList()
            );

        gestorInventarioObservable.registerPurchase(purchase);

        nifTextField.clear();
        nomeClienteTextField.clear();
        gestorInventarioObservable.firePropertyChange(STATE_CHANGE, null, null);

    }

    private float getTotalPrice() {
        float total = 0.0f;

        for(Product product : registarVendaGuiContainer.getCartList()) {
            total += product.getPrice() * product.getStock();
        }

        return total;
    }

    private VBox createAddProductContainer() {
        Label nifLabel = new Label("NIF: ");
        nifTextField = new TextField();
        Label nomeClienteLabel = new Label("Nome do Cliente: ");
        nomeClienteTextField = new TextField();
        VBox clientInfoContainer = new VBox();
        HBox nifContainer = new HBox();
        HBox nomeClienteContainer = new HBox();

        nomeClienteContainer.getChildren().addAll(nomeClienteLabel, nomeClienteTextField);
        nifContainer.getChildren().addAll(nifLabel, nifTextField);
        clientInfoContainer.getChildren().addAll(nomeClienteContainer, nifContainer);
        clientInfoContainer.setPadding(new Insets(20, 20, 20, 20));
        clientInfoContainer.setSpacing(16);

        return clientInfoContainer;
    }

    private VBox createProductListView() {
        VBox container = new VBox();
        tableView = new TableView();
        TableColumn<Product, String> column1 = new TableColumn<>("Nome");
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, String> column2 = new TableColumn<>("Preço");
        column2.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Integer> column3 = new TableColumn<>("Quantidade");
        column3.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tableView.setItems((ObservableList) registarVendaGuiContainer.getCartList());
        tableView.getColumns().addAll(column1, column2, column3);

        finishBtn = new Button("Finalizar venda");
        finishBtn.setOnAction(e -> handleFinishSellBtn());

        VBox.setMargin(finishBtn, new Insets(16, 0 , 0, 0));
        container.setAlignment(Pos.TOP_RIGHT);
        container.getChildren().addAll(
                tableView,
                finishBtn
        );
        container.setPadding(new Insets(16, 0, 16, 0));

        return container;
    }

    private void createViewAndRegisterListeners(){
        VBox addProductContainer = createAddProductContainer();
        VBox tableViewContainer = createProductListView();

        // Create view
        setAlignment(Pos.CENTER);
        setSpacing(10);
        getChildren().addAll(
                addProductContainer,
                tableViewContainer
        );
    }

    private void update() {
        tableView.setItems((ObservableList) registarVendaGuiContainer.getCartList());
        this.setVisible(registarVendaGuiContainer.getInternalState() == 1);
    }
}
