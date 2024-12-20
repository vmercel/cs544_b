package edu.miu.cs.cs544.mercel.jpa.recommender.jms;

import edu.miu.cs.cs544.mercel.jpa.recommender.recommendation.DietRecommendation;
import edu.miu.cs.cs544.mercel.jpa.recommender.recommendation.DietRecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class JmsConsumer {

    @Value("${jms.queue.food-log-receive}")
    private String foodLogQueue;

    @Autowired
    private DietRecommendationRepository recommendationRepository;

    @JmsListener(destination = "${jms.queue.food-log-receive}")
    public void receiveMessage(String message) {
        System.out.println("Message received: " + message);

        try {
            // Split the message into lines
            String[] lines = message.split("\n");

            // Extract relevant information using specific lines
            String userIdStr = lines[2].split(":")[1].trim();
            String mealType = lines[4].split(":")[1].trim();
            String caloriesStr = lines[5].split(":")[1].trim();
            String foodItem = lines[8].split(":")[1].trim();

            // Parse numeric fields
            Long userId = Long.parseLong(userIdStr); // Parse User ID
            int calories = Integer.parseInt(caloriesStr); // Parse Calories

            // Generate a diet recommendation
            DietRecommendation recommendation = new DietRecommendation();
            recommendation.setUserId(userId);
            recommendation.setRecommendationText(
                    String.format("For %s, consider balancing calorie intake. Current calories: %d.", mealType, calories)
            );
            recommendation.setStatus("PENDING");
            recommendation.setCreatedDate(LocalDate.now());

            // Save the recommendation to the database
            recommendationRepository.save(recommendation);

            System.out.println("Recommendation generated and saved with status PENDING for User ID: " + userId);

        } catch (NumberFormatException e) {
            System.err.println("Error processing message: Invalid number format in numeric field. Details: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error processing message: Missing expected data in the message. Details: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }
}
