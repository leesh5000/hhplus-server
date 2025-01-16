package kr.hhplus.be.server.product.infra.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.common.domain.dto.response.PageDetails;
import kr.hhplus.be.server.order.domain.QOrder;
import kr.hhplus.be.server.order.domain.QOrderProduct;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.QProduct;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.domain.service.dto.response.TopSellingProductResult;
import kr.hhplus.be.server.product.infra.persistence.jpa.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public PageDetails<List<Product>> findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> productPage = productJpaRepository.findAll(pageRequest);
        long totalCount = productJpaRepository.count();
        return new PageDetails<>(
                productPage.getNumber(),
                productPage.getSize(),
                totalCount,
                productPage.getTotalPages(),
                productPage.getContent()
        );
    }

    @Override
    public List<Product> findAll(List<Long> ids) {
        return List.of();
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return productJpaRepository.findById(productId);
    }

    @Override
    public List<TopSellingProductResult> queryPopularProducts(Integer days, Integer limit) {
        return this.findTopSellingProductsInLastNDays(days, limit);
    }

    public List<TopSellingProductResult> findTopSellingProductsInLastNDays(int days, int limit) {
        QOrderProduct orderProduct = QOrderProduct.orderProduct;
        QOrder orders = QOrder.order;
        QProduct product = QProduct.product;

        // ID로 조인
        return jpaQueryFactory
                .select(Projections.constructor(TopSellingProductResult.class,
                        product.id,
                        product.name,
                        product.price.amount,
                        orderProduct.quantity.sum().longValue()
                ))
                .from(orderProduct)
                .join(orders).on(orderProduct.order.eq(orders))  // ID로 조인
                .join(product).on(orderProduct.productId.eq(product.id))
                .where(orders.createdAt.goe(LocalDate.now().minusDays(days).atStartOfDay()))
                .groupBy(product.id, product.name)
                .orderBy(orderProduct.quantity.sum().desc())
                .limit(limit)
                .fetch();
    }


    @Override
    public Product getById(Long productId) {
        return productJpaRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }
}
