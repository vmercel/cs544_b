package edu.miu.cs.cs544.mercel.jpa.recommender.recommendation;

import edu.miu.cs.cs544.mercel.jpa.recommender.jms.JmsProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/rec")
@Tag(name = "Diet Recommendation API", description = "Endpoints for managing diet recommendations")
public class DietRecommendationController {

    @Autowired
    private DietRecommendationService recommendationService;

    @Autowired
    private DietRecommendationRepository recommendationRepository;

    @Autowired
    private JmsProducer jmsProducer;

    // Create a new recommendation
    @PostMapping("/{userId}")
    @Operation(summary = "Create a recommendation", description = "Generates and saves a new recommendation for the specified user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recommendation created successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<DietRecommendation> createRecommendation(@PathVariable Long userId) {
        DietRecommendation recommendation = recommendationService.generateRecommendation(userId);
        return ResponseEntity.ok(recommendation);
    }

    // Read all recommendations
    @GetMapping
    @Operation(summary = "Get all recommendations", description = "Fetches all recommendations from the database.")
    @ApiResponse(responseCode = "200", description = "List of recommendations retrieved successfully")
    public ResponseEntity<List<DietRecommendation>> getAllRecommendations() {
        return ResponseEntity.ok(recommendationService.getAllRecommendations());
    }

    // Read a recommendation by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get recommendation by ID", description = "Fetches a specific recommendation using its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recommendation retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Recommendation not found")
    })
    public ResponseEntity<DietRecommendation> getRecommendationById(@PathVariable Long id) {
        return ResponseEntity.ok(recommendationService.getRecommendationById(id));
    }

    // Update a recommendation
    @PutMapping("/{id}")
    @Operation(summary = "Update recommendation", description = "Updates the details of an existing recommendation.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recommendation updated successfully"),
            @ApiResponse(responseCode = "404", description = "Recommendation not found")
    })
    public ResponseEntity<DietRecommendation> updateRecommendation(
            @PathVariable Long id, @RequestBody DietRecommendation updatedRecommendation) {
        return ResponseEntity.ok(recommendationService.updateRecommendation(id, updatedRecommendation));
    }

    // Delete a recommendation
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete recommendation", description = "Deletes a specific recommendation by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Recommendation deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Recommendation not found")
    })
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long id) {
        recommendationService.deleteRecommendation(id);
        return ResponseEntity.noContent().build();
    }

    // Find by Status dynamically
    @GetMapping("/status/{status}")
    @Operation(summary = "Find recommendations by status", description = "Fetches recommendations based on their status (e.g., PENDING, APPROVED).")
    @ApiResponse(responseCode = "200", description = "Recommendations retrieved successfully")
    public ResponseEntity<List<DietRecommendation>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(recommendationService.findByStatus(status));
    }

    // Custom Criteria Query: Find recommendations by created date range
    @GetMapping("/date-range")
    @Operation(summary = "Find recommendations by date range", description = "Fetches recommendations created within a specified date range.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recommendations retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date range provided")
    })
    public ResponseEntity<List<DietRecommendation>> findByCreatedDateRange(
            @RequestParam("start") String startDate, @RequestParam("end") String endDate) {
        return ResponseEntity.ok(recommendationService.findByCreatedDateRange(startDate, endDate));
    }

    // Named Query: Fetch recommendations for a specific user ID
    @GetMapping("/user/{userId}")
    @Operation(summary = "Find recommendations by user ID", description = "Fetches recommendations for a specific user.")
    @ApiResponse(responseCode = "200", description = "Recommendations retrieved successfully")
    public ResponseEntity<List<DietRecommendation>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(recommendationService.findByUserId(userId));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve recommendation", description = "Approves a pending recommendation and notifies the user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recommendation approved successfully"),
            @ApiResponse(responseCode = "404", description = "Recommendation not found")
    })
    public ResponseEntity<DietRecommendation> approveRecommendation(@PathVariable Long id) {
        DietRecommendation recommendation = recommendationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recommendation not found"));

        // Update the recommendation status to APPROVED
        recommendation.setStatus("APPROVED");
        DietRecommendation updatedRecommendation = recommendationRepository.save(recommendation);

        // Prepare a multiline message for the JMS queue
        String message = String.format(
                "Diet Recommendation Approved:\n" +
                        "--------------------------\n" +
                        "Recommendation ID: %d\n" +
                        "User ID: %d\n" +
                        "Status: %s\n" +
                        "Created Date: %s\n" +
                        "Advice: %s\n" +
                        "--------------------------",
                updatedRecommendation.getId(),
                updatedRecommendation.getUserId(),
                updatedRecommendation.getStatus(),
                updatedRecommendation.getCreatedDate(),
                updatedRecommendation.getRecommendationText()
        );

        // Send the message to the JMS queue
        jmsProducer.sendMessage("diet_rec_user_queue", message);

        return ResponseEntity.ok(updatedRecommendation);
    }
}
