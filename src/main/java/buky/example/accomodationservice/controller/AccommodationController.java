package buky.example.accomodationservice.controller;

import buky.example.accomodationservice.dto.AccommodationResultDTO;
import buky.example.accomodationservice.dto.SearchDto;
import buky.example.accomodationservice.exceptions.NotFoundException;
import buky.example.accomodationservice.model.Accommodation;
import buky.example.accomodationservice.security.HasRole;
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
    @HasRole("HOST")
    public Accommodation create(@RequestBody Accommodation dto, Long userId) {
        return accommodationService.createAccommodation(dto, userId);
    }

    @PutMapping()
    @HasRole("HOST")
    public Accommodation update(@RequestBody Accommodation dto, Long userId) {
        return accommodationService.updateAccommodation(dto, userId);
    }

    @DeleteMapping("/{id}")
    @HasRole("HOST")
    public void deleteById(@PathVariable Long id, Long userId) {
        accommodationService.deleteAccommodation(id, userId);
    }

    @GetMapping("/host/{id}")
    public Long getHostIdForAccommodation(@PathVariable Long id) {
        return accommodationService.getHostIdForAccommodation(id);
    }

    @GetMapping("/ids-by-user/{id}")
    public List<Long> getAccommodationIdsByOwner(@PathVariable Long id) {
        return accommodationService.getAccommodationIdsByOwner(id);
    }

    @GetMapping("/get-all-host")
    @HasRole("HOST")
    public List<Accommodation> getAccommodationByHost(Long userId) {
        return accommodationService.getAllForHost(userId);
    }

    @GetMapping("/search")
    public List<AccommodationResultDTO> searchAccommodation(SearchDto searchDto) {
        return accommodationService.search(searchDto);
    }
}
