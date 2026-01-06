package energymix.controller;

import energymix.service.GenerationService;
import energymix.service.model.DailyMix;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "@CrossOrigin(origins = \"https://energy-mix.onrender.com\")")
public class GenerationController {
    private final GenerationService service;

    public GenerationController(GenerationService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get information about energy generation mix for next 3 days",
            description = "Retrieves a list of DailyMixes for next 3 days about energy generation mix containing day, average use of each fuel and percentage of clean energy"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK, got information successfully "),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/mix3days")
    public List<DailyMix> dailyMix(){
        return service.computeDailyMixesForNext3Days();
    }
}
