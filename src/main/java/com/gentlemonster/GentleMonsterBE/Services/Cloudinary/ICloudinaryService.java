package com.gentlemonster.GentleMonsterBE.Services.Cloudinary;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface ICloudinaryService {
    Map<String, Object> uploadMedia(MultipartFile media, String folderName);
    void deleteMedia(String publicId);
}
