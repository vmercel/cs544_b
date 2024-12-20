package edu.miu.cs.cs544.mercel.jpa.recommender;


import edu.miu.cs.cs544.mercel.jpa.recommender.recommendation.DietRecommendation;
import edu.miu.cs.cs544.mercel.jpa.recommender.review.DietitianReview;
import edu.miu.cs.cs544.mercel.jpa.recommender.recommendation.DietRecommendationRepository;
import edu.miu.cs.cs544.mercel.jpa.recommender.review.DietitianReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DietRecommendationRepository recommendationRepository;

    @Autowired
    private DietitianReviewRepository reviewRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create a Diet Recommendation
        DietRecommendation recommendation = new DietRecommendation();
        recommendation.setUserId(1L); // Assuming user ID 1 exists in Monitoring Service
        recommendation.setRecommendationText("Increase protein intake and reduce sugary foods.");
        recommendation.setCreatedDate(LocalDate.now());
        recommendation.setStatus("PENDING");
        recommendationRepository.save(recommendation);

        // Create a Dietitian Review
        DietitianReview review = new DietitianReview();
        review.setRecommendation(recommendation);
        review.setComments("Looks good, but reduce carb intake slightly.");
        review.setReviewDate(LocalDate.now());
        review.setApproved(true);
        reviewRepository.save(review);
    }
}

