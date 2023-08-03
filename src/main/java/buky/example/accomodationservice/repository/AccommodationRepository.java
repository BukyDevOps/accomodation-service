package buky.example.accomodationservice.repository;

import buky.example.accomodationservice.model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    long deleteByUserId(Long userId);

    List<Accommodation> findIdsByUserId(Long userId);


}
