package energymix.ClientTest;

import energymix.client.CarbonIntensityClient;
import energymix.client.dto.GenerationResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CarbonIntensityClientTest {

    @Autowired
    CarbonIntensityClient carbonIntensityClient;

    @Test
    void checkApiData(){
        String from = OffsetDateTime.now(ZoneOffset.UTC).withHour(0).withMinute(0).withSecond(0).withNano(0).toString();
        String to = OffsetDateTime.now(ZoneOffset.UTC).withHour(0).withMinute(0).withSecond(0).plusDays(3).withNano(0).toString();

        GenerationResponseDto responseDto = carbonIntensityClient.getGeneration(from, to);

        assertNotNull(responseDto);
        assertNotNull(responseDto.data());
        assertFalse(responseDto.data().isEmpty());
        System.out.println(responseDto);
    }
}
