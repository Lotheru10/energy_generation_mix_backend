package energymix.service;

import energymix.client.CarbonIntensityClient;
import energymix.client.dto.GenerationResponseDto;
import energymix.service.model.DailyMix;
import energymix.service.model.GenerationInterval;
import energymix.utils.DtoMapper;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;


@Service
public class GenerationService {

    private final CarbonIntensityClient client;
    private final DtoMapper dtoMapper;
    private final static Set<String> CLEAN = Set.of("biomass", "nuclear", "hydro", "wind", "solar");

    public GenerationService(CarbonIntensityClient client, DtoMapper dtoMapper) {
        this.client = client;
        this.dtoMapper = dtoMapper;
    }


    public List<DailyMix> computeDailyMixesForNext3Days(){
        //gets raw data for next n days
        String from = OffsetDateTime.now(ZoneOffset.UTC).withHour(0).withMinute(30).withSecond(0).withNano(0).toString();
        String to = OffsetDateTime.now(ZoneOffset.UTC).withHour(0).withMinute(30).withSecond(0).withNano(0).plusDays(3).toString();
        GenerationResponseDto responseDto = client.getGeneration(from, to);

        //map to service model
        List<GenerationInterval> intervals = responseDto.data()
                .stream()
                .map(dtoMapper::toIntervals)
                .toList();

        List<DailyMix> days = new ArrayList<>();
        Map<String, Double> sumByFuel = new HashMap<>();
        String currentDay = null;
        for (int i=0;i< intervals.size();i++){
            //every 48 intervals we have a new day
            if (i%48==0){
                if (currentDay != null){
                    //if it's not a fist day - finish
                    days.add(finishDay(currentDay, sumByFuel));
                }
                currentDay = intervals.get(i).from();
                sumByFuel = new HashMap<>();
            }
            //calculate sum for each fuel in current day
            for (Map.Entry<String, Double> measurement : intervals.get(i).mixByFuel().entrySet()){
                String fuel = measurement.getKey();
                Double perc = measurement.getValue();
                sumByFuel.merge(fuel, perc, Double::sum);

            }
        }
        //finish for the last day
        days.add(finishDay(intervals.get(intervals.size()-1).from(), sumByFuel));

        return days;
    }

    private DailyMix finishDay(String day, Map<String, Double> sumByFuel){
        double clean = 0.0;
        Map<String, Double> avgByFuel = new LinkedHashMap<>();
        //calculate averages
        sumByFuel.forEach((key, value) -> avgByFuel.put(key, value / 48));

        //calculate clean energy
        for (String fuel : CLEAN) {
            clean += avgByFuel.getOrDefault(fuel, 0.0);
        }
        return new DailyMix(day, avgByFuel, clean);

    }

}
