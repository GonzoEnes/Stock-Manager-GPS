package gestorInventario.logic.states;

import gestorInventario.logic.model.GestorInventario;

public class StatusUser extends StateAdapter {

    public StatusUser(GestorInventario gestorInventario) {
        super(gestorInventario);
    }

    @Override
    public IState goToPaginaInicial() {
        gestorInventario.setSelectedUser(null);
        return new PaginaInicial(gestorInventario);
    }

    @Override
    public State getState() { return State.STATUS_USER; }
}
