package gestorInventario.logic.model;

import java.util.Objects;

public class Fornecedor {
    private Integer id;
    private String name;

    public Fornecedor(Integer id,String name){
        this.id = id;
        this.name = name;
    }

    public Integer getId() {return id;}

    public String getName() {return name;}

    public void setId(Integer id) {this.id = id;}

    public void setName(String name) {this.name = name;}

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fornecedor that = (Fornecedor) o;
        return id.equals(that.id) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
