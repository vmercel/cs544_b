package edu.miu.cs.cs544.mercel.jpa.recommender.review;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietitianReviewRepository extends JpaRepository<DietitianReview, Long> {
}

