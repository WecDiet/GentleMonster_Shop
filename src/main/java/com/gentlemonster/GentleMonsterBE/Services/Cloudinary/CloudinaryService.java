package com.gentlemonster.GentleMonsterBE.Services.Cloudinary;

import java.io.File;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.gentlemonster.GentleMonsterBE.Utils.FileUploadUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService implements ICloudinaryService {
    
    private final Cloudinary cloudinary;

    // @Override
    // public Map<String, Object> uploadMedia(MultipartFile multipartFile, String folderName, String fileName) {
    //     // Lấy phần mở rộng file (.jpg, .png, .mp4...)
    //     String extension = fileUploadUtil.getExtension(multipartFile.getOriginalFilename());
    //     System.out.println("Extension: " + extension);
    //     String fileNameCloudinary = fileUploadUtil.generateFileName(fileName) + "." + extension;
    //     System.out.println("File name for Cloudinary: " + fileNameCloudinary);

    //     // Xác định loại file (image, video)
    //     String resourceType = fileUploadUtil.getResourceTypeFromExtension(extension);

    //     // Tạo file tạm để upload
    //     File tempFile = new File(System.getProperty("java.io.tmpdir"), fileNameCloudinary);

    //     try {
    //         FileUploadUtil.assertAllowed(multipartFile, fileUploadUtil.MEDIA_PATTERN);

    //         // Ghi file tạm
    //         multipartFile.transferTo(tempFile);

    //         // Tạo public_id trong Cloudinary (folder + tên file không có đuôi)
    //         String publicID = folderName + "/" + fileNameCloudinary.substring(0, fileNameCloudinary.lastIndexOf('.'));

    //         // Upload file lên Cloudinary
    //         Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap(
    //             "folder", folderName,
    //             "public_id", publicID,
    //             "overwrite", true,
    //             "resource_type", resourceType
    //         ));
    //         return uploadResult;
    //     } catch (Exception e) {
    //         throw new RuntimeException("Failed to upload file to Cloudinary", e);
    //     }finally {
    //         // Xóa file tạm sau khi upload
    //         if (tempFile.exists()) {
    //             tempFile.delete();
    //         }
    //     }
    @Override
    public Map<String, Object> uploadMedia(MultipartFile media, String folderName) {
        try {
            // Validate file
            FileUploadUtil.assertAllowed(media, FileUploadUtil.MEDIA_PATTERN);

            // Lấy phần mở rộng
            String originalFilename = media.getOriginalFilename();
            if (originalFilename == null || !originalFilename.contains(".")) {
                throw new RuntimeException("Invalid file name");
            }

            String extension = FileUploadUtil.getExtension(originalFilename);
            String resourceType = FileUploadUtil.getResourceTypeFromExtension(extension);
            System.out.println("Resource type: " + resourceType);
            String fileNameCloudinary = FileUploadUtil.generateFileName(originalFilename);
            System.out.println("Extension: " + extension);
            System.out.println("File name for Cloudinary: " + fileNameCloudinary);
            // String fileNameCloudinary = fileName + "." + extension;
            // System.out.println("File name for Cloudinary: " + fileNameCloudinary);
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
            throw new RuntimeException("Failed to upload file to Cloudinary", e);
        } finally {
            // Xoá file tạm
            File tempFile = new File(System.getProperty("java.io.tmpdir"), media.getOriginalFilename());
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
       
    }


    
}
