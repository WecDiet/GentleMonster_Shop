package com.gentlemonster.DTO.Responses.Warehouse;

import java.util.List;

import com.gentlemonster.DTO.Responses.Cloudinary.ImageResponse;
import com.gentlemonster.DTO.Responses.User.BaseUserWarehouse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseResponse {
    private String warehouseName; // Tên kho
    private String warehouseLocation; // Vị trí trong kho
    private String street; // Đường
    private String ward; // Phường
    private String district; // Quận
    private String city; // Thành phố
    private String country; // Quốc gia
    private int totalCapacity; // Dung lượng tối đa của kho
    private BaseUserWarehouse user;
    private List<ImageResponse> medias;
}
