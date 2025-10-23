package com.gentlemonster.Services.Voucher;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gentlemonster.Contants.MessageKey;
import com.gentlemonster.DTO.Requests.Voucher.AddVoucherRequest;
import com.gentlemonster.DTO.Requests.Voucher.EditVoucherRequest;
import com.gentlemonster.DTO.Requests.Voucher.VoucherRequest;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.DTO.Responses.Voucher.BaseVoucherResponse;
import com.gentlemonster.DTO.Responses.Voucher.VoucherResponse;
import com.gentlemonster.Entities.Product;
import com.gentlemonster.Entities.Voucher;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.Repositories.IProductRepository;
import com.gentlemonster.Repositories.IVoucherRepository;
import com.gentlemonster.Repositories.Specification.ProductSpecification;
import com.gentlemonster.Utils.LocalizationUtils;

import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class VoucherService implements IVoucherService {
    @Autowired
    private IVoucherRepository iVoucherRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LocalizationUtils localizationUtil;
    @Autowired
    private IProductRepository iProductRepository;

    @Override
    public PagingResponse<List<BaseVoucherResponse>> getAllVouchers(VoucherRequest voucherRequest) {
        List<BaseVoucherResponse> listBaseVoucherResponses;
        List<Voucher> listVouchers;
        Pageable pageable;
        // Implement logic to fetch vouchers from the repository
        if (voucherRequest.getPage() == 0 && voucherRequest.getLimit() == 0) {
            listVouchers = iVoucherRepository.findAll();
            listBaseVoucherResponses = listVouchers.stream()
                    .map(voucher -> modelMapper.map(voucher, BaseVoucherResponse.class))
                    .toList();
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtil.getLocalizedMessage(MessageKey.VOUCHER_GET_SUCCESS));
            return new PagingResponse<>(listBaseVoucherResponses, messages,1,(long) listBaseVoucherResponses.size());
        }else{
            voucherRequest.setPage(Math.max(voucherRequest.getPage(),1));
            pageable = PageRequest.of(voucherRequest.getPage() - 1, voucherRequest.getLimit());
        }

        Page<Voucher> voucherPage = iVoucherRepository.findAll(pageable);
        listBaseVoucherResponses = voucherPage.getContent().stream()
                .map(voucher -> modelMapper.map(voucher, BaseVoucherResponse.class))
                .toList();
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.VOUCHER_GET_SUCCESS));
        return new PagingResponse<>(listBaseVoucherResponses, messages, voucherPage.getNumber() + 1, voucherPage.getTotalElements());
    }

    @Override
    @Transactional
    public APIResponse<Boolean> createVoucher(AddVoucherRequest addVoucherRequest)throws NotFoundException {
        Voucher voucher = modelMapper.map(addVoucherRequest, Voucher.class);
        if (iVoucherRepository.existsByCode(addVoucherRequest.getCode())) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.VOUCHER_CODE_EXISTS));
        }
        List<String> listCodeProduct = addVoucherRequest.getProductCode();
        Set<Product> listProducts = null;
        if(listCodeProduct != null && !listCodeProduct.isEmpty()){
            Specification<Product> productSpecification = ProductSpecification.getProductByCode(listCodeProduct);
            List<Product> products = iProductRepository.findAll(productSpecification);
            
                        // Kiểm tra xem tất cả productCodes có được tìm thấy không
            List<String> foundProductCodes = products.stream()
                    .map(Product::getCode)
                    .collect(Collectors.toList());
            List<String> missingProductCodes = listCodeProduct.stream()
                    .filter(code -> !foundProductCodes.contains(code))
                    .collect(Collectors.toList());

            if (!missingProductCodes.isEmpty()) {
                throw new NotFoundException(
                        localizationUtil.getLocalizedMessage(
                                MessageKey.PRODUCT_NOT_FOUND,
                                String.join(", ", missingProductCodes)
                        )
                );
            }

            listProducts = new HashSet<>(products);
        }
        if (!listProducts.isEmpty()) {
            voucher.setProducts(listProducts);
        }
        voucher.setStatus(true); // Mặc định voucher mới được tạo là "active"
        iVoucherRepository.save(voucher);

        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.VOUCHER_CREATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> updateVoucher(String voucherID, EditVoucherRequest editVoucherRequest) throws NotFoundException {
        Voucher voucher = iVoucherRepository.findById(UUID.fromString(voucherID)).orElse(null);
        if (voucher == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.VOUCHER_NOT_FOUND));
        }

        // Update voucher details
        modelMapper.map(editVoucherRequest, voucher);

        // Update associated products
        List<String> listCodeProduct = editVoucherRequest.getProductCode();
        Set<Product> listProducts = null;
        if (listCodeProduct != null && !listCodeProduct.isEmpty()) {
            Specification<Product> productSpecification = ProductSpecification.getProductByCode(listCodeProduct);
            List<Product> products = iProductRepository.findAll(productSpecification);
            if (products.size() != listCodeProduct.size()) {
            // Check if all product codes are found
            List<String> foundProductCodes = products.stream()
                .map(Product::getCode)
                .collect(Collectors.toList());
            List<String> missingProductCodes = listCodeProduct.stream()
                .filter(code -> !foundProductCodes.contains(code))
                .collect(Collectors.toList());

            if (!missingProductCodes.isEmpty()) {
            throw new NotFoundException(
                localizationUtil.getLocalizedMessage(
                        MessageKey.PRODUCT_NOT_FOUND,
                        String.join(", ", missingProductCodes)
                        )
                    );
                }
            }

            listProducts = new HashSet<>(products);
        }

        if (listProducts != null && !listProducts.isEmpty()) {
            voucher.setProducts(listProducts);
        } else {
            voucher.setProducts(null); // Clear products if no valid products are provided
        }

        iVoucherRepository.save(voucher);

        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.VOUCHER_UPDATE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<Boolean> deleteVoucher(String voucherID) throws NotFoundException {
        Voucher voucher = iVoucherRepository.findById(UUID.fromString(voucherID)).orElse(null);
        if (voucher == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.VOUCHER_NOT_FOUND));
        }
        iVoucherRepository.delete(voucher);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.VOUCHER_DELETE_SUCCESS));
        return new APIResponse<>(true, messages);
    }

    @Override
    public APIResponse<VoucherResponse> getOneVoucher(String voucherID) throws NotFoundException {
        Voucher voucher = iVoucherRepository.findById(UUID.fromString(voucherID)).orElse(null);
        if (voucher == null) {
            throw new NotFoundException(localizationUtil.getLocalizedMessage(MessageKey.VOUCHER_NOT_FOUND));
        }
        VoucherResponse voucherResponse = modelMapper.map(voucher, VoucherResponse.class);
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtil.getLocalizedMessage(MessageKey.VOUCHER_GET_SUCCESS));
        return new APIResponse<>(voucherResponse, messages);
    }

}
