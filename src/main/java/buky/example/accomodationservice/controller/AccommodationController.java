package buky.example.accomodationservice.controller;

import buky.example.accomodationservice.exceptions.NotFoundException;
import buky.example.accomodationservice.model.Accommodation;
import buky.example.accomodationservice.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accommodation")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping()
    public List<Accommodation> getAllAccommodations() {
        return accommodationService.findAllAccommodation();
    }

    @GetMapping("/{id}")
    public Accommodation getAccommodationById(@PathVariable Long id) throws NotFoundException {
        return accommodationService.findOneAccommodation(id);
    }

    @PostMapping()
    public Accommodation create(@RequestBody Accommodation dto) {
        return accommodationService.createAccommodation(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        accommodationService.deleteAccommodation(id);
    }

    @GetMapping("/host/{id}")
    public Long getHostIdForAccommodation(@PathVariable Long id) {
        return accommodationService.getHostIdForAccommodation(id);
    }

    @GetMapping("/ids-by-user/{id}")
    public List<Long> getAccommodationIdsByOwner(@PathVariable Long id) {
        return accommodationService.getAccommodationIdsByOwner(id);
    }
}
