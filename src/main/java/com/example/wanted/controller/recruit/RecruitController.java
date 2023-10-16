package com.example.wanted.controller.recruit;

import com.example.wanted.dto.recruit.request.RecruitRequest;
import com.example.wanted.dto.recruit.response.RecruitDetailResponse;
import com.example.wanted.dto.recruit.response.RecruitResponse;
import com.example.wanted.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/{id}")
    public void deleteRecruitment(@PathVariable Long id){
        recruitService.deleteRecruitment(id);
    }

    @GetMapping
    public List<RecruitResponse> getRecruitments(@RequestParam(required = false) String keyword) {
        return recruitService.getRecruitments(keyword);
    }

    @GetMapping("/{id}")
    public RecruitDetailResponse getRecruitmentDetail(@PathVariable Long id) {
        return recruitService.getRecruitmentDetail(id);
    }


}
