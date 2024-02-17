package gestorInventario.ui.gui.states;

import gestorInventario.logic.GestorInventarioObservable;
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

public class StatusUserGUI extends BorderPane {
    private final GestorInventarioObservable gestorInventarioObservable;

    public StatusUserGUI(GestorInventarioObservable gestorInventarioObservable) {
        this.gestorInventarioObservable = gestorInventarioObservable;
        createViewAndRegisterListeners();
        registerObservable();
        update();
    }

    private void registerObservable(){
        this.gestorInventarioObservable.addPropertyChangeListener(STATE_CHANGE, evt -> update());
    }

    private void createViewAndRegisterListeners(){
        // Create view
        if(gestorInventarioObservable.getSelectedUser() == null) return;
        User selectedUser = gestorInventarioObservable.getSelectedUser();
        String permissionsLabel = selectedUser.isAdmin() ? "Administrador" : "Normal";

        Label nameLabel = new Label("Nome: " + selectedUser.getName());
        Label emailLabel = new Label("Email: " + selectedUser.getEmail());
        Label userTypeLabel = new Label("Permissões: " + permissionsLabel);

        VBox container = new VBox();
        container.getChildren().addAll(
                nameLabel,
                emailLabel,
                userTypeLabel
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
        this.setVisible(gestorInventarioObservable.getCurrentState() == State.STATUS_USER);
        createViewAndRegisterListeners();
    }
}
