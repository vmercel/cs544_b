package edu.miu.cs.cs544.mercel.jpa.recommender.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(String queueName, String message) {
        jmsTemplate.convertAndSend(queueName, message);
        System.out.println("Message sent to queue " + queueName + ": " + message);
    }
}
