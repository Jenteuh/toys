package be.vdab.toys;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String scale;
    private String description;
    private int inStock;
    private int inOrder;
    private BigDecimal price;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "productlineId")
    private Productline productline;
    @Version
    private int version;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getScale() {
        return scale;
    }

    public String getDescription() {
        return description;
    }

    public int getInStock() {
        return inStock;
    }

    public int getInOrder() {
        return inOrder;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Productline getProductline() {
        return productline;
    }

    public int getVersion() {
        return version;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public void setInOrder(int inOrder) {
        this.inOrder = inOrder;
    }

    public void verlaagInStockEnInOrder(int aantal) {
        if(aantal <= inStock) {
            setInStock(getInStock() - aantal);
            setInOrder(getInOrder() - aantal);
        } else throw new OnvoldoendeStockException(id);
    }
}
