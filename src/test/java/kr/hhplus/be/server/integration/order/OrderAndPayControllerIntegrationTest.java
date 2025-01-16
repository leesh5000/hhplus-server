package kr.hhplus.be.server.integration.order;

import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import kr.hhplus.be.server.coupon.domain.service.CouponService;
import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.OrderProduct;
import kr.hhplus.be.server.order.domain.service.OrderService;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.service.ProductService;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("API 통합 테스트 : 주문/결제")
@AutoConfigureMockMvc
@SpringBootTest
public class OrderAndPayControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private OrderService orderService;

    @DisplayName("""
            사용자 ID, 주문할 상품 목록, 사용할 쿠폰 목록을 입력하여
            
            주문/결제 요청을 하면,
            
            {201 Created} 응답과
            Location 헤더에 {생성된 주문 URI}가 포함되어야 하고,
            
            사용자 잔액에서 {결제 금액이 차감}되어야 하고,
            상품에서 주문한 양 만큼의 {재고가 감소}되어야 하고,
            사용한 쿠폰은 {사용 처리 상태}가 되어야 하고,
            
            응답으로 받은 생성된 주문 URI 의 ID를 통해 주문 정보를 조회하면,
            
            주문 정보의
            
            {사용자 ID가 주문자 ID와 일치}하고,
            {주문 상품 목록이 주문한 상품 목록}과 일치하고,
            {주문 금액이 주문 금액}과 일치하고,
            """)
    @SqlGroup({
            @Sql(value = "/sql/insert_order_and_pay_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete_order_and_pay_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    void order_and_pay_test() throws Exception {

        // given : 사용자 ID, 주문할 상품 목록, 사용할 쿠폰 목록 (resources/sql/data.sql 참고)
        User user = userService.getById(100L);
        Point userPoint = user.getPoint();
        Product product1 = productService.getById(100L);
        Product product2 = productService.getById(101L);
        Product product3 = productService.getById(102L);
        Coupon coupon = couponService.getById(1L);
        Point totalOrderPrice = product1.getPrice().multiply(2)
                .add(product2.getPrice().multiply(3))
                .add(product3.getPrice().multiply(1));
        Point totalPaymentPrice = totalOrderPrice.subtract(coupon.getDiscountAmount());

        // when : 주문/결제 요청을 하면
        AtomicReference<Long> orderId = new AtomicReference<>();
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "userId": 100,
                                          "products": [
                                            {
                                              "productId": 100,
                                              "quantity": 2
                                            },
                                            {
                                              "productId": 101,
                                              "quantity": 3
                                            },
                                            {
                                              "productId": 102,
                                              "quantity": 1
                                            }
                                          ],
                                          "applyCouponIds": [
                                            1
                                          ]
                                        }
                                        """)
                )
                // then 1 : 201 Created 응답해야 한다.
                .andExpect(status().isCreated())
                // then 2 : Location 헤더에 생성된 주문 URI 가 포함되어야 한다.
                .andExpect(header().string("Location", matchesPattern("http://localhost/api/v1/orders/\\d+")))
                .andDo(result -> {
                    // 생성된 주문 URI 에서 ID 추출
                    extractAndSetOrderIdFromLocation(result, orderId);
                })
        ;

        // then 3 : 사용자 잔액에서 주문 금액이 차감되어야 한다.
        User updatedUser = userService.getById(100L);
        Point updatedUserPoint = updatedUser.getPoint();
        Assertions.assertThat(updatedUserPoint).isEqualTo(userPoint.subtract(totalPaymentPrice));

        // then 4 : 상품에서 주문한 양 만큼의 재고가 감소되어야 한다.
        Product updatedProduct1 = productService.getById(100L);
        Product updatedProduct2 = productService.getById(101L);
        Product updatedProduct3 = productService.getById(102L);
        Assertions.assertThat(updatedProduct1.getStock()).isEqualTo(product1.getStock() - 2);
        Assertions.assertThat(updatedProduct2.getStock()).isEqualTo(product2.getStock() - 3);
        Assertions.assertThat(updatedProduct3.getStock()).isEqualTo(product3.getStock() - 1);

        // then 5 : 사용한 쿠폰은 사용 처리 상태가 되어야 한다.
        IssuedCoupon updatedIssuedCoupon = couponService.getIssuedCouponById(1L);
        Assertions.assertThat(updatedIssuedCoupon.isUsed()).isTrue();

        // then 6 : 응답으로 받은 생성된 주문 URI 의 ID를 통해 주문 정보를 조회할 수 있어야 한다.
        Order order = orderService.getById(orderId.get());

        // then 7 : 주문 정보의 사용자 ID가 주문자 ID와 일치해야 한다.
        Assertions.assertThat(order.getUserId()).isEqualTo(100L);

        // then 8 : 주문 상품 목록이 주문한 상품 목록과 일치해야 한다.
        Assertions.assertThat(order.getOrderProducts())
                .extracting(OrderProduct::getProductId)
                .containsExactlyInAnyOrder(100L, 101L, 102L);
        Assertions.assertThat(order.getOrderProducts())
                .extracting(OrderProduct::getQuantity)
                .containsExactlyInAnyOrder(2, 3, 1);

        // then 9 : 주문 금액이 주문 금액과 일치해야 한다.
        Assertions.assertThat(order.getOrderPrice()).isEqualTo(totalOrderPrice);

    }

    private static void extractAndSetOrderIdFromLocation(MvcResult result, AtomicReference<Long> orderId) {
        String location = result.getResponse().getHeader("Location");
        if (location == null) {
            throw new RuntimeException("Test Failed : Location Header Not Found");
        }
        Matcher matcher = Pattern.compile("/(\\d+)").matcher(location);
        if (matcher.find()) {
            orderId.set(Long.parseLong(matcher.group(1)));
        }
    }

}
