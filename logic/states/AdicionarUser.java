package gestorInventario.logic.states;

import gestorInventario.logic.db.DBCondition;
import gestorInventario.logic.model.GestorInventario;
import gestorInventario.logic.model.User;

import java.util.List;

public class AdicionarUser extends StateAdapter {

    public AdicionarUser(GestorInventario gestorInventario) {
        super(gestorInventario);
    }

    @Override
    public IState goToPaginaInicial() { return new PaginaInicial(gestorInventario); }

    @Override
    public IState adicionarUtilizador(User user) {
        if(user.getEmail().trim().isEmpty() || user.getName().trim().isEmpty()) {
            gestorInventario.setErrorMsg("Por favor preencha todos os campos.");
            return new AdicionarUser(gestorInventario);
        }

        boolean doesUserWithEmailExist = gestorInventario.getUsersByEmail(user.getEmail()).size() > 0;
        if(doesUserWithEmailExist) {
            gestorInventario.setErrorMsg("Utilizador com o email '" + user.getEmail() + "' já existe");
            return new AdicionarUser(gestorInventario);
        }

        User newUser = gestorInventario.createUser(user);
        if(newUser == null) {
            gestorInventario.setErrorMsg("Erro ao adicionar utilizador");
            return new AdicionarUser(gestorInventario);
        }

        gestorInventario.setSelectedUser(newUser);
        return new StatusUser(gestorInventario);
    }

    @Override
    public State getState() { /* return this state */ return State.ADICIONAR_USER; }
}