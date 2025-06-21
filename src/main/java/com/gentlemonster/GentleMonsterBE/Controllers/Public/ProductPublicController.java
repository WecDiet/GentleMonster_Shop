package com.gentlemonster.GentleMonsterBE.Controllers.Public;

import com.gentlemonster.GentleMonsterBE.Contants.Enpoint;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.Services.Product.ProductService;
import com.gentlemonster.GentleMonsterBE.Services.ProductType.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class ProductPublicController {

    @Autowired
    private ProductService productService;

    // @Autowired
    // private ProductTypeService productTypeService;

    @GetMapping(Enpoint.Product_Type.PUBLIC_PRODUCT_TYPE)
    public ResponseEntity<APIResponse<?>> getAllProductPublic(@PathVariable String categorySlug, @PathVariable String sliderSlug) {
        if (categorySlug == null) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, List.of("Category is required !!!!")));
        }
        if (sliderSlug == null) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, List.of("Slider is required !!!!")));
        }
        return ResponseEntity.ok(productService.getAllProductPublic(categorySlug, sliderSlug));
    }

    @GetMapping(Enpoint.Product.ID_PRODUCT)
    public ResponseEntity<?> getProductById(@PathVariable String productSlug, @PathVariable String productCode) {
        if (productSlug == null) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, List.of("Slug is required !!!!")));
        }
        if (productCode == null) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, List.of("Code is required !!!!")));
        }

        return ResponseEntity.ok(productService.getProductDetailPublic(productSlug, productCode));
    }
}
