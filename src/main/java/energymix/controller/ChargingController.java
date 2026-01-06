package energymix.controller;

import energymix.service.ChargingService;
import energymix.service.GenerationService;
import energymix.service.model.ChargingWindow;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ChargingController {
    private final ChargingService service;

    public ChargingController(ChargingService service) {
        this.service = service;
    }

    @Operation(
            summary = "Gives a window of chosen length with highest percentage of clean energy",
            description = "Returns a windows of chosen length, where the percentage of a clean energy is the highest among two next days"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK, got information successfully "),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/charging/{len}")
    public ChargingWindow bestWindow(
            @Parameter(description = "Length of the charging window") @PathVariable int len) {
        return service.findBestChargingWindow(len);
    }
}
