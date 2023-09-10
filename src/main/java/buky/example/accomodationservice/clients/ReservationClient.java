package buky.example.accomodationservice.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationClient {

    private final RestTemplate restTemplate;

    @Value(value = "${reservation.BaseURL}")
    private String baseURL;

    public List<Integer> getUnavailableAccommodations(LocalDate start, LocalDate end) {
        String endpoint = baseURL + "/unavailable?start=" + start.toString() + "&end=" + end.toString();
        return sendRequest(endpoint);
    }

    private List<Integer> sendRequest(String endpoint) {
        try {
            ResponseEntity<List<Integer>> responseEntity = restTemplate.exchange(endpoint, HttpMethod.GET,
                    new HttpEntity<List<Integer>>(new HttpHeaders()),
                    ParameterizedTypeReference.forType(List.class));

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                if(responseEntity.getBody() == null) return new ArrayList<>();

                return responseEntity.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}