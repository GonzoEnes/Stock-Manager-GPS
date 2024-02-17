package gestorInventario.ui.gui.states;

import gestorInventario.logic.GestorInventarioObservable;
import gestorInventario.logic.model.Product;
import gestorInventario.logic.model.User;
import gestorInventario.logic.model.Venda;
import gestorInventario.logic.states.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static gestorInventario.ui.gui.Constants.STATE_CHANGE;
import static javafx.application.Application.launch;

public class ConsultarVendasGUI extends BorderPane {
    private final GestorInventarioObservable gestorInventarioObservable;
    private ObservableList<Venda> data;
    private ComboBox sellersComboBox;
    private ComboBox priceSortingComboBox;
    private final HashMap<Integer, String> priceSortingOptions = new HashMap();
    private ObservableList<String> optionsPreco;
    private TextField nifTextField;
    private Button nifSearchButton;
    private Button seeDetailsBtn;
    private Pagination pagination;
    private Venda selectedVenda;
    private DatePicker datePicker;

    public ConsultarVendasGUI(GestorInventarioObservable gestorInventarioObservable) {
        this.gestorInventarioObservable = gestorInventarioObservable;
        createViewAndRegisterListeners();
        registerObservable();
        update();
    }

    public void applySearch() {
        String nif = nifTextField.getText().trim();
        User seller = null;
        if(sellersComboBox.getSelectionModel().getSelectedIndex() > 0)
            seller = (User) sellersComboBox.getSelectionModel().getSelectedItem();

        Timestamp timestamp = null;

        if(datePicker.getValue() != null)
            timestamp = Timestamp.valueOf(datePicker.getValue().atTime(0,0));

        int priceSorting = List.of(0, -1, 1).get(priceSortingComboBox.getSelectionModel().getSelectedIndex());

        System.out.println();
        gestorInventarioObservable.efetuarPesquisaVendas(nif, seller, priceSorting, timestamp);
    }

    public int itemsPerPage() {
        return 1;
    }

    public int rowsPerPage() {
        return 10;
    }

    public VBox createFiltersViewAndRegisterListeners() {
        ObservableList<User> sellersOptions = FXCollections.observableArrayList(gestorInventarioObservable.getUsers());
        sellersOptions.add(0, new User("", "", "", false));
        sellersComboBox = new ComboBox(sellersOptions);
        priceSortingComboBox = new ComboBox(optionsPreco);
        nifTextField = new TextField(gestorInventarioObservable.getConsultarVendasFilters().getNif().trim());
        datePicker = new DatePicker();

        if(gestorInventarioObservable.getConsultarVendasFilters().getSeller() == null)
            sellersComboBox.getSelectionModel().selectFirst();
        else
            sellersComboBox.getSelectionModel().select(gestorInventarioObservable.getConsultarVendasFilters().getSeller());

        priceSortingComboBox.getSelectionModel().select(
                priceSortingOptions.get(gestorInventarioObservable.getConsultarVendasFilters().getPriceSorting()));

        if(gestorInventarioObservable.getConsultarVendasFilters().getTimestamp() != null)
            datePicker.setValue(gestorInventarioObservable.getConsultarVendasFilters().getTimestamp().toLocalDateTime().toLocalDate());

        Label sellerLabel = new Label("Filtrar por vendedor:");
        HBox nifSearchContainer = new HBox();
        VBox filtersContainer = new VBox();
        HBox sellerFilteringContainer = new HBox();
        VBox priceSortingContainer = new VBox();
        Label priceLabel = new Label("Ordene por:");
        Button clearDate = new Button("x");
        HBox datePickerContainer = new HBox(datePicker, clearDate);

        nifSearchButton = new Button("Pesquisar");

        priceSortingContainer.getChildren().addAll(priceLabel);
        priceSortingContainer.getChildren().addAll(priceSortingComboBox);
        priceSortingContainer.setPadding(new Insets(-17));

        sellerFilteringContainer.getChildren().addAll(sellersComboBox, priceSortingContainer, datePickerContainer);
        sellerFilteringContainer.setSpacing(60);

        filtersContainer.getChildren().addAll(sellerLabel, sellerFilteringContainer);

        nifSearchContainer.getChildren().addAll(nifTextField, nifSearchButton);
        nifSearchContainer.setSpacing(10);

        nifSearchButton.setOnAction(e -> applySearch());
        sellersComboBox.setOnAction(e -> applySearch());
        priceSortingComboBox.setOnAction(e -> applySearch());
        datePicker.setOnAction(e -> applySearch());
        clearDate.setOnAction(e -> {
            datePicker.setValue(null);
            applySearch();
        });

        nifSearchContainer.setAlignment(Pos.CENTER_LEFT);

        seeDetailsBtn = new Button("Ver detalhes");
        seeDetailsBtn.setDisable(true);
        seeDetailsBtn.setOnAction(e -> gestorInventarioObservable.selecionarVenda(selectedVenda));

        VBox container = new VBox();
        container.getChildren().addAll(
                new Label("Consultar Vendas"),
                nifSearchContainer,
                filtersContainer,
                seeDetailsBtn
        );
        container.setPadding(new Insets(20));
        return container;
    }

    public TableView createPage(int pageIndex) {
        ObservableList<Venda> sample = FXCollections.observableArrayList();
        int initIndex = rowsPerPage() * pageIndex;
        int lastIndex;
        if (initIndex + rowsPerPage() < data.size())
            lastIndex = initIndex + rowsPerPage() - 1;
        else
            lastIndex = data.size()-1;

        for(int i = initIndex; i <= lastIndex; i++)
            sample.add(data.get(i));

        VBox box = new VBox(6);
        TableView tableView = new TableView();

        TableColumn<Venda, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Venda, String> sellerCol = new TableColumn<>("Vendedor");
        sellerCol.setCellValueFactory(new PropertyValueFactory<>("seller"));

        TableColumn<Venda, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<Venda, String> clientNameCol = new TableColumn<>("Nome Cliente");
        clientNameCol.setCellValueFactory(new PropertyValueFactory<>("nomeCliente"));

        TableColumn<Venda, String> clientNifCol = new TableColumn<>("NIF");
        clientNifCol.setCellValueFactory(new PropertyValueFactory<>("nif"));

        TableColumn<Venda, String> dateCol = new TableColumn<>("Data");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        tableView.getColumns().addAll(
                idCol,
                sellerCol,
                totalCol,
                clientNameCol,
                clientNifCol,
                dateCol
        );

        tableView.setItems(sample);
        tableView.setMinHeight(200);

        tableView.focusedProperty().addListener(e -> {
            selectedVenda = (Venda) tableView.getFocusModel().getFocusedItem();
            seeDetailsBtn.setDisable(selectedVenda == null);
        });

        return tableView;
    }


    private void registerObservable(){
        this.gestorInventarioObservable.addPropertyChangeListener(STATE_CHANGE, evt -> update());
    }

    private void createViewAndRegisterListeners(){
        // Create view

        data = FXCollections.observableArrayList(gestorInventarioObservable.getPurchasesList());
        priceSortingOptions.put(0, "");
        priceSortingOptions.put(-1, "Preço descendente");
        priceSortingOptions.put(1, "Preço ascendente");
        optionsPreco = FXCollections.observableArrayList(List.of(priceSortingOptions.values().toArray(new String[0])));

        pagination = new Pagination((data.size() / rowsPerPage() + 1), 0);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                if (pageIndex > data.size() / rowsPerPage() + 1) {
                    return null;
                } else {
                    return createPage(pageIndex);
                }
            }

        });

        AnchorPane anchor = new AnchorPane();
        AnchorPane.setTopAnchor(pagination, 10.0);
        AnchorPane.setRightAnchor(pagination, 10.0);
        AnchorPane.setBottomAnchor(pagination, 10.0);
        AnchorPane.setLeftAnchor(pagination, 10.0);
        anchor.getChildren().addAll(pagination);

        VBox filtersContainers = createFiltersViewAndRegisterListeners();

        Button backBtn = new Button("Voltar à página inicial");
        backBtn.setOnAction(e -> gestorInventarioObservable.goToPaginaInicial());
        backBtn.setAlignment(Pos.BOTTOM_LEFT);
        HBox bottomContainer = new HBox();
        bottomContainer.getChildren().add(backBtn);
        HBox.setMargin(backBtn, new Insets(16, 16, 16, 16));

        setTop(filtersContainers);
        setCenter(anchor);
        setBottom(bottomContainer);

        // Create Listeners

    }


    private void update() {
        getChildren().clear();
        this.setVisible(gestorInventarioObservable.getCurrentState() == State.CONSULTAR_VENDAS);
        createViewAndRegisterListeners();
    }



}
