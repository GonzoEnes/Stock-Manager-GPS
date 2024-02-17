package gestorInventario.logic.states;

import gestorInventario.logic.model.GestorInventario;
import gestorInventario.logic.model.Product;

public class EditarProduto extends StateAdapter {

    public EditarProduto(GestorInventario gestorInventario) {
        super(gestorInventario);
    }

    @Override
    public IState goToPaginaInicial() { return new PaginaInicial(gestorInventario); }

    @Override
    public IState editarProduto(Product product) {
        StringBuilder errorMsgBuilder = new StringBuilder();
        if(product.getName().trim().isEmpty()) {
            errorMsgBuilder.append("Nome não pode estar vazio\n");
        }

        if(product.getCategory() == null || product.getCategory().getId() < 0) {
            errorMsgBuilder.append("Categoria não pode estar vazia\n");
        }

        if(product.getPrice() < 0.0f) {
            errorMsgBuilder.append("Preço inválido\n");
        }

        if(product.getStock() < 0) {
            errorMsgBuilder.append("Stock inválido\n");
        }

        if(product.getFornecedor() == null || product.getFornecedor().getId() < 0) {
            errorMsgBuilder.append("Produto não pode estar vazio\n");
        }

        if(!errorMsgBuilder.toString().trim().isEmpty()) {
            gestorInventario.setErrorMsg(errorMsgBuilder.toString());
            return new EditarProduto(gestorInventario);
        }

        boolean querySucessful = gestorInventario.editProduct(product);

        if(!querySucessful) {
            gestorInventario.setErrorMsg("Erro ao submeter as alterações");
            return new EditarProduto(gestorInventario);
        }
        gestorInventario.setErrorMsg(null);
        gestorInventario.setSelectedProduct(product);
        return new VisualizarProduto(gestorInventario);
    }

    @Override
    public State getState() { /* return this state */ return State.EDITAR_PRODUTO; }
}