package gestorInventario.logic.states;

import gestorInventario.logic.model.GestorInventario;
import gestorInventario.logic.model.Product;
import gestorInventario.logic.model.User;
import gestorInventario.logic.model.Venda;

import java.sql.Timestamp;
import java.time.Instant;

public class RegistarVenda extends StateAdapter {

    public RegistarVenda(GestorInventario gestorInventario) {
        super(gestorInventario);
    }

    @Override
    public IState goToPaginaInicial() { return new PaginaInicial(gestorInventario); }

    @Override
    public IState submeterVenda(Venda purchase) {
        if(purchase.getSeller() == null || purchase.getTotal() < 0 || purchase.getProducts().size() == 0)
            return new RegistarVenda(gestorInventario);

        User seller = gestorInventario.getUserById(purchase.getSeller().getId());
        if(seller == null) return new RegistarVenda(gestorInventario);

        float total = 0.0f;

        for(Product cartProduct : purchase.getProducts()) {
            Product prod = gestorInventario.checkBarCode(cartProduct.getId());
            if(prod == null || cartProduct.getStock() > prod.getStock()) return new RegistarVenda(gestorInventario);

            total += prod.getPrice() * cartProduct.getStock();
        }

        purchase.setTotal(total);
        purchase.setTimestamp(Timestamp.from(Instant.now()));

        Venda newPurchase = gestorInventario.registerPurchage(purchase);
        gestorInventario.setSelectedPurchase(newPurchase);
        if(newPurchase == null) {
            gestorInventario.setErrorMsg("Ocorreu um erro ao registar venda");
            return new RegistarVenda(gestorInventario);
        }

        gestorInventario.setSuccessMsg("Venda registada com sucesso");
        return new VisualizarVenda(gestorInventario);
    }

    @Override
    public State getState() { return State.REGISTAR_VENDA; }
}