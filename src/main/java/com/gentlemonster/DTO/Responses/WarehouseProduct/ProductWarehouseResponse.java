package com.gentlemonster.DTO.Responses.WarehouseProduct;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gentlemonster.DTO.Responses.Cloudinary.ImageResponse;
import com.gentlemonster.DTO.Responses.Product.BaseProductResponse;
import com.gentlemonster.DTO.Responses.Warehouse.BaseWarehouseResponse;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductWarehouseResponse {
//    private BaseProductResponse products;
    private int quantity;

    private String productName; // Thêm trường này
    private String manufacturer;

//    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime importDate;

    private boolean publicProduct;
    private BigInteger importPrice; // Giá nhập hàng

//    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime modifiedDate;

    private String productType; // Loại sản phẩm

    private BaseWarehouseResponse warehouse;

    private List<ImageResponse> images; // Danh sách hình ảnh của sản phẩm
}
