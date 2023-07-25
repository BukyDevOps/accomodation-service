package buky.example.accomodationservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class TestTest {

    @Test
    public void test() {
        assert(true);
    }
}
