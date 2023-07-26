package buky.example.accomodationservice.repository;

import buky.example.accomodationservice.model.PriceRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRuleRepository extends JpaRepository<PriceRule, Long> {
}
