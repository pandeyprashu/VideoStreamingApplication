package com.stream.app.controller;

import com.stream.app.entities.Video;
import com.stream.app.playload.CustomMessage;
import com.stream.app.services.VideoService;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/videos")
@CrossOrigin("*")
public class VideoController {
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description) {
        Video video = new Video();

        video.setVideoId(UUID.randomUUID().toString());
        video.setTitle(title);
        video.setDescription(description);

        Video savedVideo = videoService.save(video, file);
        if (savedVideo != null) {
            // return ResponseEntity.ok(new CustomMessage("Video created
            // successfully",true));
            return ResponseEntity.status(HttpStatus.OK).body(savedVideo);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CustomMessage.builder()
                    .message("Video not uploaded").success(false).build());
        }
    }

    // Streaming Video API
    @GetMapping("/stream/{videoId}")
    public ResponseEntity<Resource> stream(@PathVariable String videoId) {

        Video video = videoService.get(videoId);
        String contentType = video.getContentType();
        String filePath = video.getFilePath();
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .body(new FileSystemResource(filePath));

    }

    // Get all videos
    @GetMapping
    public List<Video> getAll() {
        return (videoService.getAll());
    }

    // To send video in chunks
    @GetMapping("stream/range/{videoId}")
    public ResponseEntity<Resource> streamVideoRange(@PathVariable String videoId,
            @RequestHeader(value = "Range", required = false) String range) {
        System.out.println(range);

        Video video = videoService.get(videoId);
        Path path = Paths.get(video.getFilePath());

        Resource resource = new FileSystemResource(path);
        String contentType = video.getContentType();
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        // File length
        long fileLength = path.toFile().length();

        if (range == null) {
            System.out.println("Entered in the range block");
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        }
        // Calculate the range
        long rangeStart, rangeEnd;

        String[] rangeParts = range.replace("bytes=", "").split("-"); // range.replace("bytes="," ").split("-");

        rangeStart = Long.parseLong(rangeParts[0]);
        if (rangeParts.length > 1) {

            rangeEnd = Long.parseLong(rangeParts[1]);
        } else {
            rangeEnd = fileLength - 1;
        }
        if (rangeEnd > fileLength - 1) {
            rangeEnd = fileLength - 1;
        }

        InputStream inputStream;
        try {
            inputStream = Files.newInputStream(path);
            inputStream.skip(rangeStart);
            System.out.println("StartRange: "+rangeStart+" EndRange: "+rangeEnd);

        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        long contentLength = rangeEnd - rangeStart + 1;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);
        httpHeaders.add("Cache-Control", "no-cache");
        httpHeaders.add("Pragma", "no-cache");
        httpHeaders.add("Expires", "0");
        httpHeaders.add("X-Content-Type-Options", "nosniff");

        httpHeaders.setContentLength(contentLength);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).headers(httpHeaders)
                .contentType(MediaType.parseMediaType(contentType)).body(new InputStreamResource(inputStream));
    }
}
