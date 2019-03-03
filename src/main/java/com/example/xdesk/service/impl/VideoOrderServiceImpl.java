package com.example.xdesk.service.impl;


import com.example.xdesk.domain.User;
import com.example.xdesk.domain.Video;
import com.example.xdesk.domain.VideoOrder;
import com.example.xdesk.dto.VideoOrderDto;
import com.example.xdesk.mapper.UserMapper;
import com.example.xdesk.mapper.VideoMapper;
import com.example.xdesk.mapper.VideoOrderMapper;
import com.example.xdesk.service.VideoOrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class VideoOrderServiceImpl implements VideoOrderService {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private VideoOrderMapper videoOrderMapper;

    @Autowired
    private UserMapper userMapper;

    public String save(VideoOrderDto videoOrderDto){

        //获取视频
        Video video = videoMapper.findById(videoOrderDto.getVideoId());
        //获取用户信息
        User user = userMapper.findByid(videoOrderDto.getUserId());

        //生t成订单
        VideoOrder videoOrder=new VideoOrder();
        videoOrder.setTotalFee(video.getPrice());
        videoOrder.setVideoImg(video.getCoverImg());
        videoOrder.setVideoTitle(video.getTitle());
        videoOrder.setCreateTime(new Date());
        videoOrder.setVideoId(video.getId());
        videoOrder.setState(0);
        videoOrder.setUserId(user.getId());
        videoOrder.setHeadImg(user.getHeadImg());
        videoOrder.setNickname(user.getName());

        videoOrder.setDel(0);
        videoOrder.setIp(videoOrderDto.getIp());
        videoOrderMapper.insert(videoOrder);
    }
}
