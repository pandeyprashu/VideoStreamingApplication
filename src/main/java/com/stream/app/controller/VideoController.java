package com.stream.app.controller;


import com.stream.app.entities.Video;
import com.stream.app.playload.CustomMessage;
import com.stream.app.services.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/videos")
public class VideoController {
    private VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }
    @PostMapping
    public ResponseEntity<CustomMessage> create(@RequestParam("file")MultipartFile file,
                                                @RequestParam("title")String title,
                                                @RequestParam("description")String description){
        Video video=new Video();

        video.setVideoId(UUID.randomUUID().toString());
        video.setTitle(title);
        video.setDescription(description);

        videoService.save(video,file);
        return null;
    }
}
