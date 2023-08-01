package buky.example.accomodationservice.messaging.consumers;


import buky.example.accomodationservice.messaging.messages.UserDeletionResponseMessage;
import buky.example.accomodationservice.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final AccommodationService accommodationService;

    @KafkaListener(topics = "accommodations_topic", groupId = "accommodation-consumer-group")
    public void listen(Long id) {
        // Add your custom logic to process the received message here
        System.out.println("Received message from accommodation_topic: " + id);
    }

    @KafkaListener(topics = "user-deletion-permission-topic",containerFactory = "accommodationRatingListenerContainerFactory")
    public void userDeletionPermission(UserDeletionResponseMessage message) {
        accommodationService.userDeleted(message);
    }

}
