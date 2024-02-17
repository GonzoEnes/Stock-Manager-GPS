package gestorInventario.ui.gui.states;

import gestorInventario.logic.GestorInventarioObservable;
import gestorInventario.logic.states.State;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static gestorInventario.ui.gui.Constants.STATE_CHANGE;

public class AdicionarUserGUI extends BorderPane {
    private final GestorInventarioObservable gestorInventarioObservable;
    private TextField emailField, nameField;
    private CheckBox isAdminField;
    Button submitFormButton;

    public AdicionarUserGUI(GestorInventarioObservable gestorInventarioObservable) {
        this.gestorInventarioObservable = gestorInventarioObservable;
        createViewAndRegisterListeners();
        registerObservable();
        update();
    }

    private void registerObservable(){
        this.gestorInventarioObservable.addPropertyChangeListener(STATE_CHANGE, evt -> update());
    }

    private boolean isEmailValid(String emailFieldText) {
        return !emailFieldText.trim().isEmpty() && emailFieldText.contains("@") && emailFieldText.contains(".") && emailFieldText.length() < 50;
    }

    private boolean isNameValid(String nameFieldText) {
        return !nameFieldText.trim().isEmpty() && nameFieldText.length() < 50;
    }

    private boolean validateForm(String nameFieldText, String emailFieldText) {
        return isEmailValid(emailFieldText) && isNameValid(nameFieldText);
    }

    private void onTextFieldChange() {
        submitFormButton.setDisable(!validateForm(nameField.getText(), emailField.getText()));
    }

    private void createViewAndRegisterListeners(){
        // Create view
        nameField = new TextField();
        emailField = new TextField();
        isAdminField = new CheckBox();
        submitFormButton = new Button("Submeter");
        VBox container = new VBox();

        container.setAlignment(Pos.CENTER);
        container.setSpacing(10);
        container.getChildren().addAll(
                new VBox(new Label("Nome:  "), nameField),
                new VBox(new Label("Email:  "), emailField),
                new VBox(new Label("Admin:  "), isAdminField),
                submitFormButton
        );

        Button backBtn = new Button("Voltar");
        backBtn.setOnAction(e -> gestorInventarioObservable.goToPaginaInicial());
        backBtn.setAlignment(Pos.BOTTOM_LEFT);
        HBox bottomContainer = new HBox();
        bottomContainer.getChildren().add(backBtn);
        HBox.setMargin(backBtn, new Insets(16, 16, 16, 16));

        setBottom(bottomContainer);
        setCenter(container);
        setPadding(new Insets(30, 100, 30, 100));

        // Create Listeners
        nameField.textProperty().addListener(e -> onTextFieldChange());
        emailField.textProperty().addListener(e -> onTextFieldChange());
        isAdminField.textProperty().addListener(e -> onTextFieldChange());
        submitFormButton.setOnAction(e -> gestorInventarioObservable.createUser(
                nameField.getText(), emailField.getText(), isAdminField.isSelected()
        ));
    }

    private void displayErrorMsg() {
        if(gestorInventarioObservable.getErrorMsg() == null) return;

        Alert errorAlert = new Alert(Alert.AlertType.ERROR, gestorInventarioObservable.getErrorMsg());
        errorAlert.show();

        gestorInventarioObservable.clearErrorMsg();
    }

    private void update() {
        this.setVisible(gestorInventarioObservable.getCurrentState() == State.ADICIONAR_USER);
        submitFormButton.setDisable(true);
        nameField.clear();
        emailField.clear();
        isAdminField.setSelected(false);
        displayErrorMsg();
    }
}
