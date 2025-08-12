package com.gentlemonster.DTO.Requests.Warehouse;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddWarehouseRequest {
    @NotEmpty(message = "Warehouse Name is required")
    private String warehouseName; // Tên kho hàng

    @NotEmpty(message = "Warehouse Location is required")
    private String warehouseLocation; // Vị trí trong kho

    @NotEmpty(message = "Street is required")
    private String street; // Đường

    @NotEmpty(message = "Ward is required")
    private String ward; // Phường

    @NotEmpty(message = "District is required")
    private String district; // Quận

    @NotEmpty(message = "City is required")
    private String city; // Thành phố

    @NotEmpty(message = "Country is required")
    private String country; // Quốc gia

    private int totalCapacity; // Dung lượng tối đa của kho

    private String fullName;

    private String code;
}
