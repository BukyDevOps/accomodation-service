package buky.example.accomodationservice.repository;

import buky.example.accomodationservice.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
}
