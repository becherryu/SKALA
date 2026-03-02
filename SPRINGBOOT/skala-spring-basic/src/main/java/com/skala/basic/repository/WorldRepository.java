package com.skala.basic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skala.basic.table.WorldEntity;

public interface WorldRepository extends JpaRepository<WorldEntity, Long> {
    Optional<WorldEntity> findByName(String name);

    List<WorldEntity> findBySinceGreaterThanEqual(Integer since);

    @Query("SELECT w FROM WorldEntity w WHERE w.since >= :since")
    List<WorldEntity> findSomething(Integer since);
}
