package org.fffd.l23o6.util.strategy.payment;

public abstract class PaymentStrategy {

    // TO DO: implement this by adding necessary methods and implement specified strategy
    public abstract boolean pay(Long userId, Long orderId);
    public abstract boolean refund(Long userId, Long orderId);
}
