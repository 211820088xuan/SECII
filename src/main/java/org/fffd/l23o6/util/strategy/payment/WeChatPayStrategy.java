package org.fffd.l23o6.util.strategy.payment;

public class WeChatPayStrategy extends PaymentStrategy{
    @Override
    public boolean pay(Long orderId, double price) {
        return false;
    }

    @Override
    public boolean refund(Long orderId, double price) {
        return false;
    }
}
