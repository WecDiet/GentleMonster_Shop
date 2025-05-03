package com.gentlemonster.GentleMonsterBE.Services.Subsidiary;

import com.gentlemonster.GentleMonsterBE.DTO.Requests.Subsidiary.AddSubsidiaryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Subsidiary.EditSubsidiaryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Requests.Subsidiary.SubsidiaryRequest;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.APIResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.PagingResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Subsidiary.BaseSubsidiaryResponse;
import com.gentlemonster.GentleMonsterBE.DTO.Responses.Subsidiary.SubsidiaryResponse;

import java.util.List;

public interface ISubsidiaryService {
    PagingResponse<List<BaseSubsidiaryResponse>> getAllSubsidiary(SubsidiaryRequest subsidiaryRequest);
    APIResponse<SubsidiaryResponse> getSubsidiaryByID(String subsidiaryID);
    APIResponse<Boolean> addSubsidiary(AddSubsidiaryRequest addSubsidiaryRequest);
    APIResponse<Boolean> editSubsidiary(String subsidiaryID, EditSubsidiaryRequest editSubsidiaryRequest);
    APIResponse<Boolean> deleteSubsidiary(String subsidiaryID);
}
