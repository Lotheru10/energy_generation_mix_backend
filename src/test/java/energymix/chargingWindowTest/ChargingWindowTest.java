package energymix.chargingWindowTest;


import energymix.service.ChargingService;
import energymix.service.GenerationService;
import energymix.service.model.ChargingWindow;
import energymix.service.model.DailyMix;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class ChargingWindowTest {

    @Autowired
    ChargingService service;

    @Autowired
    MockMvc mockMvc;

    @Test
    void serviceReturnsData(){
        ChargingWindow window = service.findBestChargingWindow(3);

        assertNotNull(window);
        assertFalse(window.cleanPerc().isNaN());
    }

    @Test
    void callingEndpointIsOk() throws Exception {
        mockMvc.perform(get("/api/charging/{len}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.cleanPerc").exists());
    }
}
