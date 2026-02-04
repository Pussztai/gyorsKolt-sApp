package com.example.asd2.Repository;

import com.example.asd2.model.Spending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SpendingRepository extends JpaRepository<Spending, Long> {

    @Query("SELECT s.category, CAST(SUM(s.amount) AS long) FROM Spending s GROUP BY s.category")

    List<Object[]> getCategorySummary();

    List<Spending> findByCreatedAtAfter(LocalDateTime date);

}