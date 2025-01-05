```mermaid
classDiagram
    
    class User {
        -id: Long
        -name: String
        -coupons: List~UserCoupon~
        -balance: Money
        +charge(Money): UserBalance
    }
    
    User "1" --> "many" UserCoupon
    
    class UserBalance {
        -id: Long
        -user: User
        -usage: Money
        -reason: String
    }
    
    class CouponMold {
        -id: Long
        -discountAmount: Money
        -quantity: int
        -expirationDate: Date
        +issue(userId): Coupon
    }

    class UserCoupon {
        -id: Long
        -owner: User
        -mold: CouponMold
        -discountAmount: Money
        -expirationDate: Date
        +isValid(): boolean
    }

    UserCoupon "1" --> "1" CouponMold
    
    UserBalance "1" --> "1" User
    
    class Order {
        -orderItems: List~OrderProduct~
        -totalPrice: Money
        -state: OrderState
        -orderer: Orderer
        +startOrder()
        +completePayment()
        +cancel()
    }
    
    class Orderer {         
        -userId: Long
        -name: String
    }

    class OrderProduct {
        -product: Product
        -price: int
        -quantity: int
        -discountAmount: Money
        +calculatePrice(): Money
    }

    class OrderState {
        <<enumeration>>
        PAYMENT_WAITING
        DELIVERY_COMPLETED
        CANCELED
    }
    
    class Product {
        -id: Long
        -name: String
        -price: Money
        -stockQuantity: int
        +removeStock(int)
        +isAvailable(int): boolean
    }
    
    class Payment {
        -id: Long
        -order: Order
        -userCoupons: List~UserCoupon~
        -amount: Money
        -paymentMethod: PaymentMethod
        -paymentState: PaymentState
        +complete()
        +cancel()
    }
    
    Payment "1" --> "1" Order
    Payment "1" --> "many" UserCoupon
    
    OrderProduct "many" --> "1" Product

    Order "1" --> "1" Orderer
    Order "1" --* "many" OrderProduct
    Order "1" --> "1" OrderState
```
