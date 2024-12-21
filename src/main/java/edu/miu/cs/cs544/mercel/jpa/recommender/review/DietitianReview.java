package edu.miu.cs.cs544.mercel.jpa.recommender.review;


import edu.miu.cs.cs544.mercel.jpa.recommender.recommendation.DietRecommendation;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class DietitianReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "recommendation_id")
    private DietRecommendation recommendation;

    @Lob
    private String comments;

    private LocalDate reviewDate;

    private Boolean approved;

    @Version
    private Integer version;
    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DietRecommendation getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(DietRecommendation recommendation) {
        this.recommendation = recommendation;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}

