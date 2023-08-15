package buky.example.accomodationservice.model;

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
@Entity
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private int minGuestNum = 0;
    private int maxGuestNum;
    private boolean autoApproveReservation = false;
    @ElementCollection
    private Set<String> tags;
    @ElementCollection
    private Set<String> images;
    @ManyToOne(cascade = CascadeType.ALL)
    private Location location;
    @OneToOne(cascade = CascadeType.ALL)
    private AccommodationAvailability availability;


}
