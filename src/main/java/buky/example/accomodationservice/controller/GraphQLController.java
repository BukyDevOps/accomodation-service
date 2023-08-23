package buky.example.accomodationservice.controller;

import buky.example.accomodationservice.clients.UserClient;
import buky.example.accomodationservice.model.Accommodation;
import buky.example.accomodationservice.model.User;
import buky.example.accomodationservice.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GraphQLController {

    private final AccommodationService accommodationService;
    private final UserClient userClient;

    @QueryMapping
    Accommodation accommodationById(@Argument Long id) {
        return accommodationService.findOneAccommodation(id);
    }

    @SchemaMapping(typeName="Accommodation", field="user")
    public User getAuthor(Accommodation accommodation) {
        return userClient.getUserById(accommodation.getUserId());
    }

}
