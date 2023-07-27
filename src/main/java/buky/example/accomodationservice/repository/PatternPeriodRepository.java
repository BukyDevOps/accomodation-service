package buky.example.accomodationservice.repository;

import buky.example.accomodationservice.model.PatternPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatternPeriodRepository extends JpaRepository<PatternPeriod,Long> {
}
