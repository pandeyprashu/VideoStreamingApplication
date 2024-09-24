package com.stream.app.services.impl;

import com.stream.app.entities.Video;
import com.stream.app.repository.VideoRepository;
import com.stream.app.services.VideoService;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoRepository videoRepository;  // Inject VideoRepository to access DB operations

    @Value("${files.video}")
    String DIR;  // Directory path to save video files, fetched from application properties

    @PostConstruct
    public void init() {
        File file = new File(DIR);
        // Check if the folder exists; create it if not
        if (!file.exists()) {
            file.mkdir();  // Create folder
            System.out.println("Folder created");
        } else {
            System.out.println("Folder exists");
        }
    }

    @Override
    public Video save(Video video, MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();  // Extract file name
            String contentType = file.getContentType();  // Get file type
            InputStream inputStream = file.getInputStream();  // Get input stream of file

            // Clean the file name to avoid any invalid path characters
            String cleanFileName = StringUtils.cleanPath(filename);
            String cleanFolder = StringUtils.cleanPath(DIR);

            // Resolve the path for saving the file
            Path path = Paths.get(cleanFolder, cleanFileName);
            System.out.println(path);

            // Copy the file to the target location, replacing any existing file
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);

            // Set the file type and path in the video entity
            video.setContentType(contentType);
            video.setFilePath(path.toString());

        } catch (IOException e) {
            e.printStackTrace();  // Log any errors during file operations
        }

        // Save the video details in the repository (database)
        return this.videoRepository.save(video);
    }

    @Override
    public Video get(String videoId) {
        return null;  // Method to retrieve a video by ID, not yet implemented
    }

    @Override
    public Video getByTitle(String title) {
        return null;  // Method to retrieve a video by title, not yet implemented
    }

    @Override
    public List<Video> getAll() {
        return List.of();  // Method to retrieve all videos, returns an empty list for now
    }
}
