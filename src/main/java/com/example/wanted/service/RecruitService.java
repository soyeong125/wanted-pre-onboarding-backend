package com.example.wanted.service;

import com.example.wanted.domain.company.Company;
import com.example.wanted.domain.company.CompanyRepository;
import com.example.wanted.domain.recruit.Recruit;
import com.example.wanted.domain.recruit.RecruitRepository;
import com.example.wanted.dto.recruit.request.RecruitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public void saveRecruitment(RecruitRequest request){

        Company company =  companyRepository.findById(request.getCompanyId())
                .orElseThrow(IllegalArgumentException::new);

        Recruit recruit = recruitRepository.save(Recruit.builder()
                .company(company)
                .position(request.getPosition())
                .compensation(request.getCompensation())
                .content(request.getContent())
                .skill(request.getSkill())
                .build());

        recruitRepository.save(recruit);
    }




}
