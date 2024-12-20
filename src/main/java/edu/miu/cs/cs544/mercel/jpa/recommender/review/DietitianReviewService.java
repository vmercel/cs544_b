package edu.miu.cs.cs544.mercel.jpa.recommender.review;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DietitianReviewService {
    @Autowired
    private DietitianReviewRepository reviewRepository;

    public DietitianReview submitReview(DietitianReview review) {
        return reviewRepository.save(review);
    }
}
