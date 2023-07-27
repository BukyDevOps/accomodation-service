package buky.example.accomodationservice.repository;

import buky.example.accomodationservice.model.RangePeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RangePeriodRepository extends JpaRepository<RangePeriod, Long> {
}
