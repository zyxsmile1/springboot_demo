package com.example.xdesk.service;


import com.example.xdesk.domain.VideoOrder;
import com.example.xdesk.dto.VideoOrderDto;

public interface VideoOrderService {

     String save(VideoOrderDto videoOrderDto) throws Exception;

     VideoOrder findByOutTradeNo(String outTradeNo) ;

     int updateVideoOrderByOutTradeNo(VideoOrder videoOrder);



}


