package buky.example.accomodationservice.service;

import buky.example.accomodationservice.exceptions.NotFoundException;
import buky.example.accomodationservice.model.Accommodation;
import buky.example.accomodationservice.repository.AccommodationRepository;
import buky.example.accomodationservice.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.io.NotActiveException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final LocationRepository locationRepository;
    private final AccommodationRepository accommodationRepository;

    public Accommodation createAccommodation(Accommodation accommodation) {
        locationRepository.save(accommodation.getLocation());
        return accommodationRepository.save(accommodation);
    }

    public Accommodation findOneAccommodation(Long id) throws NotFoundException {
        return accommodationRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<Accommodation> findAllAccommodation(){
        return accommodationRepository.findAll();
    }

    public void deleteAccommodation(Long id) {
        accommodationRepository.deleteById(id);
    }

}
