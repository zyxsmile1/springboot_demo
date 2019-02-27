package com.example.xdesk.service.impl;

import com.example.xdesk.domain.Video;
import com.example.xdesk.mapper.VideoMapper;
import com.example.xdesk.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService{

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public List<Video> getAll() {
        return videoMapper.getAll();
    }

    @Override
    public int update(Video video) {
        return videoMapper.update(video);
    }
}
