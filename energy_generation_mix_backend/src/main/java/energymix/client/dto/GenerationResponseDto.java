package energymix.client.dto;

import java.util.List;

public record GenerationResponseDto(List<IntervalData> data) {

    public record IntervalData(
            String from,
            String to,
            List<GenerationMix> generationmix
    ) {}

    public record GenerationMix(
            String fuel,
            double perc
    ) {}

}
