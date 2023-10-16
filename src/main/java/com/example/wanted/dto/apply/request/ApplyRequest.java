package com.example.wanted.dto.apply.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ApplyRequest {
    private long userId;
    private long recruitId;

    @Builder
    public ApplyRequest(long userId, long recruitId) {
        this.userId = userId;
        this.recruitId = recruitId;
    }
}
