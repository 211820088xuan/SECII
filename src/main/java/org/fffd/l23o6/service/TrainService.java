package org.fffd.l23o6.service;

import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.springframework.data.domain.Page;

public interface TrainService {
    public TrainVO getTrain(Long trainId);
    public Page<TrainVO> listTrains(Integer page, Integer pageSize, String startCity, String endCity, String date);
}
