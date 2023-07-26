package buky.example.accomodationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class AccommodationAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Accommodation accommodation;
    @OneToMany
    List<RangePeriod> allRangePeriods;
    @OneToMany
    List<PatternPeriod> allPatternPeriods;
    @OneToOne
    private Price price;

}
