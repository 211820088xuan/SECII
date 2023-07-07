package org.fffd.l23o6.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.mapper.TrainMapper;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.enum_.TrainType;
import org.fffd.l23o6.pojo.vo.train.AdminTrainVO;
import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.fffd.l23o6.pojo.vo.train.TicketInfo;
import org.fffd.l23o6.pojo.vo.train.TrainDetailVO;
import org.fffd.l23o6.service.TrainService;
import org.fffd.l23o6.util.strategy.train.GSeriesSeatStrategy;
import org.fffd.l23o6.util.strategy.train.KSeriesSeatStrategy;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import io.github.lyc8503.spring.starter.incantation.exception.CommonErrorType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainServiceImpl implements TrainService {
    private final TrainDao trainDao;
    private final RouteDao routeDao;

    @Override
    public TrainDetailVO getTrain(Long trainId) {
        TrainEntity train = trainDao.findById(trainId).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();
        return TrainDetailVO.builder()
                .id(trainId)
                .date(train.getDate())
                .name(train.getName())
                .stationIds(route.getStationIds())
                .arrivalTimes(train.getArrivalTimes())
                .departureTimes(train.getDepartureTimes())
                .extraInfos(train.getExtraInfos()).build();
    }

    @Override
    public List<TrainVO> listTrains(Long startStationId, Long endStationId, String date) {
        // TO DO
        // First, get all routes contains [startCity, endCity]
        // Then, Get all trains on that day with the wanted routes
        List<RouteEntity> routeEntities = routeDao.findAll();
        List<TrainEntity> trainEntities = trainDao.findAll();
        List<Long> desRoutes = new ArrayList<>();
        List<Long> desTrains = new ArrayList<>();
        List<TrainVO> trainVOList = trainDao.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(TrainMapper.INSTANCE::toTrainVO).collect(Collectors.toList());
        List<TrainVO> desTrainVOs = new ArrayList<>();

        int flag;
        for (RouteEntity temp : routeEntities) {
            for (int j = 0; j < temp.getStationIds().size(); j++) {
                if (startStationId.equals(temp.getStationIds().get(j))) {
                    for (int k = j + 1; k < temp.getStationIds().size();k++){
                        if (endStationId.equals(temp.getStationIds().get(k))) {
                            desRoutes.add(temp.getId());
                            break;
                        }
                    }
                }
            }
        }

        for (TrainEntity temp : trainEntities) {
            flag = 0;
            for (Long desRoute : desRoutes) {
                if (temp.getRouteId().equals(desRoute)) {
                    flag = 1;
                    break;
                }
            }
            if (temp.getDate().equals(date) && flag == 1) {
                desTrains.add(temp.getId());
            }
        }

        for (TrainVO temp : trainVOList) {
            for (Long desTrain : desTrains) {
                if (temp.getId().equals(desTrain)) {
                    TrainEntity train = trainDao.findById(desTrain).get();
                    RouteEntity route =  routeDao.findById(train.getRouteId()).get();
                    temp.setArrivalTime(train.getArrivalTimes().get(route.getStationIds().indexOf(endStationId)));
                    temp.setDepartureTime(train.getDepartureTimes().get(route.getStationIds().indexOf(startStationId)));
                    temp.setStartStationId(startStationId);
                    temp.setEndStationId(endStationId);

                    temp.setTicketInfo(listTickets(train.getTrainType()));
                    desTrainVOs.add(temp);
                    break;
                }
            }
        }
        return desTrainVOs;
    }

    @Override
    public List<AdminTrainVO> listTrainsAdmin() {
        return trainDao.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(TrainMapper.INSTANCE::toAdminTrainVO).collect(Collectors.toList());
    }

    @Override
    public void addTrain(String name, Long routeId, TrainType type, String date, List<Date> arrivalTimes,
            List<Date> departureTimes) {
        TrainEntity entity = TrainEntity.builder().name(name)
                .routeId(routeId)
                .trainType(type)
                .date(date)
                .arrivalTimes(arrivalTimes)
                .departureTimes(departureTimes)
                .build();
        RouteEntity route = routeDao.findById(routeId).get();
        long fromStationId = route.getStationIds().get(0);
        long toStationId = route.getStationIds().get(route.getStationIds().size() - 1);
        int startStationIndex = route.getStationIds().indexOf(fromStationId);
        int endStationIndex = route.getStationIds().indexOf(toStationId);
        if (route.getStationIds().size() != entity.getArrivalTimes().size()
                || route.getStationIds().size() != entity.getDepartureTimes().size()) {
            throw new BizException(CommonErrorType.ILLEGAL_ARGUMENTS, "列表长度错误");
        }
        entity.setExtraInfos(new ArrayList<String>(Collections.nCopies(route.getStationIds().size(), "预计正点")));
        switch (entity.getTrainType()) {
            case HIGH_SPEED:
                entity.setSeats(GSeriesSeatStrategy.INSTANCE.initSeatMap(route.getStationIds().size()));
//                entity.setSeatPrices(GSeriesSeatStrategy.INSTANCE.setPrice(startStationIndex,endStationIndex));
                break;
            case NORMAL_SPEED:
                entity.setSeats(KSeriesSeatStrategy.INSTANCE.initSeatMap(route.getStationIds().size()));
//                entity.setSeatPrices(KSeriesSeatStrategy.INSTANCE.setPrice(startStationIndex,endStationIndex));
                break;
        }
        trainDao.save(entity);
    }

    @Override
    public void changeTrain(Long id, String name, Long routeId, TrainType type, String date, List<Date> arrivalTimes,
                            List<Date> departureTimes) {
        // TO DO: edit train info, please refer to `addTrain` above
        deleteTrain(id);
        addTrain(name, id, type, date, arrivalTimes, departureTimes);
    }

    @Override
    public void deleteTrain(Long id) {
        trainDao.deleteById(id);
    }

    public List<TicketInfo> listTickets(TrainType trainType){
        List<TicketInfo> ticketInfos = new ArrayList<>();
        switch (trainType){
            case NORMAL_SPEED:
                TicketInfo ticketInfo = new TicketInfo();
                ticketInfo.setType("软卧");
                ticketInfo.setCount(8);
                ticketInfo.setPrice(15);
                ticketInfos.add(ticketInfo);

                TicketInfo ticketInfo1 = new TicketInfo();
                ticketInfo1.setType("硬卧");
                ticketInfo1.setCount(12);
                ticketInfo1.setPrice(10);
                ticketInfos.add(ticketInfo1);

                TicketInfo ticketInfo2 = new TicketInfo();
                ticketInfo2.setType("软座");
                ticketInfo2.setCount(16);
                ticketInfo2.setPrice(8);
                ticketInfos.add(ticketInfo2);

                TicketInfo ticketInfo3 = new TicketInfo();
                ticketInfo3.setType("硬座");
                ticketInfo3.setCount(20);
                ticketInfo3.setPrice(6);
                ticketInfos.add(ticketInfo3);

                TicketInfo noSeat = new TicketInfo();
                noSeat.setType("无座");
                noSeat.setCount(25);
                noSeat.setPrice(5);
                ticketInfos.add(noSeat);
                break;
            case HIGH_SPEED:
                TicketInfo businessSeat = new TicketInfo();
                businessSeat.setType("商务座");
                businessSeat.setCount(3);
                businessSeat.setPrice(25);
                ticketInfos.add(businessSeat);

                TicketInfo firstClassSeat = new TicketInfo();
                firstClassSeat.setType("一等座");
                firstClassSeat.setCount(12);
                firstClassSeat.setPrice(20);
                ticketInfos.add(firstClassSeat);

                TicketInfo secondClassSeat = new TicketInfo();
                secondClassSeat.setType("二等座");
                secondClassSeat.setCount(15);
                secondClassSeat.setPrice(15);
                ticketInfos.add(secondClassSeat);

                TicketInfo noSeat1 = new TicketInfo();
                noSeat1.setType("无座");
                noSeat1.setCount(20);
                noSeat1.setPrice(13);
                ticketInfos.add(noSeat1);
                break;
        }
        return ticketInfos;
    }
}
