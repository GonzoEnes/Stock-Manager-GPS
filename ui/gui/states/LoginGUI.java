package gestorInventario.ui.gui.states;

import gestorInventario.logic.GestorInventarioObservable;
import gestorInventario.logic.states.State;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import static gestorInventario.ui.gui.Constants.STATE_CHANGE;

public class LoginGUI extends BorderPane {
    private final GestorInventarioObservable gestorInventarioObservable;
    PasswordField passwordText = new PasswordField();
    TextField emailText = new TextField();

    public LoginGUI(GestorInventarioObservable gestorInventarioObservable) {
        this.gestorInventarioObservable = gestorInventarioObservable;
        createViewAndRegisterListeners();
        registerObservable();
        update();
    }

    private void registerObservable(){
        this.gestorInventarioObservable.addPropertyChangeListener(STATE_CHANGE, evt -> update());
    }

    private void displayErrorMsg() {
        if(gestorInventarioObservable.getErrorMsg() == null) return;

        Alert errorAlert = new Alert(Alert.AlertType.ERROR, gestorInventarioObservable.getErrorMsg());
        errorAlert.show();

        gestorInventarioObservable.clearErrorMsg();
    }

    private void createViewAndRegisterListeners(){
        // Create view
        Label titleLabel = new Label("Bits & bites");
        Label emailLabel = new Label("Email:");
        Label passwordLabel = new Label("Password:");
        Button loginButton = new Button("Login");
        titleLabel.setFont(new Font("Arial", 30));
        VBox emailContainer = new VBox(emailLabel, emailText);
        VBox passwordContainer = new VBox(passwordLabel, passwordText);

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().addAll(
                titleLabel,
                emailContainer,
                passwordContainer,
                loginButton
        );

        setCenter(vbox);
        setPadding(new Insets(100, 250, 30, 250));

        // Create Listeners
        loginButton.setOnAction(e -> {
            if (passwordText.getText().isEmpty() || emailText.getText().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Preencha todos os campos");
                a.show();
            } else
                gestorInventarioObservable.submeteLogin(emailText.getText(), passwordText.getText());
        });
    }

    private void update() {
        this.setVisible(gestorInventarioObservable.getCurrentState() == State.LOGIN);
        displayErrorMsg();
    }
}
