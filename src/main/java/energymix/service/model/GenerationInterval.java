package energymix.service.model;

import java.util.ArrayList;
import java.util.Map;

public record GenerationInterval(
        String from,
        String to,
        Map<String, Double> mixByFuel
) {

}
