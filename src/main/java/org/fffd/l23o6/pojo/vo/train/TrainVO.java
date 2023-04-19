package org.fffd.l23o6.pojo.vo.train;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrainVO {
    private Long trainId;
    private String trainName;
    private String startStation;
    private String endStation;
    private Date departureTime;
    private Date arrivalTime;
    private Boolean isLate;
}
