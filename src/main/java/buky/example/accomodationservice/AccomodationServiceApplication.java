package buky.example.accomodationservice;

import buky.example.accomodationservice.model.*;
import buky.example.accomodationservice.repository.AccommodationRepository;
import buky.example.accomodationservice.repository.LocationRepository;
import buky.example.accomodationservice.service.AccommodationService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@AllArgsConstructor
public class AccomodationServiceApplication implements CommandLineRunner {

    private final AccommodationRepository accommodationRepository;
    private final LocationRepository locationRepository;
    private final AccommodationService accommodationService;

    public static void main(String[] args) {
        SpringApplication.run(AccomodationServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {


        var location1 = Location.builder()
                .name("loc1")
                .fullAddress("FullAddress 1")
                .lon(0.0001)
                .lat(0.0002)
                .build();
        var accommodation1 = Accommodation.builder()
                .location(location1)
                .autoApproveReservation(true)
                .userId(1L)
                .maxGuestNum(10)
                .minGuestNum(1)
                .tags(Set.of("WI FI", "Parking", "Bazen"))
                .name("Soba")
                .description("Neki opis kako je super i povoljno...")
                .images(Set.of("https://res.cloudinary.com/dkdue4xbo/image/upload/v1663276292/products/rbhgqm9hyljv3wjosp2h.jpg"))
                .build();

        locationRepository.saveAll(List.of(location1));
        accommodationRepository.saveAll(List.of(accommodation1));

        var accommodationAvailability = AccommodationAvailability
                .builder()
                .allPatternPeriods(
                        List.of(PatternPeriod
                                .builder()
                                .dayOfWeek(Set.of(DayOfWeek.MONDAY))
                                .build()))
                .allRangePeriods(
                        List.of(RangePeriod
                                .builder()
                                .startDate(LocalDate.now())
                                .endDate(LocalDate.now().plusDays(10))
                                .build()))
                .price(Price
                        .builder()
                        .basePrice(175.0)
                        .byPerson(true)
                        .priceRules(
                                Set.of(PriceRule
                                        .builder()
                                        .specialPrice(170.0)
                                        .patternPeriod(
                                                PatternPeriod
                                                        .builder()
                                                        .dayOfWeek(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY))
                                                        .build())
                                        .build()))
                        .build())
                .build();


        accommodationService.createAvailability(1L, accommodationAvailability);
    }
}
