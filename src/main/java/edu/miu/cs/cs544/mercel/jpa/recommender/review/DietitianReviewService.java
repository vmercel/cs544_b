package edu.miu.cs.cs544.mercel.jpa.recommender.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DietitianReviewService {
    @Autowired
    private DietitianReviewRepository reviewRepository;

    public List<DietitianReview> findReviewsByApprovalAndDateRange(Boolean approved, LocalDate startDate, LocalDate endDate) {
        return reviewRepository.findReviewsByApprovalAndDateRange(approved, startDate, endDate);
    }

    public DietitianReview updateReviewWithLock(Long id, String comments) {
        DietitianReview review = reviewRepository.findByIdWithLock(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setComments(comments);
        return reviewRepository.save(review);
    }
}
