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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Map<String,Object> data=new HashMap<>();
        PageHelper.startPage(page,size);
        List<Video> list = videoService.getAll();
        PageInfo<Video> info=new PageInfo<>(list);
        data.put("total_size",info.getTotal());
        data.put("total_page",info.getPages());
        data.put("current_page",page);
        data.put("data",info.getList());
        return info;

    }


    /**
     * 通过Id查找视频
     * @param videoId
     * @return
     */
    @GetMapping("find_by_id")
    public Object finVideoById(@RequestParam(value = "video_id",required = true) int videoId){
        return videoService.findById(videoId);
    }

}
