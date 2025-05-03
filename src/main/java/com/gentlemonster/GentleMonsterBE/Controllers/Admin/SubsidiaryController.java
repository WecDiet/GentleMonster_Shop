package com.gentlemonster.GentleMonsterBE.Controllers.Admin;

import com.gentlemonster.GentleMonsterBE.Contants.Enpoint;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Subsidiary.AddSubsidiaryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Subsidiary.EditSubsidiaryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Subsidiary.SubsidiaryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.Services.Subsidiary.SubsidiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Enpoint.Subsidiary.BASE)
public class SubsidiaryController {
    @Autowired
    private SubsidiaryService subsidiaryService;

    @GetMapping
    public ResponseEntity<PagingResponse<?>> getAllSubsidiary(@ModelAttribute SubsidiaryRequest subsidiaryRequest) {
        return ResponseEntity.ok(subsidiaryService.getAllSubsidiary(subsidiaryRequest));
    }

    @GetMapping(Enpoint.Subsidiary.ID)
    public ResponseEntity<APIResponse<?>> getSubsidiaryByID(@PathVariable String subsidiaryID) {
        return ResponseEntity.ok(subsidiaryService.getSubsidiaryByID(subsidiaryID));
    }

    @PostMapping(Enpoint.Subsidiary.NEW)
    public ResponseEntity<APIResponse<Boolean>> addSubsidiary(@RequestBody AddSubsidiaryRequest addSubsidiaryRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(new APIResponse<>(false, errorMessages));
        }
        return ResponseEntity.ok(subsidiaryService.addSubsidiary(addSubsidiaryRequest));
    }

    @PutMapping(Enpoint.Subsidiary.EDIT)
    public ResponseEntity<APIResponse<Boolean>> editSubsidiary(@PathVariable String subsidiaryID, @RequestBody EditSubsidiaryRequest editSubsidiaryRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(new APIResponse<>(false, errorMessages));
        }
        return ResponseEntity.ok(subsidiaryService.editSubsidiary(subsidiaryID, editSubsidiaryRequest));
    }

    @DeleteMapping(Enpoint.Subsidiary.DELETE)
    public ResponseEntity<APIResponse<Boolean>> deleteSubsidiary(@PathVariable String subsidiaryID) {
        return ResponseEntity.ok(subsidiaryService.deleteSubsidiary(subsidiaryID));
    }

}
