package com.example.xdesk.controller;

import com.example.xdesk.domain.Video;
import com.example.xdesk.service.VideoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *视频接口
 */
@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    @Autowired
    private VideoService videoService;


    @GetMapping("page")
    public Object pageVideo(@RequestParam(value = "page",defaultValue = "1") int page,
                            @RequestParam(value = "size",defaultValue = "10") int size){

        PageHelper.startPage(page,size);
        List<Video> list = videoService.getAll();
        PageInfo<Video> info=new PageInfo<>(list);
        return info;

    }




}
