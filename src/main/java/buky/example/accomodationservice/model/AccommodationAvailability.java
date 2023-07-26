package buky.example.accomodationservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @ManyToOne
    private Accommodation accommodation;
    @OneToMany
    List<RangePeriod> allRangePeriods;
    @OneToMany
    List<PatternPeriod> allPatternPeriods;
    @OneToOne
    private Price price;

}
