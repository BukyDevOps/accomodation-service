package buky.example.accomodationservice.dto;

import buky.example.accomodationservice.model.Location;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccommodationDTO {

    private String name;
    private String description;
    private int minGuestNum;
    private int maxGuestNum;
    private Location location;
    private Set<String> tags;
}
