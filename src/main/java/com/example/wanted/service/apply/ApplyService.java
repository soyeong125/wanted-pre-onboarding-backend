package com.example.wanted.service.apply;

import com.example.wanted.domain.apply.Apply;
import com.example.wanted.domain.apply.ApplyRepository;
import com.example.wanted.domain.company.Company;
import com.example.wanted.domain.recruit.Recruit;
import com.example.wanted.domain.recruit.RecruitRepository;
import com.example.wanted.domain.user.User;
import com.example.wanted.domain.user.UserRepository;
import com.example.wanted.dto.apply.request.ApplyRequest;
import com.example.wanted.dto.recruit.request.RecruitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplyService {

    private final ApplyRepository applyRepository;
    private final UserRepository userRepository;
    private final RecruitRepository recruitRepository;

    @Transactional
    public void apply(ApplyRequest request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(IllegalArgumentException::new);
        Recruit recruit = recruitRepository.findById(request.getRecruitId())
                .orElseThrow(IllegalArgumentException::new);

        if (user.hasAppliedTo(recruit)) {
            throw new IllegalStateException("이미 지원하셨습니다.");
        }

        Apply apply = applyRepository.save(Apply.builder()
                .recruit(recruit)
                .user(user)
                .build());

        applyRepository.save(apply);
    }

}
