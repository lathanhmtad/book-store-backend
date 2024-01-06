package com.book.service.impl;

import com.book.service.CloudinaryImageService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
@Service
public class CloudinaryImageServiceImpl implements CloudinaryImageService {
    @Value("${com.book.cloudinary.imageFolder}")
    private String folderName;

    private Cloudinary cloudinary;
    public CloudinaryImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String upload(MultipartFile file, String folder) throws Exception {
        try {
            Map params = ObjectUtils.asMap(
                    "public_id", file.getOriginalFilename(),
                    "use_filename", "true",
                    "unique_filename", "false",
                    "folder", folderName + "/" + folder
            );
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
            return uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
