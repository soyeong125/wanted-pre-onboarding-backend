package com.example.wanted.dto.recruit.response;

import com.example.wanted.domain.recruit.Recruit;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class RecruitDetailResponse {

    private Long recruitId;
    private String companyName;
    private String location;
    private String position;
    private int compensation;
    private String skill;
    private String content;
    private List<Long> recruitList;

    @Builder
    public RecruitDetailResponse(Long recruitId, String companyName, String location, String position, int compensation, String skill, String content, List<Long> recruitList) {
        this.recruitId = recruitId;
        this.companyName = companyName;
        this.location = location;
        this.position = position;
        this.compensation = compensation;
        this.skill = skill;
        this.content = content;
        this.recruitList = recruitList;
    }

    public static RecruitDetailResponse buildEntity (Recruit recruit) {

        List<Long> list = recruit.getCompany().getRecruitList()
                    .stream()
                    .filter(rlist -> !rlist.getId().equals(recruit.getId()))
                    .map(Recruit::getId)
                    .toList();

        return builder()
                .recruitId(recruit.getId())
                .companyName(recruit.getCompany().getName())
                .location(recruit.getLocation())
                .position(recruit.getPosition())
                .compensation(recruit.getCompensation())
                .content(recruit.getContent())
                .skill(recruit.getSkill())
                .recruitList(list)
                .build();
    }
}
