package com.app.service.impl;

import com.app.service.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    @Value("${com.app.cloudinary.rootFolder}")
    private String rootFolder;
    private Cloudinary cloudinary;
    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
    @Override
    public String upload(MultipartFile file, String folder) throws Exception {
        try {
            Map params = ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", true,
                    "folder", rootFolder + "/" + folder
            );
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
            return uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Boolean delete(String imgUrl, String folder) throws Exception {
        try {
            int rootFolderIndex = imgUrl.indexOf(rootFolder);
            int lastDotIndex = imgUrl.lastIndexOf(".");
            String publicId = imgUrl.substring(rootFolderIndex, lastDotIndex);
            Map options = Map.of("invalidate", true);
            Map result = cloudinary.uploader().destroy(publicId, options);
            return result.get("result").toString().equals("ok");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
