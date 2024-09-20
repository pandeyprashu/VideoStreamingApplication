package com.stream.app.services.impl;


import com.stream.app.entities.Video;
import com.stream.app.repository.VideoRepository;
import com.stream.app.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Value("${files.video}")
    String DIR;

    @Override
    public Video save(Video video, MultipartFile file) {
        try{
            String filename=file.getOriginalFilename();
            String contentType=file.getContentType();
            InputStream inputStream=file.getInputStream();

            //Use to clean the path of the file
            String cleanFileName = StringUtils.cleanPath(filename);
            String cleanFolder=StringUtils.cleanPath(DIR);

           Path path= Paths.get(cleanFolder,cleanFileName);

            //folder path : Create
        }catch (IOException e){

        }

        return this.videoRepository.save(video);
        
    }

    @Override
    public Video get(String videoId) {
        return null;
    }

    @Override
    public Video getByTitle(String title) {
        return null;
    }

    @Override
    public List<Video> getAll() {
        return List.of();
    }
}
