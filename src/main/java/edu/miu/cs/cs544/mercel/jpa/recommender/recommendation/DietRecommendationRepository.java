package edu.miu.cs.cs544.mercel.jpa.recommender.recommendation;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietRecommendationRepository extends JpaRepository<DietRecommendation, Long> {
    List<DietRecommendation> findByUserIdAndStatus(Long userId, String status);
}

