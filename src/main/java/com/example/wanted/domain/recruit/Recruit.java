package com.example.wanted.domain.recruit;

import com.example.wanted.domain.company.Company;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Recruit {

    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String position;

    @Column(nullable = true)
    private int compensation;

    @Column(nullable = true)
    private String location;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String skill;

    @ManyToOne
    private Company company;


    @Builder
    public Recruit(Long id, String position, int compensation, String location, String content, String skill, Company company) {
        this.id = id;
        this.position = position;
        this.compensation = compensation;
        this.location = location;
        this.content = content;
        this.skill = skill;
        this.company = company;
    }

    public void updateInfo(String position, int compensation, String location, String content, String skill) {
        this.position = position;
        this.compensation = compensation;
        this.location = location;
        this.content = content;
        this.skill = skill;
    }
}
