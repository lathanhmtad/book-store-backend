package com.app.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryImageService {
    String upload(MultipartFile file, String folder) throws Exception;
    Boolean delete(String imgUrl, String folder) throws Exception;
}
