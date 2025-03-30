package com.gentlemonster.GentleMonsterBE.DTO.Responses.WarehouseProduct;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Product.BaseProductResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Warehouse.BaseWarehouseResponse;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;


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
}
