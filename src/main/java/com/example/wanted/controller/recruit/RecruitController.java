package com.example.wanted.controller.recruit;

import com.example.wanted.dto.recruit.request.RecruitCreateRequest;
import com.example.wanted.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recruitment")
public class RecruitController {

    private final RecruitService recruitService;

    @PostMapping
    public void saveRecruitment(@RequestBody RecruitCreateRequest request){
        recruitService.saveRecruitment(request);
    }


}
