package edu.miu.cs.cs544.mercel.jpa.recommender.recommendation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DietRecommendationService {

    @Autowired
    private DietRecommendationRepository recommendationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // CRUD Operations

    public DietRecommendation generateRecommendation(Long userId) {
        DietRecommendation recommendation = new DietRecommendation();
        recommendation.setUserId(userId);
        recommendation.setRecommendationText("Eat more vegetables and reduce sugar intake.");
        recommendation.setStatus("PENDING");
        recommendation.setCreatedDate(LocalDate.now());
        return recommendationRepository.save(recommendation);
    }

    public List<DietRecommendation> getAllRecommendations() {
        return recommendationRepository.findAll();
    }

    public DietRecommendation getRecommendationById(Long id) {
        return recommendationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recommendation not found with id: " + id));
    }

    public DietRecommendation updateRecommendation(Long id, DietRecommendation updatedRecommendation) {
        DietRecommendation existing = getRecommendationById(id);
        existing.setRecommendationText(updatedRecommendation.getRecommendationText());
        existing.setStatus(updatedRecommendation.getStatus());
        existing.setCreatedDate(updatedRecommendation.getCreatedDate());
        return recommendationRepository.save(existing);
    }

    public void deleteRecommendation(Long id) {
        recommendationRepository.deleteById(id);
    }

    // Dynamic Query: Find by Status
    public List<DietRecommendation> findByStatus(String status) {
        TypedQuery<DietRecommendation> query = entityManager.createQuery(
                "SELECT r FROM DietRecommendation r WHERE r.status = :status", DietRecommendation.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    // Criteria Query: Find by Created Date Range
    public List<DietRecommendation> findByCreatedDateRange(String startDate, String endDate) {
        var builder = entityManager.getCriteriaBuilder();
        var criteria = builder.createQuery(DietRecommendation.class);
        var root = criteria.from(DietRecommendation.class);

        criteria.select(root).where(
                builder.between(root.get("createdDate"), LocalDate.parse(startDate), LocalDate.parse(endDate))
        );

        return entityManager.createQuery(criteria).getResultList();
    }

    // Named Query: Find by User ID
    public List<DietRecommendation> findByUserId(Long userId) {
        TypedQuery<DietRecommendation> query = entityManager.createNamedQuery(
                "DietRecommendation.findByUserId", DietRecommendation.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
