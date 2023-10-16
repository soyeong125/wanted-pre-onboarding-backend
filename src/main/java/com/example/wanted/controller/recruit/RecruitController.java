package com.example.wanted.controller.recruit;

import com.example.wanted.dto.recruit.request.RecruitRequest;
import com.example.wanted.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recruitment")
public class RecruitController {

    private final RecruitService recruitService;

    @PostMapping
    public void saveRecruitment(@RequestBody RecruitRequest request){
        recruitService.saveRecruitment(request);
    }

    @PatchMapping("/{id}")
    public void updateRecruitment(@PathVariable Long id, @RequestBody RecruitRequest request){
        recruitService.updateRecruitment(id, request);
    }


}
