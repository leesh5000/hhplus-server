package kr.hhplus.be.server.product.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.domain.BaseEntity;
import kr.hhplus.be.server.common.domain.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "PRODUCT")
@Entity
public class Product extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    private String name;
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price"))
    private Point price;
    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = ALL)
    @JoinColumn(name = "id", referencedColumnName = "product_id", nullable = false)
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

    public void decreaseStock(Integer quantity) {
        inventory.decrease(quantity);
    }
}
