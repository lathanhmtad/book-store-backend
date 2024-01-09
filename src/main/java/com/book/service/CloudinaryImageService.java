package com.book.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryImageService {
    String upload(MultipartFile file, String folder) throws Exception;
    Boolean delete(String imgUrl, String folder) throws Exception;
}
