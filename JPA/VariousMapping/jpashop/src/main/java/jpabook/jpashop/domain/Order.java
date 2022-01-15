package jpabook.jpashop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDERS")
public class Order {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToOne
    @JoinColumn(name = "DELIVERY_ID")
    private Delivery delivery;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> oderItems = new ArrayList<>();

    private LocalDateTime orderDate;

    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;

    public void addOrderItem(OrderItem orderItem) {
        oderItems.add(orderItem);
        orderItem.setOrder(this);
    }
}
