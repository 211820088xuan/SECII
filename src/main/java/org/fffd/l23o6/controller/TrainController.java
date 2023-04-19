package org.fffd.l23o6.controller;

import java.util.Date;

import org.fffd.l23o6.pojo.vo.PagedResult;
import org.fffd.l23o6.pojo.vo.Response;
import org.fffd.l23o6.pojo.vo.train.ListTrainRequest;
import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/")
public class TrainController {
    @GetMapping("train")
    public Response<PagedResult<TrainVO>> listTrains(@Valid @RequestParam ListTrainRequest request) {
        return null;
    }

    @GetMapping("train/{trainId}")
    public Response<TrainVO> getTrain(@PathVariable Integer trainId) {
        return Response.success(new TrainVO(1L,"1","1","1",new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()),false));
    }
}
