package buky.example.accomodationservice.service;

import buky.example.accomodationservice.exceptions.NotFoundException;
import buky.example.accomodationservice.model.Accommodation;
import buky.example.accomodationservice.model.AccommodationAvailability;
import buky.example.accomodationservice.model.PriceRule;
import buky.example.accomodationservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final LocationRepository locationRepository;
    private final AccommodationRepository accommodationRepository;
    private final AccommodationAvailabilityRepository accommodationAvailabilityRepository;
    private final PatternPeriodRepository patternPeriodRepository;
    private final RangePeriodRepository rangePeriodRepository;
    private final PriceRepository priceRepository;
    private final PriceRuleRepository priceRuleRepository;

    public Accommodation createAccommodation(Accommodation accommodation) {
        locationRepository.save(accommodation.getLocation());
        return accommodationRepository.save(accommodation);
    }

    public Accommodation findOneAccommodation(Long id) throws NotFoundException {
        return accommodationRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<Accommodation> findAllAccommodation() {
        return accommodationRepository.findAll();
    }

    public void deleteAccommodation(Long id) {
        accommodationRepository.deleteById(id);
    }

    public Accommodation createAvailability(Long id, AccommodationAvailability dto) {
        Accommodation accommodation = findOneAccommodation(id);
        if (accommodation.getAvailability() != null)
            throw new RuntimeException("Availability already specified");
        dto.setAccommodation(accommodation);
        var rangePeriods = rangePeriodRepository.saveAll(dto.getAllRangePeriods());
        var patternPeriods = patternPeriodRepository.saveAll(dto.getAllPatternPeriods());

        dto.setAllRangePeriods(rangePeriods);
        dto.setAllPatternPeriods(patternPeriods);

        var rules = dto.getPrice().getPriceRules().stream().map(rule -> {
            if (rule.getPatternPeriod() == null) {
                if (rule.getRangePeriod() == null) {
                    throw new NotFoundException("No Period ranges set for this price rule");
                }
                //RANGE PERIOD
                rule.setRangePeriod(rangePeriodRepository.save(rule.getRangePeriod()));
                return rule;
            }
            //PATTERN PERIOD
            rule.setPatternPeriod(patternPeriodRepository.save(rule.getPatternPeriod()));
            return rule;
        }).collect(Collectors.toSet());

        dto.getPrice().setPriceRules(new HashSet<>(priceRuleRepository.saveAll(rules)));
        dto.setPrice(priceRepository.save(dto.getPrice()));

        var availability = accommodationAvailabilityRepository.save(dto);

        accommodation.setAvailability(availability);
        return accommodationRepository.save(accommodation);
    }

    public void deleteAvailability(Long id) {
        var availability = accommodationAvailabilityRepository.findById(id).orElseThrow(NotFoundException::new);
        var accommodation = availability.getAccommodation();
        accommodation.setAvailability(null);
        accommodationRepository.save(accommodation);
        accommodationAvailabilityRepository.deleteById(id);

    }

}
