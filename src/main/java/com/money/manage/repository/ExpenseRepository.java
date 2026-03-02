package com.money.manage.repository;

import com.money.manage.entity.ExpenseEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity,String> {

    List<ExpenseEntity> findByProfileIdOrderByDateDesc(String profileId);

    List<ExpenseEntity> findTop5ByProfileIdOrderByDateDesc(String profileId);

    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") String profileId);

    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            String profileId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Sort sort
    );

    List<ExpenseEntity> findByProfileIdAndDateBetween(String profileId, LocalDate start,LocalDate end);
    List<ExpenseEntity> findByProfileIdAndDate(String profileId,LocalDate date);


}
