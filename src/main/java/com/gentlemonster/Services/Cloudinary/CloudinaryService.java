package com.gentlemonster.Services.Cloudinary;

import java.io.File;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.Utils.FileUploadUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService implements ICloudinaryService {
    
    private final Cloudinary cloudinary;
    @Override
    public Map<String, Object> uploadMedia(MultipartFile media, String folderName) {
        try {
            // Validate file
            FileUploadUtils.assertAllowed(media, FileUploadUtils.MEDIA_PATTERN);

            // Lấy phần mở rộng
            String originalFilename = media.getOriginalFilename();
            if (originalFilename == null || !originalFilename.contains(".")) {
                throw new RuntimeException("Invalid file name");
            }

            String extension = FileUploadUtils.getExtension(originalFilename);
            String resourceType = FileUploadUtils.getResourceTypeFromExtension(extension);
            String fileNameCloudinary = FileUploadUtils.generateFileName(originalFilename);
            // File tạm
            File tempFile = new File(System.getProperty("java.io.tmpdir"), fileNameCloudinary);
            media.transferTo(tempFile);

            // Tạo public_id
            String publicID = "/" + fileNameCloudinary.substring(0, fileNameCloudinary.lastIndexOf('.'));

            // Upload
            Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap(
                "folder", folderName,
                "public_id", publicID,
                "overwrite", true,
                "resource_type", resourceType
            ));

            return uploadResult;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to Cloudinary", e.fillInStackTrace());
        } finally {
            // Xoá file tạm
            File tempFile = new File(System.getProperty("java.io.tmpdir"), media.getOriginalFilename());
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
       
    }

    public void deleteMedia(String publicId){
       try {
            if (publicId == null || publicId.isEmpty()) {
                throw new IllegalArgumentException("Public ID cannot be null or empty");
            }

            // Assume resource type is image
            String resourceType = "image";

            // Delete from Cloudinary
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap(
                    "resource_type", resourceType,
                    "invalidate", true
            ));
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from Cloudinary", e);
        }
        
    }


    
}
