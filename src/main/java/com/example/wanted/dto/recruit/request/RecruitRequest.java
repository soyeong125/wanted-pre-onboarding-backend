package com.example.wanted.dto.recruit.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RecruitRequest {
    private long companyId;
    private String position;
    private int compensation;
    private String content;
    private String location;
    private String skill;


    @Builder
    public RecruitRequest(long companyId, String position, int compensation, String content, String location, String skill) {
        this.companyId = companyId;
        this.position = position;
        this.compensation = compensation;
        this.content = content;
        this.location = location;
        this.skill = skill;
    }
}
