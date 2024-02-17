package gestorInventario.ui.gui.states;

import gestorInventario.logic.GestorInventarioObservable;
import gestorInventario.logic.model.Product;
import gestorInventario.logic.model.Venda;
import gestorInventario.logic.states.State;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static gestorInventario.ui.gui.Constants.STATE_CHANGE;

public class VisualizarVendaGUI extends BorderPane {
    private final GestorInventarioObservable gestorInventarioObservable;

    public VisualizarVendaGUI(GestorInventarioObservable gestorInventarioObservable) {
        this.gestorInventarioObservable = gestorInventarioObservable;
        createViewAndRegisterListeners();
        registerObservable();
        update();
    }

    private void registerObservable(){
        this.gestorInventarioObservable.addPropertyChangeListener(STATE_CHANGE, evt -> update());
    }

    private VBox createInfoView(Venda purchase) {
        Label idLabel = new Label("Id: " + purchase.getId());
        Label sellerNameLabel = new Label("Vendedor: " + purchase.getSeller().getName());
        Label clientNameLabel = new Label("Nome do cliente: " + purchase.getNomeCliente());
        Label nifLabel = new Label("NIF: " + purchase.getNif());
        Label purchaseDateLabel = new Label("Data de venda: " +
                purchase.getTimestamp().toString().substring(0, purchase.getTimestamp().toString().indexOf(".")));
        Label totalLabel = new Label("Total: " + purchase.getTotal() + "€");
        VBox infoContainer = new VBox();

        infoContainer.getChildren().addAll(
                idLabel,
                sellerNameLabel,
                clientNameLabel,
                nifLabel,
                purchaseDateLabel,
                totalLabel
        );
        infoContainer.setSpacing(8);

        return infoContainer;
    }

    private TableView createCartTableView(Venda purchase) {
        TableView tableView = new TableView();
        TableColumn<Product, String> nameCol = new TableColumn<>("Nome");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Product, String> priceCol = new TableColumn<>("Preço");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Product, Integer> quantCol = new TableColumn<>("Quantidade");
        quantCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<Product, Integer> barCodeCol = new TableColumn<>("Código de barras");
        barCodeCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        Button confirmSell = new Button("Confirmar venda");
        tableView.setItems((ObservableList) purchase.getProducts());
        tableView.getColumns().addAll(barCodeCol, nameCol, priceCol, quantCol);
        tableView.setMinWidth(500);

        return tableView;
    }

    private void createViewAndRegisterListeners(){
        // Create view
        if(gestorInventarioObservable.getSelectedPurchase() == null) return;

        VBox infoContainer = createInfoView(gestorInventarioObservable.getSelectedPurchase());
        TableView cartListTable = createCartTableView(gestorInventarioObservable.getSelectedPurchase());
        HBox globalContainer = new HBox();
        globalContainer.getChildren().addAll(infoContainer, cartListTable);
        globalContainer.setPadding(new Insets(60, 100, 60, 100));
        globalContainer.setSpacing(16);

        Button backBtn = new Button("Voltar à página inicial");
        backBtn.setOnAction(e -> gestorInventarioObservable.goToPaginaInicial());
        backBtn.setAlignment(Pos.BOTTOM_LEFT);
        HBox bottomContainer = new HBox();
        bottomContainer.getChildren().add(backBtn);
        HBox.setMargin(backBtn, new Insets(16, 16, 16, 16));

        setCenter(globalContainer);
        setBottom(bottomContainer);
    }

    private void displaySuccessMsg() {
        if(gestorInventarioObservable.getSuccessMsg() == null) return;

        Alert errorAlert = new Alert(Alert.AlertType.CONFIRMATION, gestorInventarioObservable.getSuccessMsg());
        errorAlert.show();

        gestorInventarioObservable.clearSuccessMsg();
    }

    private void update() {
        createViewAndRegisterListeners();
        this.setVisible(gestorInventarioObservable.getCurrentState() == State.VISUALIZAR_VENDAS);
        displaySuccessMsg();
    }
}
