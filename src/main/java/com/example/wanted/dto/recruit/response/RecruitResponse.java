package com.example.wanted.dto.recruit.response;

import com.example.wanted.domain.recruit.Recruit;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RecruitResponse {

    private Long recruitId;
    private String companyName;
    private String location;
    private String position;
    private int compensation;
    private String skill;

    public RecruitResponse(Recruit recruit) {
        this.recruitId = recruit.getId();
        this.companyName = recruit.getCompany().getName();
        this.location = recruit.getLocation();
        this.position = recruit.getPosition();
        this.compensation = recruit.getCompensation();
        this.skill = recruit.getSkill();
    }
}