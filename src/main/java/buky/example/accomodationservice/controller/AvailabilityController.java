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


    @PostMapping("/create/{id}")
    public Accommodation createAvailability(@PathVariable Long id, @RequestBody AccommodationAvailability dto) {
        return accommodationService.createAvailability(id, dto);
    }


}
