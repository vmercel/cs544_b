package edu.miu.cs.cs544.mercel.jpa.recommender.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DietitianReviewRepository extends JpaRepository<DietitianReview, Long> {

    @Query("SELECT r FROM DietitianReview r WHERE r.approved = :approved AND r.reviewDate BETWEEN :startDate AND :endDate")
    List<DietitianReview> findReviewsByApprovalAndDateRange(
            @Param("approved") Boolean approved,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM DietitianReview r WHERE r.id = :id")
    Optional<DietitianReview> findByIdWithLock(@Param("id") Long id);
}
