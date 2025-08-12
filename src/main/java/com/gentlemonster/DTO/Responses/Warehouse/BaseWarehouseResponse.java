package com.gentlemonster.DTO.Responses.Warehouse;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseWarehouseResponse {
    private UUID id;
    private String warehouseName;
    private String warehouseLocation;
    private int totalCapacity;
}
