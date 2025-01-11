package kr.hhplus.be.server.product.domain.repository;

import kr.hhplus.be.server.common.domain.dto.response.PageDetails;
import kr.hhplus.be.server.product.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductQueryRepository {
    PageDetails<List<Product>> findAll(int page, int size);
    List<Product> findAll(List<Long> ids);
    Optional<Product> findById(Long productId);
    List<Product> findPopularProducts(Integer days, Integer limit);
}
