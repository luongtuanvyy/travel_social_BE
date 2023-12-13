package com.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.dto.AccountData;
import com.app.entity.Tour;

@Repository
@EnableJpaRepositories
public interface TourRepository extends JpaRepository<Tour, Integer> {
    Page<Tour> findAll(Specification<Tour> spec, Pageable pageable);

    @Query("SELECT t FROM Tour t WHERE t.discount IS NOT NULL")
    Page<Tour> DiscountIsNotNull(Specification<Tour> spec, Pageable pageable);

    Optional<Tour> findTourById(Integer id);

    @Query(value = "SELECT new com.app.dto.AccountData(a.id, a.name, a.avatar, a.isVerify, a.email)\n" +
            "FROM Account a\n" +
            "WHERE a.email IN (\n" +
            "  SELECT t.createdBy\n" +
            "  FROM Tour t\n" +
            "  WHERE t.id = :tourId\n" +
            ")")
    Page<AccountData> getCompanyCreatedBY(@Param("tourId") Integer tourId, Pageable pageable);

    @Query(value = "SELECT ((SUM(b.adult) * t.adult) + (SUM(b.children) * t.children) + (SUM(b.baby) * t.baby)) FROM Booking b LEFT JOIN Tour t ON t.id = b.tourId WHERE t.id = :tourId  AND MONTH(t.endDate) = 7 AND YEAR(t.endDate) = 2023 GROUP BY b.tourId")
    Optional<Integer> getRevenueTour(@Param("tourId") Integer tourId);
}
