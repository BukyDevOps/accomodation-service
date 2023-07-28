package buky.example.accomodationservice.controller;

import buky.example.accomodationservice.security.HasRole;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    @HasRole("GUEST, HOST")
    public String test(Long userId){
        return userId.toString();
    }
}
