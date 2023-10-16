package com.example.wanted.domain.recruit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {

    @Query("""
            SELECT r FROM Recruit r 
            WHERE r.company.name like %:keyword% 
               OR r.position like %:keyword% 
               OR r.skill like %:keyword%
            """)
    List<Recruit> findByKeyword(@Param("keyword") String keyword);

}
