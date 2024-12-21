package edu.miu.cs.cs544.mercel.jpa.recommender.recommendation;

import jakarta.persistence.*;
import java.time.LocalDate;

@NamedQuery(
        name = "DietRecommendation.findByUserId",
        query = "SELECT r FROM DietRecommendation r WHERE r.userId = :userId"
)
@Entity
public class DietRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // Reference to the user (from Monitoring Service)

    @Lob
    private String recommendationText;

    private String status; // PENDING, APPROVED, REJECTED

    private LocalDate createdDate;

    @Version
    private Long version;
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRecommendationText() {
        return recommendationText;
    }

    public void setRecommendationText(String recommendationText) {
        this.recommendationText = recommendationText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
