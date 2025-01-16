package kr.hhplus.be.server.mock;

import kr.hhplus.be.server.common.domain.service.DateTimeHolder;
import kr.hhplus.be.server.coupon.application.IssueCouponUseCase;
import kr.hhplus.be.server.coupon.domain.service.CouponService;
import kr.hhplus.be.server.coupon.interfaces.IssueCouponController;
import kr.hhplus.be.server.coupon.interfaces.ListCouponsController;
import kr.hhplus.be.server.mock.repository.FakeCouponRepository;
import kr.hhplus.be.server.mock.repository.FakeProductRepository;
import kr.hhplus.be.server.mock.repository.FakeUserRepository;
import kr.hhplus.be.server.product.domain.service.ProductService;
import kr.hhplus.be.server.product.interfaces.ListProductsController;
import kr.hhplus.be.server.user.domain.service.UserService;
import kr.hhplus.be.server.user.interfaces.ChargePointController;
import kr.hhplus.be.server.user.interfaces.CheckPointController;

import java.time.LocalDateTime;

public class TestContainers {

    /** Repository **/
    public final FakeUserRepository userRepository;
    public final FakeCouponRepository couponRepository;
    public final FakeProductRepository productRepository;

    /** Application Service **/
    public final IssueCouponUseCase issueCouponUseCase;

    /** Domain Service **/
    public final UserService userService;
    public final CouponService couponService;
    public final ProductService productService;

    /** Controller **/
    public final ChargePointController chargePointController;
    public final CheckPointController checkPointController;
    public final IssueCouponController issueCouponController;
    public final ListCouponsController listCouponsController;
    public final ListProductsController listProductsController;

    /** Infra **/
    public final DateTimeHolder dateTimeHolder;

    public TestContainers(DateTimeHolder dateTimeHolder) {

        this.dateTimeHolder = dateTimeHolder;

        this.userRepository = new FakeUserRepository();
        this.couponRepository = new FakeCouponRepository();
        this.productRepository = new FakeProductRepository();

        this.userService = new UserService(userRepository);
        this.couponService = new CouponService(couponRepository, dateTimeHolder);
        this.productService = new ProductService(productRepository);
        this.issueCouponUseCase = new IssueCouponUseCase(userService, couponService);

        this.chargePointController = new ChargePointController(userService);
        this.checkPointController = new CheckPointController(userService);
        this.issueCouponController = new IssueCouponController(issueCouponUseCase);
        this.listCouponsController = new ListCouponsController(couponService);
        this.listProductsController = new ListProductsController(productService);
    }

    public TestContainers() {
        this(LocalDateTime::now);
    }
}
