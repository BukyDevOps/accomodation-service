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
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double basePrice;
    private boolean byPerson;
    @OneToMany
    Set<PriceRule> priceRules;

}
