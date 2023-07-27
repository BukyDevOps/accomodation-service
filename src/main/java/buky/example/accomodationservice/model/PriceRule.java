package buky.example.accomodationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PriceRule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double specialPrice;
    @OneToOne
    private RangePeriod rangePeriod;
    @OneToOne
    private PatternPeriod patternPeriod;

}
