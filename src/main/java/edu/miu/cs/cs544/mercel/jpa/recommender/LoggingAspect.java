package edu.miu.cs.cs544.mercel.jpa.recommender;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Aspect
@Component
public class LoggingAspect {

    private static final String LOG_FILE = "logs.txt";

    /**
     * Logs each method execution in FoodLogController.
     * Matches any public method in FoodLogController with any parameters.
     */
    @Before("execution(public * edu.miu.cs.cs544.mercel.jpa.recommender.recommendation.DietRecommendationController.*(..))")
    public void logBeforeEndpointCall(JoinPoint joinPoint) {
        // Get method details
        System.out.println("Starting method execution: ");
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String timestamp = LocalDateTime.now().toString();

        // Create log message
        String logMessage = String.format("[%s] Called method: %s.%s", timestamp, className, methodName);

        // Write to log file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logMessage);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }
}

