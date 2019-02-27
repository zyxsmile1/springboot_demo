package com.example.xdesk.controller;

import com.example.xdesk.config.WeChatConfig;
import com.example.xdesk.domain.Video;
import com.example.xdesk.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private WeChatConfig weChatConfig;
    @Autowired
    private VideoService videoService;



    @GetMapping("/")
    public String test(){
        System.out.println(weChatConfig.getAppId());
        System.out.println(weChatConfig.getAppsecret());
        return "xdesk hello word777777777777";
    }

    @GetMapping("/getAll")
    public Object getAll(){
        List<Video> list = videoService.getAll();

        return list;
    }


}
