package buky.example.accomodationservice.service;

import buky.example.accomodationservice.clients.ReservationClient;
import buky.example.accomodationservice.dto.AccommodationResultDTO;
import buky.example.accomodationservice.dto.SearchDto;
import buky.example.accomodationservice.exceptions.NotFoundException;
import buky.example.accomodationservice.messaging.messages.UserDeletionResponseMessage;
import buky.example.accomodationservice.model.*;
import buky.example.accomodationservice.model.enumerations.Role;
import buky.example.accomodationservice.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private final ReservationClient reservationClient;
    private final double EARTH_RADIUS = 6371000; // Earth's radius in meters
    private final EntityManager entityManager;

    public Accommodation createAccommodation(Accommodation accommodation, Long userId) {
        locationRepository.save(accommodation.getLocation());
        accommodation.setUserId(userId);
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

    public Long getHostIdForAccommodation(Long id) {
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Accommodation is not found!"));

        return accommodation.getUserId();
    }

    public void userDeleted(UserDeletionResponseMessage message) {
        if (!message.isPermitted() || !message.getRole().equals(Role.HOST))
            return;
        deleteAllAccommodationsByUser(message.getUserId());
    }

    private void deleteAllAccommodationsByUser(Long userId) {
        accommodationRepository.deleteByUserId(userId);
    }

    public List<Long> getAccommodationIdsByOwner(Long id) {
        return accommodationRepository
                .findIdsByUserId(id)
                .stream()
                .map(Accommodation::getId)
                .collect(Collectors.toList());
    }

    public List<AccommodationResultDTO> search(SearchDto searchDto) {
        List<Long> unavailableAccommodations = reservationClient.getUnavailableAccommodations(searchDto.getStartDate(),
                searchDto.getEndDate());

        List<Accommodation> filteredAccommodations = accommodationRepository
                .findAllByGuestNumberExceptUnavailableOnes(searchDto.getGuestsNum(), unavailableAccommodations);

        filteredAccommodations = filteredAccommodations.stream().filter(a -> accommodationAvailable(a, searchDto.getStartDate(), searchDto.getEndDate())).collect(Collectors.toList());

        List<AccommodationResultDTO> results = filteredAccommodations.stream().map(a -> {
            double totalPrice = calculateTotalPrice(a, searchDto.getStartDate(), searchDto.getEndDate());

            return AccommodationResultDTO.builder()
                    .accommodation(a)
                    .totalPrice(totalPrice)
                    .pricePerGuest(totalPrice / searchDto.getGuestsNum())
                    .distance(calculateDistance(searchDto.getLat(), searchDto.getLon(),
                            a.getLocation().getLat(), a.getLocation().getLon())).build();
        }).toList();
        results.sort((o1, o2) -> (int) (o1.getDistance() - o2.getDistance()));
        return results;
    }

    private boolean accommodationAvailable(Accommodation accommodation, LocalDate reservationStart, LocalDate reservationEnd) {
        //PRE scenario 3 - rangovi koji se potencijalno mogu "zakrpiti" patternima...
        Set<LocalDate> rangeDatesOfInterest = new HashSet<>();

        //scenario 1
        var rangePeriods = accommodation.getAvailability().getAllRangePeriods();
        if (!rangePeriods.isEmpty()) {
            for (RangePeriod range : rangePeriods) {
                var rangeStart = range.getStartDate();
                var rangeEnd = range.getEndDate();
                boolean startOverlap = !reservationStart.isBefore(rangeStart);
                boolean endOverlap = !reservationEnd.isAfter(rangeEnd);
                if (startOverlap && endOverlap) {
                    return true;
                }

                if (startOverlap) {
                    rangeDatesOfInterest.addAll(dateRangeToSet(reservationStart, rangeEnd));
                    break;
                }

                if (endOverlap) {
                    rangeDatesOfInterest.addAll(dateRangeToSet(rangeStart, reservationEnd));
                }
            }
        }

        //scenario 2
        var patternPeriods = accommodation.getAvailability().getAllPatternPeriods();
        Set<LocalDate> reservationDates = dateRangeToSet(reservationStart, reservationEnd);
        Set<LocalDate> patternDates = new HashSet<>();

        if (!patternPeriods.isEmpty()) {
            patternDates = patternPeriods
                    .stream()
                    .map(patternPeriod ->
                            patternPeriod.getPeriodDates(reservationStart, reservationEnd))
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());

            if (patternDates.containsAll(reservationDates))
                return true;
        }
        //scenario 3
        if (!rangeDatesOfInterest.isEmpty()) {
            if (!patternDates.isEmpty())
                rangeDatesOfInterest.addAll(patternDates);
            return rangeDatesOfInterest.containsAll(reservationDates);
        }
        return false;
    }

    private Set<LocalDate> dateRangeToSet(LocalDate start, LocalDate end) {
        Set<LocalDate> dateSequence = new HashSet<>();

        LocalDate curr = start;
        while (!curr.isAfter(end)) {
            dateSequence.add(curr);
            curr = curr.plusDays(1);
        }

        return dateSequence;
    }

    private double calculateTotalPrice(Accommodation accommodationDto, LocalDate reservationStart, LocalDate reservationEnd) {

        var days = dateRangeToSet(reservationStart, reservationEnd);
        var priceList = accommodationDto.getAvailability().getPrice();

        return days
                .stream()
                .map(day -> calculateDayPrice(priceList, day))
                .reduce(0.0, Double::sum);
    }

    private double calculateDayPrice(Price priceList, LocalDate day) {
        //if any rule applies return
        for (PriceRule rule : priceList.getPriceRules()) {
            if (patternRuleAppliesToDate(rule.getPatternPeriod(), day))
                return rule.getSpecialPrice();
            if (rangeRuleAppliesToDate(rule.getRangePeriod(), day)) {
                return rule.getSpecialPrice();
            }
        }
        //no rule applies for the day => return BasePrice
        return priceList.getBasePrice();
    }

    private boolean patternRuleAppliesToDate(PatternPeriod patternPeriod, LocalDate day) {
        return patternPeriod.getDayOfWeek().contains(day.getDayOfWeek());
    }

    private boolean rangeRuleAppliesToDate(RangePeriod rangePeriod, LocalDate day) {
        return !day.isBefore(rangePeriod.getStartDate()) && !day.isAfter(rangePeriod.getEndDate());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    @Transactional
    public Accommodation updateAccommodation(Accommodation dto, Long userId) {

        if(dto.getUserId() != userId)
            return null;

        return entityManager.merge(dto);

    }
}
