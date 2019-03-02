package com.example.xdesk.service;

import com.example.xdesk.domain.Video;

import java.util.List;

public interface VideoService {

    List<Video> getAll();

    int update(Video video);

    Video findById(int videoId);
}
