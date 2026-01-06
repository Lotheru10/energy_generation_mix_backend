package energymix.utils;

import energymix.client.dto.GenerationResponseDto;
import energymix.service.model.GenerationInterval;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class DtoMapper {


    public GenerationInterval toIntervals(GenerationResponseDto.IntervalData data){
        Map<String, Double> mix = new LinkedHashMap<>();
        for (GenerationResponseDto.GenerationMix m : data.generationmix()){
            mix.put(m.fuel(), m.perc());
        }
        return new GenerationInterval(data.from(), data.to(), mix);
    }
}
