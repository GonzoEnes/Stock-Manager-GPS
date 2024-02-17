package gestorInventario.logic.states;

import gestorInventario.logic.model.GestorInventario;
import gestorInventario.logic.model.Product;

public class AdicionarProduto extends StateAdapter {

    public AdicionarProduto(GestorInventario gestorInventario) {
        super(gestorInventario);
    }

    @Override
    public IState goToPaginaInicial() { return new PaginaInicial(gestorInventario); }

    @Override
    public IState adicionarProduto(Product product) {
        if(product.getCategory() == null || product.getFornecedor() == null || product.getName().trim().isEmpty()
        || product.getPrice() <= 0.0f || product.getStock() <= 0)
            return new AdicionarProduto(gestorInventario);

        boolean doesProductExists = gestorInventario.checkBarCode(product.getId()) != null;

        if(doesProductExists) {
            gestorInventario.setErrorMsg("Produto com o mesmo código de barras já existe");
            return new AdicionarProduto(gestorInventario);
        }

        Product newProduct = gestorInventario.createProduct(product);
        if(newProduct == null) return new AdicionarProduto(gestorInventario);

        gestorInventario.setSelectedProduct(newProduct);
        return new VisualizarProduto(gestorInventario);
    }

    @Override
    public State getState() { /* return this state */ return State.ADICIONAR_PRODUTO; }
}