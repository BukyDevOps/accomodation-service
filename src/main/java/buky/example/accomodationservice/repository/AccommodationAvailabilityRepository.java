package buky.example.accomodationservice.repository;

import buky.example.accomodationservice.model.AccommodationAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationAvailabilityRepository extends JpaRepository<AccommodationAvailability, Long> {
}
