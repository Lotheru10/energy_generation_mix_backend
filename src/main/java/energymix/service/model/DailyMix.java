package energymix.service.model;

import java.util.Map;

public record DailyMix(
        String date,
        Map<String, Double> averageUse,
        double cleanPerc
) {
}
