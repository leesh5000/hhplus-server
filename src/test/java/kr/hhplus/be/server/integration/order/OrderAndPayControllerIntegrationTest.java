package kr.hhplus.be.server.integration.order;

import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import kr.hhplus.be.server.coupon.domain.service.CouponService;
import kr.hhplus.be.server.mock.domain.ProductFixture;
import kr.hhplus.be.server.mock.domain.UserFixture;
import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.OrderProduct;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.domain.service.OrderService;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.domain.service.ProductService;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.concurrent.Executors.newFixedThreadPool;
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
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

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

    @DisplayName("""
            동시에 50명의 유저가 각각 한 번씩
            재고가 30개인 상품을 주문하면,
            30개의 주문만 성공해야 하고, 상품은 재고가 0이 되어야 한다.
            """)
    @Test
    void order_and_pay_test_concurrency() throws Exception {

        // 50명의 유저 생성
        int userCount = 50;
        for (int i = 0; i < userCount; i++) {
            User user = UserFixture.create();
            userRepository.save(user);
        }

        // 상품 재고 30개로 초기화
        productRepository.save(
                ProductFixture.create(null, 30)
        );

        Product product = productService.getById(1L);
        int stock = product.getStock();
        Long totalOrderCount = orderRepository.count();

        // 동시 요청을 위한 스레드 생성
        ExecutorService executorService = newFixedThreadPool(50);
        List<Callable<Boolean>> tasks = new ArrayList<>();

        // when : 50명의 유저가 각각 한 번씩 재고가 30개인 상품을 주문한다.
        for (int i = 0; i < 50; i++) {
            tasks.add(() -> {
                mockMvc.perform(
                                MockMvcRequestBuilders.post("/api/v1/orders")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("""
                                                {
                                                  "userId": 100,
                                                  "products": [
                                                    {
                                                      "productId": 1,
                                                      "quantity": 1
                                                    }
                                                  ],
                                                  "applyCouponIds": []
                                                }
                                                """)
                        )
                        .andExpect(status().isCreated());
                return true;
            });
        }

        // 모든 작업을 동시 실행
        executorService.invokeAll(tasks);
        executorService.shutdown();

        // then : 30개의 주문만 성공해야 하고, 상품은 재고가 0이 되어야 한다.
        Product updatedProduct = productService.getById(1L);
        Assertions.assertThat(updatedProduct.getStock()).isEqualTo(0);

        // then : 30개의 주문만 성공해야 한다.
        Long updatedOrderCount = orderRepository.count();
        Assertions.assertThat(updatedOrderCount - totalOrderCount).isEqualTo(30);

    }

    @DisplayName("""
            잔액이 100,000 포인트인 유저가
            가격이 30,000 포인트인 상품을 동시에 4번 주문하면,
            주문은 3개만 성공해야 하고, 잔액은 10,000 포인트이어야 한다.
            """)
    @Test
    void order_and_pay_test_concurrency_with_point() throws Exception {

        // given : 잔액이 100,000 포인트인 유저 생성
        User user = UserFixture.create(100L, 100000L);
        userRepository.save(user);

        // given : 가격이 30,000 포인트인 상품 생성
        Product product = ProductFixture.create(1L, 30000);
        productRepository.save(product);

        // when : 가격이 30,000 포인트인 상품을 동시에 4번 주문한다.
        ExecutorService executorService = newFixedThreadPool(4);
        List<Callable<Boolean>> tasks = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            tasks.add(() -> {
                mockMvc.perform(
                                MockMvcRequestBuilders.post("/api/v1/orders")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("""
                                                {
                                                  "userId": 100,
                                                  "products": [
                                                    {
                                                      "productId": 1,
                                                      "quantity": 1
                                                    }
                                                  ],
                                                  "applyCouponIds": []
                                                }
                                                """)
                        )
                        .andExpect(status().isCreated());
                return true;
            });
        }

        // 모든 작업을 동시 실행
        executorService.invokeAll(tasks);
        executorService.shutdown();

        // then : 주문은 3개만 성공해야 하고, 잔액은 10,000 포인트이어야 한다.
        User updatedUser = userService.getById(100L);
        Assertions.assertThat(updatedUser.getPoint()).isEqualTo(10000L);
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
