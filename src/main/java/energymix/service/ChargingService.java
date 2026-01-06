package energymix.service;

import energymix.client.CarbonIntensityClient;
import energymix.client.dto.GenerationResponseDto;
import energymix.service.model.ChargingWindow;
import energymix.service.model.GenerationInterval;
import energymix.utils.DtoMapper;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
public class ChargingService {
    private final CarbonIntensityClient client;
    private final DtoMapper dtoMapper;
    private final static Set<String> CLEAN = Set.of("biomass", "nuclear", "hydro", "wind", "solar");

    public ChargingService(CarbonIntensityClient client, DtoMapper dtoMapper) {
        this.client = client;
        this.dtoMapper = dtoMapper;
    }

    public ChargingWindow findBestChargingWindow(int lenght){
        String from = OffsetDateTime.now(ZoneOffset.UTC).withHour(0).withMinute(30).withSecond(0).withNano(0).plusDays(1).toString();
        String to = OffsetDateTime.now(ZoneOffset.UTC).withHour(0).withMinute(30).withSecond(0).withNano(0).plusDays(3).toString();

        GenerationResponseDto responseDto = client.getGeneration(from, to);

        //map to service model
        List<GenerationInterval> intervals = responseDto.data()
                .stream()
                .map(dtoMapper::toIntervals)
                .toList();

        //first possible window
        String start = intervals.get(0).from();
        String end = intervals.get(2*lenght-1).to();
        double clean = 0.0;
        double bestClean = 0.0;
        double[] cleanInIntervals = new double[intervals.size()];
        for (int i =0; i<2*lenght;i++){
            bestClean = getClean(intervals, bestClean, cleanInIntervals, i);
        }

        //sliding window for rest possibilities
        double prevClean = bestClean;
        for (int i = lenght*2; i<intervals.size();i++){
            clean = prevClean - cleanInIntervals[i-lenght*2];
            clean = getClean(intervals, clean, cleanInIntervals, i);
            if (clean > bestClean){
                bestClean = clean;
                start = intervals.get(i-lenght*2+1).from();
                end = intervals.get(i).to();
            }
            prevClean = clean;
        }

        return new ChargingWindow(start, end, bestClean/(lenght*2));
    }

    private double getClean(List<GenerationInterval> intervals, double clean, double[] cleanInIntervals, int i) {
        for (Map.Entry<String, Double> measurement : intervals.get(i).mixByFuel().entrySet()){
            String fuel = measurement.getKey();
            double perc = measurement.getValue();
            if(CLEAN.contains(fuel)){
                cleanInIntervals[i]+=perc;
            }
        }
        clean+=cleanInIntervals[i];
        return clean;
    }
}
