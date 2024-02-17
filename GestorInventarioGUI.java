package gestorInventario;

import gestorInventario.logic.GestorInventarioObservable;
import gestorInventario.logic.StateMachine;
import gestorInventario.logic.db.DB;
import gestorInventario.logic.model.GestorInventario;
import gestorInventario.ui.gui.Root;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class GestorInventarioGUI extends Application {
    @Override
    public void start(Stage primaryStage) throws SQLException {
        DB db = new DB();
        GestorInventario gestorInventario = new GestorInventario(db);
        StateMachine stateMachine = new StateMachine(gestorInventario);
        GestorInventarioObservable gestorInventarioObservable = new GestorInventarioObservable(stateMachine);
        Root root = new Root(gestorInventarioObservable);

        System.out.println("Fiz um print");
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestor de Inventário");
        primaryStage.setOnCloseRequest(windowEvent -> Platform.exit());
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
