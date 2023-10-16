package com.example.wanted.domain.company;

import com.example.wanted.domain.recruit.Recruit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Recruit> recruitList;

    public Company(String name) {
        this.name = name;
    }
}
