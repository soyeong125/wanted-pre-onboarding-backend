package com.example.wanted.domain.user;

import com.example.wanted.domain.apply.Apply;
import com.example.wanted.domain.recruit.Recruit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name="USER_TBL")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Apply> applyList = new ArrayList<>();

    public boolean hasAppliedTo(Recruit recruit) {
        return applyList.stream().anyMatch(apply -> apply.getRecruit().equals(recruit));
    }

    public User(String name) {
        this.name = name;
    }
}

