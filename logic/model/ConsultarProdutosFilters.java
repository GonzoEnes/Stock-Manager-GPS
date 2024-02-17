package gestorInventario.logic.model;

import java.util.Objects;

public class ConsultarProdutosFilters {
    private String searchBar = "";
    private Category category = null;
    private Fornecedor fornecedor = null;
    private int priceSorting = 0;

    public ConsultarProdutosFilters(String searchBar, Category category, Fornecedor fornecedor, int priceSorting) {
        this.searchBar = searchBar;
        this.category = category;
        this.fornecedor = fornecedor;
        this.priceSorting = priceSorting;
    }

    public ConsultarProdutosFilters() {
    }

    public String getSearchBar() {
        return searchBar;
    }

    public Category getCategory() {
        return category;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public int getPriceSorting() {
        return priceSorting;
    }

    @Override
    public String toString() {
        return "ConsultarProdutosFilters{" +
                "searchBar='" + searchBar + '\'' +
                ", category=" + category +
                ", fornecedor=" + fornecedor +
                ", priceSorting=" + priceSorting +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsultarProdutosFilters that = (ConsultarProdutosFilters) o;
        return priceSorting == that.priceSorting && searchBar.equals(that.searchBar) && Objects.equals(category, that.category) && Objects.equals(fornecedor, that.fornecedor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchBar, category, fornecedor, priceSorting);
    }
}
