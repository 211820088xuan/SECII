package org.fffd.l23o6.util.strategy.payment;

public abstract class PaymentStrategy {

    // TO DO: implement this by adding necessary methods and implement specified strategy
    public abstract void pay(Long userId, Long orderId);
    public abstract void refund(Long userId, Long orderId);
}
