package org.fffd.l23o6.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.fffd.l23o6.dao.OrderDao;
import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.dao.UserDao;
import org.fffd.l23o6.pojo.entity.UserEntity;
import org.fffd.l23o6.pojo.enum_.OrderStatus;
import org.fffd.l23o6.exception.BizError;
import org.fffd.l23o6.pojo.entity.OrderEntity;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.vo.order.OrderVO;
import org.fffd.l23o6.service.OrderService;
import org.fffd.l23o6.util.strategy.payment.AlipayStrategy;
import org.fffd.l23o6.util.strategy.payment.PaymentStrategy;
import org.fffd.l23o6.util.strategy.train.GSeriesSeatStrategy;
import org.fffd.l23o6.util.strategy.train.KSeriesSeatStrategy;
import org.springframework.stereotype.Service;

import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final TrainDao trainDao;
    private final RouteDao routeDao;
    //表驱动实现积分打折
    int []scoreLevel = {1000,3000,10000,50000};
    int []scoreMoney = {1,3,14,100};
    double []scoreDiscount ={0.001,0.0015,0.002,0.0025,0.003};


    public Long createOrder(String username, Long trainId, Long fromStationId, Long toStationId, String seatType
            , String payType,boolean useScore,Long existingCredit,Long availableCredit) {
        Long userId = userDao.findByUsername(username).getId();
        TrainEntity train = trainDao.findById(trainId).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();
        int startStationIndex = route.getStationIds().indexOf(fromStationId);
        int endStationIndex = route.getStationIds().indexOf(toStationId);
        String seatKind = null;
        /////
        double[][] priceTable = train.getSeatPrices();
        int seatTypeIndex = 0;
        ////////
        switch (train.getTrainType()) {
            case HIGH_SPEED:
                seatKind = GSeriesSeatStrategy.INSTANCE.allocSeat(startStationIndex, endStationIndex,
                        GSeriesSeatStrategy.GSeriesSeatType.fromString(seatKind), train.getSeats());
                seatTypeIndex = Objects.requireNonNull(GSeriesSeatStrategy.GSeriesSeatType.fromString(seatKind)).ordinal();
                // 0 商务座 1 一等座 2 二等座 3 无座
                break;
            case NORMAL_SPEED:
                seatKind = KSeriesSeatStrategy.INSTANCE.allocSeat(startStationIndex, endStationIndex,
                        KSeriesSeatStrategy.KSeriesSeatType.fromString(seatKind), train.getSeats());
                seatTypeIndex = Objects.requireNonNull(KSeriesSeatStrategy.KSeriesSeatType.fromString(seatKind)).ordinal();
                // 0 软卧 1 硬卧 2 软座 3 硬座 4 无座
                break;
        }
        if (seatKind == null) {
            throw new BizException(BizError.OUT_OF_SEAT);
        }

        ///////
        double totalPrice = 0.0;
        for (int i = startStationIndex; i < endStationIndex; i++) {
            totalPrice += priceTable[i][seatTypeIndex];
        }
        //////积分折扣
        if (useScore){
            long score = userDao.findByUsername(username).getCredit();
            int index = 0;
            for (int j : scoreLevel) {
                if (score > j) {
                    index++;
                } else {
                    break;
                }
            }
            double discountMoney = 0;
            if (index == 0){
                discountMoney = score * scoreDiscount[0];
            }else {
                for (int i = 0; i < index ;i++){
                    discountMoney += scoreMoney[i];
                }
                discountMoney += (score - scoreLevel[index - 1]) * scoreDiscount[index];
            }
            totalPrice -= discountMoney;
        }

        ///////
        OrderEntity order = OrderEntity.builder()
                .trainId(trainId)
                .userId(userId)
                .seat(seatKind)
                .status(OrderStatus.PENDING_PAYMENT)
                .arrivalStationId(toStationId)
                .departureStationId(fromStationId)
                .price(totalPrice)
                .paymentType(payType)
                .existingCredit(existingCredit)
                .availableCredit(availableCredit)
                .build();
        train.setUpdatedAt(null);// force it to update
        trainDao.save(train);
        orderDao.save(order);
        return order.getId();
    }

    public List<OrderVO> listOrders(String username) {
        Long userId = userDao.findByUsername(username).getId();
        List<OrderEntity> orders = orderDao.findByUserId(userId);
        orders.sort((o1,o2)-> o2.getId().compareTo(o1.getId()));
        return orders.stream().map(order -> {
            TrainEntity train = trainDao.findById(order.getTrainId()).get();
            RouteEntity route = routeDao.findById(train.getRouteId()).get();
            int startIndex = route.getStationIds().indexOf(order.getDepartureStationId());
            int endIndex = route.getStationIds().indexOf(order.getArrivalStationId());
            return OrderVO.builder().id(order.getId())
                    .trainId(order.getTrainId())
                    .seat(order.getSeat()).status(order.getStatus().getText())
                    .createdAt(order.getCreatedAt())
                    .startStationId(order.getDepartureStationId())
                    .endStationId(order.getArrivalStationId())
                    .departureTime(train.getDepartureTimes().get(startIndex))
                    .arrivalTime(train.getArrivalTimes().get(endIndex))
                    .paymentType(order.getPaymentType())
                    .price(order.getPrice())
                    .availableCredit(order.getAvailableCredit())
                    .build();
        }).collect(Collectors.toList());
    }

    public OrderVO getOrder(Long id) {
        OrderEntity order = orderDao.findById(id).get();
        TrainEntity train = trainDao.findById(order.getTrainId()).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();
        int startIndex = route.getStationIds().indexOf(order.getDepartureStationId());
        int endIndex = route.getStationIds().indexOf(order.getArrivalStationId());
        return OrderVO.builder().id(order.getId())
                .trainId(order.getTrainId())
                .seat(order.getSeat()).status(order.getStatus().getText())
                .createdAt(order.getCreatedAt())
                .startStationId(order.getDepartureStationId())
                .endStationId(order.getArrivalStationId())
                .departureTime(train.getDepartureTimes().get(startIndex))
                .arrivalTime(train.getArrivalTimes().get(endIndex))
                .paymentType(order.getPaymentType())
                .price(order.getPrice())
                .availableCredit(order.getAvailableCredit())
                .build();
    }

    public void cancelOrder(Long id) {
        OrderEntity order = orderDao.findById(id).get();

        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new BizException(BizError.ILLEAGAL_ORDER_STATUS);
        }

        if (order.getStatus() == OrderStatus.PAID) {
            //refund user's money
            PaymentStrategy paymentStrategy;
            if (order.getPaymentType().equals("Alipay")){//支付宝支付
                paymentStrategy = new AlipayStrategy();
            }else {
                paymentStrategy = null;
            }
            paymentStrategy.refund(id,order.getPrice());

            // refund credits
            Long userId = order.getUserId();
            Long credit = order.getAvailableCredit();
            UserEntity user = userDao.findById(userId).get();
            user.setCredit(user.getCredit() - credit);

            // set the seat available again
            String seat = order.getSeat();
            int seatNum;
            TrainEntity train = trainDao.findById(order.getTrainId()).get();
            switch (train.getTrainType()){
                case HIGH_SPEED :
                    seatNum = GSeriesSeatStrategy.INSTANCE.getSeatNum(seat);
                    break;
                case NORMAL_SPEED:
                    seatNum = KSeriesSeatStrategy.INSTANCE.getSeatNum(seat);
                    break;
                default:
                    seatNum = -1;
            }
            RouteEntity route = routeDao.findById(train.getRouteId()).get();
            int startStationIndex = route.getStationIds().indexOf(order.getDepartureStationId());
            int endStationIndex = route.getStationIds().indexOf(order.getDepartureStationId());
            boolean[][]seatMap = train.getSeats();
            for (int i = startStationIndex; i < endStationIndex; i++) {
                seatMap[i][seatNum] = false;//false 是空座
            }
        } else {
             //order.getStatus() == OrderStatus.PENDING_PAYMENT
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderDao.save(order);
    }

    public void payOrder(Long id) {
        OrderEntity order = orderDao.findById(id).get();

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BizException(BizError.ILLEAGAL_ORDER_STATUS);
        }
        // use payment strategy to pay!
        PaymentStrategy paymentStrategy;
        if (order.getPaymentType().equals("Alipay")){//支付宝支付
            paymentStrategy = new AlipayStrategy();
        }else {
            paymentStrategy = null;
        }
        paymentStrategy.pay(id,order.getPrice());

        double price = order.getPrice();
        long credits = (long) (price * 50);
        long userId = order.getUserId();
        UserEntity user = userDao.findById(userId).get();
        user.setCredit(user.getCredit() + credits);

        // update user's credits, so that user can get discount next time

        order.setStatus(OrderStatus.COMPLETED);
        orderDao.save(order);
    }

}
