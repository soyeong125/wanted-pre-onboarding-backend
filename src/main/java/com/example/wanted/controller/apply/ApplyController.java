package com.example.wanted.controller.apply;

import com.example.wanted.dto.apply.request.ApplyRequest;
import com.example.wanted.dto.recruit.request.RecruitRequest;
import com.example.wanted.service.apply.ApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apply")
public class ApplyController {

    private final ApplyService applyService;

    @PostMapping
    public void apply(@RequestBody ApplyRequest request){
        applyService.apply(request);
    }
}
