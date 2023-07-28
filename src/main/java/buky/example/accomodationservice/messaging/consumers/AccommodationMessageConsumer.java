package buky.example.accomodationservice.messaging.consumers;


import buky.example.accomodationservice.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccommodationMessageConsumer {

    private final AccommodationService accommodationService;

    @KafkaListener(topics = "accommodations_topic", groupId = "accommodation-consumer-group")
    public void listen(Long id) {
        // Add your custom logic to process the received message here
        System.out.println("Received message from accommodation_topic: " + id);
    }

}
