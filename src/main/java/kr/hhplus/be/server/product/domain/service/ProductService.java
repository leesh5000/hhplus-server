package kr.hhplus.be.server.product.domain.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import kr.hhplus.be.server.common.domain.dto.response.PageDetails;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.domain.service.dto.request.PrepareProductCommand;
import kr.hhplus.be.server.product.domain.service.dto.response.ListProductsResult;
import kr.hhplus.be.server.product.domain.service.dto.response.PrepareProductResult;
import kr.hhplus.be.server.product.domain.service.dto.response.TopSellingProductResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public List<TopSellingProductResult> getPopularProducts(Integer days, Integer limit) {
        return productRepository.queryPopularProducts(days, limit);
    }

    public PageDetails<List<ListProductsResult>> list(int page, int size) {
        PageDetails<List<Product>> pageDetails = productRepository.findAll(page, size);
        List<ListProductsResult> results = pageDetails.content().stream().map(
                        ListProductsResult::from
                )
                .toList();
        return new PageDetails<>(
                pageDetails.page(),
                pageDetails.size(),
                pageDetails.totalElements(),
                pageDetails.totalPages(),
                results
        );
    }

    public List<PrepareProductResult> prepareAll(List<PrepareProductCommand> commands) {
        return commands.stream()
                .map(this::prepare)
                .toList();
    }

    public PrepareProductResult prepare(PrepareProductCommand command) {
        Long productId = command.productId();
        Integer quantity = command.quantity();

        Product product = getById(productId);
        product.decreaseStock(quantity);

        return new PrepareProductResult(product, quantity);
    }

    public Product getById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(
                                ErrorCode.PRODUCT_NOT_FOUND,
                                "ID가 %s인 상품을 찾을 수 없습니다.".formatted(productId))
                );
    }
}
