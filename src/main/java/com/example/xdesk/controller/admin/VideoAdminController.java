package com.example.xdesk.controller.admin;

import com.example.xdesk.domain.Video;
import com.example.xdesk.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api/v1/video")
public class VideoAdminController {

    @Autowired
    private VideoService videoService;


    @PutMapping("update_by_id")
    public Object update(@RequestBody Video video){

        return videoService.update(video);

    }
}
