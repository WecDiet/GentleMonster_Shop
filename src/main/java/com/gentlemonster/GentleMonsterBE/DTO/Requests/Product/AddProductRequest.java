package com.gentlemonster.GentleMonsterBE.DTO.Requests.Product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddProductRequest {
    private String productName;
    private String description;
    private String price;
    private String thumbnail;
    private boolean status;
    private String frame;
    private String lens;
    private String shape;
    private String material;
    private double lens_Width;
    private double lens_Height;
    private double bridge;
    private String country;
    private String manufacturer;
    private String productType;
    private boolean warehouseActive;
}
