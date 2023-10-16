package com.example.wanted.domain.apply;

import com.example.wanted.domain.recruit.Recruit;
import com.example.wanted.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Apply {

    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Recruit recruit;

    @ManyToOne
    private User user;

    @Builder
    public Apply(Long id, Recruit recruit, User user) {
        this.id = id;
        this.recruit = recruit;
        this.user = user;
    }
}
