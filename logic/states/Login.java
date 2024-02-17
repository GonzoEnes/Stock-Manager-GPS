package gestorInventario.logic.states;

import gestorInventario.logic.model.GestorInventario;
import gestorInventario.logic.model.User;

public class Login extends StateAdapter {

    public Login(GestorInventario gestorInventario) {
        super(gestorInventario);
    }

    @Override
    public IState submeteLogin(String email, String password){
        User user = gestorInventario.getUserByCredentials(email, password);

        if(user != null) {
            gestorInventario.setLoggedInUser(user);
            gestorInventario.setErrorMsg(null);
            return new PaginaInicial(gestorInventario);
        } else {
            gestorInventario.setErrorMsg("Credênciais erradas.");
            return new Login(gestorInventario);
        }
    }

    @Override
    public State getState() { return State.LOGIN; }
}