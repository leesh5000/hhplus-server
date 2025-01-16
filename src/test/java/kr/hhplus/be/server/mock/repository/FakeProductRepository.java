package kr.hhplus.be.server.mock.repository;

import kr.hhplus.be.server.common.domain.dto.response.PageDetails;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.domain.service.dto.response.TopSellingProductResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeProductRepository implements ProductRepository {

    private final AtomicLong autoIncrementedId = new AtomicLong(0);
    private final List<Product> productData = Collections.synchronizedList(
            new ArrayList<>(
                    List.of()
            )
    );

    @Override
    public PageDetails<List<Product>> findAll(int page, int size) {
        // 유효성 검사: page는 0 이상, size는 1 이상이어야 함
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page number must be non-negative and size must be greater than zero.");
        }

        // 데이터가 비어있을 경우 빈 결과 반환
        if (productData == null || productData.isEmpty()) {
            return new PageDetails<>(page, size, 0L, 0, List.of());
        }

        // fromIndex와 toIndex 계산
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, productData.size());

        // 시작 인덱스가 데이터 크기를 초과할 경우 빈 리스트 반환
        if (fromIndex >= productData.size()) {
            return new PageDetails<>(page, size, (long) productData.size(), getTotalPages(size), List.of());
        }

        // 유효한 페이지의 데이터 반환
        return new PageDetails<>(
                page,
                size,
                (long) productData.size(),
                getTotalPages(size),
                productData.subList(fromIndex, toIndex)
        );
    }

    @Override
    public List<Product> findAll(List<Long> ids) {
        return List.of();
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return Optional.empty();
    }

    @Override
    public List<TopSellingProductResult> queryPopularProducts(Integer days, Integer limit) {
        return List.of();
    }

    @Override
    public Product getById(Long productId) {
        return productData.stream()
                .filter(item -> item.getId().equals(productId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }

    private int getTotalPages(int size) {
        return (int) Math.ceil((double) productData.size() / size);
    }


    public void saveAll(List<Product> products) {
        products.forEach(this::save);
    }

    public void save(Product product) {
        productData.add(product);
    }
}
