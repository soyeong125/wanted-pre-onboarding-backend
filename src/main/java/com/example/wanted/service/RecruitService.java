package com.example.wanted.service;

import com.example.wanted.domain.company.Company;
import com.example.wanted.domain.company.CompanyRepository;
import com.example.wanted.domain.recruit.Recruit;
import com.example.wanted.domain.recruit.RecruitRepository;
import com.example.wanted.dto.recruit.request.RecruitRequest;
import com.example.wanted.dto.recruit.response.RecruitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public void updateRecruitment(Long id, RecruitRequest request) {
        Recruit recruit =  recruitRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        recruit.updateInfo(request.getPosition(), request.getCompensation(), request.getLocation(), request.getContent(), request.getSkill());
    }

    @Transactional
    public void deleteRecruitment(Long id) {
        Recruit recruit =  recruitRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        recruitRepository.delete(recruit);
    }

    @Transactional(readOnly = true)
    public List<RecruitResponse> getRecruitments(String keyword){

        List<Recruit> list;

        if(keyword == null || keyword.length() == 0){
            list = recruitRepository.findAll();
        }
        else{
            list = recruitRepository.findByKeyword(keyword);
        }

        return list.stream()
                .map(RecruitResponse::new)
                .collect(Collectors.toList());
    }


}
