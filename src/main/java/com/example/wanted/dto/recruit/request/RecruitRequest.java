package com.example.wanted.dto.recruit.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RecruitRequest {
    private long companyId;
    private String position;
    private int compensation;
    private String content;
    private String skill;

    @Builder
    public RecruitRequest(long companyId, String position, int compensation, String content, String skill) {
        this.companyId = companyId;
        this.position = position;
        this.compensation = compensation;
        this.content = content;
        this.skill = skill;
    }
}
