package energymix.client;

import energymix.client.dto.GenerationResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.OffsetDateTime;

@Component
public class CarbonIntensityClient {
    private final WebClient webClient;

    public CarbonIntensityClient() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("https://api.carbonintensity.org.uk");
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        this.webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl("https://api.carbonintensity.org.uk")
                .build();
    }
    public GenerationResponseDto getGeneration(String fromStr, String toStr){

        return webClient.get()
                .uri("/generation/{from}/{to}", fromStr, toStr)
                .retrieve()
                .bodyToMono(GenerationResponseDto.class)
                .block();
    }


}
