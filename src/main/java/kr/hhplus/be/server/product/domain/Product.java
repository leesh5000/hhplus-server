package kr.hhplus.be.server.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import kr.hhplus.be.server.common.domain.BaseEntity;
import kr.hhplus.be.server.common.domain.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class Product extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    private String name;
    private Point price;
    @OneToOne(optional = false, cascade = ALL)
    private ProductInventory inventory = new ProductInventory();

    public Product(Long id, String name, Integer price, Integer stock) {
        this.id = id;
        this.name = name;
        this.price = new Point(price);
        this.inventory = new ProductInventory(stock);
    }

    public Integer getStock() {
        return inventory.getStock();
    }

    public Boolean isSaved() {
        return id != null;
    }

    public void decreaseStock(Integer quantity) {
        inventory.decrease(quantity);
    }
}
