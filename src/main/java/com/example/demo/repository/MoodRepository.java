package com.example.demo.repository;

import com.example.demo.model.MoodEntry;

import java.util.List;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoodRepository extends JpaRepository<MoodEntry, Long> {

    List<MoodEntry> findTopByOrderByCreatedAtDesc(PageRequest pageable);
}
