package com.gentlemonster.GentleMonsterBE.DTO.Responses.Product.Public;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseProductPublicResponse {
    private UUID id;
    private String name;
    private List<String> productImages;
    private BigInteger price;
}
