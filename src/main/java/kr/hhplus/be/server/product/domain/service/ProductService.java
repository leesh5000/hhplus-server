package kr.hhplus.be.server.product.domain.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.domain.dto.response.PageDetails;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.domain.service.dto.response.ListProductsDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getPopularProducts(Integer days, Integer limit) {
        return productRepository.findPopularProducts(days, limit);
    }

    public PageDetails<List<ListProductsDetail>> list(int page, int size) {
        PageDetails<List<Product>> pageDetails = productRepository.findAll(page, size);
        List<ListProductsDetail> results = pageDetails.content().stream().map(
                        ListProductsDetail::from
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
}
