package buky.example.accomodationservice.dto;

import buky.example.accomodationservice.model.Accommodation;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccommodationResultDTO {

    private Accommodation accommodation;
    private double totalPrice;
    private double pricePerGuest;
    private double distance;

}
