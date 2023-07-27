package buky.example.accomodationservice.controller;

import buky.example.accomodationservice.model.Accommodation;
import buky.example.accomodationservice.model.AccommodationAvailability;
import buky.example.accomodationservice.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AccommodationService accommodationService;


    @PostMapping("/accommodation/{id}")
    public Accommodation createAvailability(@PathVariable Long id, @RequestBody AccommodationAvailability dto) {
        return accommodationService.createAvailability(id, dto);
    }

    @PutMapping("/accommodation/{id}")
    public Accommodation updateAvailability(@PathVariable Long id, @RequestBody AccommodationAvailability dto) {
        // TODO check with reservation service! can change only for ranges with no ongoing reservations!
        return null;
    }


}
