package com.gentlemonster.Controllers.Admin;

import java.util.List;

import org.aspectj.weaver.ast.Not;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gentlemonster.Contants.Enpoint;
import com.gentlemonster.DTO.Requests.Voucher.AddVoucherRequest;
import com.gentlemonster.DTO.Requests.Voucher.EditVoucherRequest;
import com.gentlemonster.DTO.Requests.Voucher.VoucherRequest;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.Services.Voucher.VoucherService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(Enpoint.Voucher.BASE)
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    private static final Logger logger = LoggerFactory.getLogger(VoucherController.class);

    @GetMapping
    public ResponseEntity<PagingResponse<?>> getAllVouchers(VoucherRequest voucherRequest) {
        return ResponseEntity.ok(voucherService.getAllVouchers(voucherRequest));
    }

    @GetMapping(Enpoint.Voucher.ID)
    public ResponseEntity<?> getOneVoucher(@PathVariable String voucherID) throws NotFoundException {
        return ResponseEntity.ok(voucherService.getOneVoucher(voucherID));
    }

    @PostMapping(Enpoint.Voucher.NEW)
    public ResponseEntity<?> createVoucher(@Valid @RequestBody AddVoucherRequest addVoucherRequest, BindingResult result) throws NotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            logger.error("Error Search user: " + errorMessages);
            return ResponseEntity.badRequest().body(new PagingResponse<>(null,  errorMessages, 0, (long) 0));
        }
        return ResponseEntity.ok(voucherService.createVoucher(addVoucherRequest));
    }

    @PutMapping(Enpoint.Voucher.EDIT)
    public ResponseEntity<?> updateVoucher(@Valid @PathVariable String voucherID, @RequestBody EditVoucherRequest editVoucherRequest, BindingResult result) throws NotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // Creating an APIResponse with error messages
            logger.error("Error Search user: " + errorMessages);
            return ResponseEntity.badRequest().body(new PagingResponse<>(null,  errorMessages, 0, (long) 0));
        }
        return ResponseEntity.ok(voucherService.updateVoucher(voucherID, editVoucherRequest));
    }

    @DeleteMapping(Enpoint.Voucher.DELETE)
    public ResponseEntity<?> deleteVoucher(@PathVariable String voucherID) throws NotFoundException {
        return ResponseEntity.ok(voucherService.deleteVoucher(voucherID));
    }

}