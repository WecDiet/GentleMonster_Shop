package com.gentlemonster.DTO.Responses.Product.Public;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import com.gentlemonster.DTO.Responses.Product.ImageMediaDetail;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseProductPublicResponse {
    private String code;
    private String slug;
    private String name;
    private ImageMediaDetail productDetail;
    private BigInteger price;
}
