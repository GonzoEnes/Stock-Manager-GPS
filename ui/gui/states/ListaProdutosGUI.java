package gestorInventario.ui.gui.states;

import gestorInventario.logic.GestorInventarioObservable;
import gestorInventario.logic.model.Category;
import gestorInventario.logic.model.Fornecedor;
import gestorInventario.logic.model.Product;
import gestorInventario.logic.states.State;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.util.Callback;
import static gestorInventario.ui.gui.Constants.STATE_CHANGE;

public class ListaProdutosGUI extends BorderPane {
    private final GestorInventarioObservable gestorInventarioObservable;
    private final HashMap<Integer, String> priceSortingOptions = new HashMap();
    private ObservableList<String> priceOptions;
    ComboBox priceSortingComboBox;
    ComboBox categoryComboBox;
    ComboBox fornecedorComboBox;
    private TextField searchBarTextField;
    private Button searchBarBtn;
    private Button seeDetailsBtn;
    private Button editProductBtn;
    private Pagination pagination;
    private Product selectedProduct;
    ObservableList<Product> data = FXCollections.observableArrayList();

    public ListaProdutosGUI(GestorInventarioObservable gestorInventarioObservable) {
        this.gestorInventarioObservable = gestorInventarioObservable;
        createViewAndRegisterListeners();
        registerObservable();
        update();
    }

    public void applySearch() {
        String searchBarText = searchBarTextField.getText().trim();

        Category category = null;
        if(categoryComboBox.getSelectionModel().getSelectedIndex() > 0)
            category = (Category) categoryComboBox.getSelectionModel().getSelectedItem();

        Fornecedor fornecedor = null;
        if(fornecedorComboBox.getSelectionModel().getSelectedIndex() > 0)
            fornecedor = (Fornecedor) fornecedorComboBox.getSelectionModel().getSelectedItem();

        int priceSorting = List.of(0, -1, 1).get(priceSortingComboBox.getSelectionModel().getSelectedIndex());

        System.out.println(searchBarText + category + fornecedor + priceSorting);
        gestorInventarioObservable.efetuarPesquisaProdutos(searchBarText, category, fornecedor, priceSorting);
    }

    public int rowsPerPage() {
        return 10;
    }

    public VBox createFiltersViewAndRegisterListeners() {
        ObservableList<Category> categoriesOptions = FXCollections.observableArrayList(gestorInventarioObservable.getCategoriesList());
        categoriesOptions.add(0, new Category(0, ""));
        categoryComboBox = new ComboBox(categoriesOptions);

        priceSortingOptions.put(0, "");
        priceSortingOptions.put(-1, "Preço descendente");
        priceSortingOptions.put(1, "Preço ascendente");
        priceOptions  = FXCollections.observableArrayList(List.of(priceSortingOptions.values().toArray(new String[0])));
        priceSortingComboBox = new ComboBox(priceOptions);

        searchBarTextField = new TextField(gestorInventarioObservable.getConsultarProdutosFilters().getSearchBar().trim());
        searchBarBtn = new Button("Pesquisar");
        HBox searchBarContainer = new HBox(searchBarTextField, searchBarBtn);

        ObservableList<Fornecedor> fornecedoresOptions = FXCollections.observableArrayList(gestorInventarioObservable.getFornecedoresList());
        fornecedoresOptions.add(0, new Fornecedor(0, ""));
        fornecedorComboBox = new ComboBox(fornecedoresOptions);

        if(gestorInventarioObservable.getConsultarProdutosFilters().getCategory() == null)
            categoryComboBox.getSelectionModel().selectFirst();
        else
            categoryComboBox.getSelectionModel().select(gestorInventarioObservable.getConsultarProdutosFilters().getCategory());

        if(gestorInventarioObservable.getConsultarProdutosFilters().getFornecedor() == null)
            fornecedorComboBox.getSelectionModel().selectFirst();
        else
            fornecedorComboBox.getSelectionModel().select(gestorInventarioObservable.getConsultarProdutosFilters().getFornecedor());

        priceSortingComboBox.getSelectionModel().select(
                priceSortingOptions.get(gestorInventarioObservable.getConsultarVendasFilters().getPriceSorting()));

        System.out.println(gestorInventarioObservable.getConsultarProdutosFilters());

        Label categoryFilterLabel = new Label("Filtrar por categoria:");
        Label fornecedorFilterLabel = new Label("Filtrar por fornecedor:");
        Label priceSortingLabel = new Label("Ordenar por preço:");

        VBox categoryFilterContainer = new VBox(categoryFilterLabel, categoryComboBox);
        VBox fornecedorFilterContainer = new VBox(fornecedorFilterLabel, fornecedorComboBox);
        VBox priceSortingContainer = new VBox(priceSortingLabel, priceSortingComboBox);

        HBox filtersContainer = new HBox(
                categoryFilterContainer,
                fornecedorFilterContainer,
                priceSortingContainer
        );
        filtersContainer.setSpacing(8);

        categoryComboBox.setOnAction(e -> applySearch());
        fornecedorComboBox.setOnAction(e -> applySearch());
        priceSortingComboBox.setOnAction(e -> applySearch());
        searchBarBtn.setOnAction(e -> applySearch());

        seeDetailsBtn = new Button("Ver detalhes");
        seeDetailsBtn.setDisable(true);
        seeDetailsBtn.setOnAction(e -> gestorInventarioObservable.selecionarProduto(selectedProduct) );

        editProductBtn = new Button("Editar");
        editProductBtn.setDisable(true);
        editProductBtn.setOnAction(e -> gestorInventarioObservable.goToEditarProduct(selectedProduct) );

        HBox productActionsContainer = new HBox(seeDetailsBtn, editProductBtn);
        productActionsContainer.setSpacing(8);

        VBox container = new VBox();
        container.getChildren().addAll(
                searchBarContainer,
                filtersContainer,
                productActionsContainer
        );
        container.setPadding(new Insets(20));
        return container;
    }

    public TableView createPage(int pageIndex) {
        ObservableList<Product> sample = FXCollections.observableArrayList();
        int initIndex = rowsPerPage() * pageIndex;
        int lastIndex;
        if (initIndex + rowsPerPage() < data.size())
            lastIndex = initIndex + rowsPerPage() - 1;
        else
            lastIndex = data.size()-1;

        for(int i = initIndex; i <= lastIndex; i++)
            sample.add(data.get(i));

        TableView tableView = new TableView();

        TableColumn<Product, String> idCol = new TableColumn<>("Código de barras");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Product, String> nameCol = new TableColumn<>("Nome");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, String> fornecedorCol = new TableColumn<>("Fornecedor");
        fornecedorCol.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));

        TableColumn<Product, String> priceCol = new TableColumn<>("Preço");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, String> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<Product, String> categoryCol = new TableColumn<>("Categoria");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        tableView.getColumns().addAll(
                idCol,
                nameCol,
                fornecedorCol,
                priceCol,
                stockCol,
                categoryCol
        );

        tableView.setItems(sample);
        tableView.setMinHeight(200);

        tableView.focusedProperty().addListener(e -> {
            selectedProduct = (Product) tableView.getFocusModel().getFocusedItem();
            seeDetailsBtn.setDisable(selectedProduct == null);
            editProductBtn.setDisable(selectedProduct == null);
        });

        return tableView;
    }


    private void registerObservable(){
        this.gestorInventarioObservable.addPropertyChangeListener(STATE_CHANGE, evt -> update());
    }

    private void createViewAndRegisterListeners(){
        // Create view

        data = FXCollections.observableArrayList(gestorInventarioObservable.getProductsList());

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
        this.setVisible(gestorInventarioObservable.getCurrentState() == State.LISTA_PRODUTOS);
        createViewAndRegisterListeners();
    }
}

