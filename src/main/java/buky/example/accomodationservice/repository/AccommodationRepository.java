package buky.example.accomodationservice.repository;

import buky.example.accomodationservice.model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    long deleteByUserId(Long userId);

    List<Accommodation> findIdsByUserId(Long userId);

    @Query("""
        select a from Accommodation a 
        where (?1 between a.minGuestNum and a.maxGuestNum) and a.id not in ?2
    """)
    List<Accommodation> findAllByGuestNumberExceptUnavailableOnes(int guestNum, List<Long> unavailableAccommodations);
}
