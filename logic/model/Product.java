package gestorInventario.logic.model;

import java.util.Objects;

public class Product {
    private String name;
    private Category category;
    private String id;
    private float price;
    private int stock;
    private Fornecedor fornecedor;

    public Product(String id, int stock){
        this.name = "";
        this.category = null;
        this.id = id;
        this.price = 0.0f;
        this.stock = stock;
        this.fornecedor = null;
    }

    public Product(String id, String name, Category category, float price, int stock, Fornecedor fornecedor){
        this.name = name;
        this.category = category;
        this.id = id;
        this.price = price;
        this.stock = stock;
        this.fornecedor = fornecedor;
    }

    public Product(String name, Category category, float price, int stock, Fornecedor fornecedor){
        this.name = name;
        this.category = category;
        this.id = "";
        this.price = price;
        this.stock = stock;
        this.fornecedor = fornecedor;
    }

    //gets
    public String getName(){return name;}

    public Category getCategory() { return category; }

    public String getId() {return id;}

    public float getPrice() {return price;}

    public Integer getStock() {return stock;}

    public Fornecedor getFornecedor() {return fornecedor;}

    //sets
    public void setName(String name) {this.name = name;}

    public void setCategory(Category category) {this.category = category;}

    public void setId(String id) {this.id = id;}

    public void setPrice(float price) {this.price = price;}

    public void setStock(int stock) {this.stock = stock;}

    public void setFornecedor(Fornecedor fornecedor) {this.fornecedor = fornecedor;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Float.compare(product.price, price) == 0 && stock == product.stock && fornecedor.equals(product.fornecedor) && name.equals(product.name) && category.equals(product.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, id, price, stock, fornecedor);
    }
}
