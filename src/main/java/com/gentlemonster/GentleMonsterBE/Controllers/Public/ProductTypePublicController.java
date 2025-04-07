package com.gentlemonster.GentleMonsterBE.Controllers.Public;

import com.gentlemonster.GentleMonsterBE.Contants.Enpoint;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.ProductType.Public.ProductPublicResponse;
import com.gentlemonster.GentleMonsterBE.Services.ProductType.ProductTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Enpoint.API_PREFIX_SHOP)
public class ProductTypePublicController {
    @Autowired
    private ProductTypeService productTypeService;

//    @GetMapping(Enpoint.Product_Type.PUBLIC_PRODUCT_TYPE)
//    public ResponseEntity<APIResponse<?>> getAllProductTypePublic(@PathVariable String categorySlug, @PathVariable String sliderSlug) {
//        if (categorySlug == null) {
//            return ResponseEntity.badRequest().body(new APIResponse<>(null, List.of("Category is required !!!!")));
//        }
//        if (sliderSlug == null) {
//            return ResponseEntity.badRequest().body(new APIResponse<>(null, List.of("Slider is required !!!!")));
//        }
//        return ResponseEntity.ok(productTypeService.getAllProductTypePublic(categorySlug, sliderSlug));
//    }
}
