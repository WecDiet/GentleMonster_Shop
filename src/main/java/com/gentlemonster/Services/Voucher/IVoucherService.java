package com.gentlemonster.Services.Voucher;

import java.util.List;
import java.util.UUID;

import com.gentlemonster.DTO.Requests.Voucher.AddVoucherRequest;
import com.gentlemonster.DTO.Requests.Voucher.EditVoucherRequest;
import com.gentlemonster.DTO.Requests.Voucher.VoucherRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.DTO.Responses.Voucher.BaseVoucherResponse;
import com.gentlemonster.DTO.Responses.Voucher.VoucherResponse;
import com.gentlemonster.Exception.NotFoundException;

public interface IVoucherService {
    PagingResponse<List<BaseVoucherResponse>> getAllVouchers(VoucherRequest voucherRequest);
    APIResponse<VoucherResponse> getOneVoucher(String voucherID) throws NotFoundException;
    APIResponse<Boolean> createVoucher(AddVoucherRequest addVoucherRequest) throws NotFoundException;
    APIResponse<Boolean> updateVoucher(String voucherID, EditVoucherRequest editVoucherRequest) throws NotFoundException;
    APIResponse<Boolean> deleteVoucher(String voucherID) throws NotFoundException;
}
