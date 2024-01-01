package com.book.service.impl;

import com.book.service.CloudinaryImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
@Service
public class CloudinaryImageServiceImpl implements CloudinaryImageService {
    @Override
    public String upload(MultipartFile file) {
        return null;
    }
}
