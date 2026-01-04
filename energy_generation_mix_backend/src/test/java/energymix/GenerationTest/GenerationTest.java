package energymix.GenerationTest;

import energymix.controller.GenerationController;
import energymix.service.GenerationService;
import energymix.service.model.DailyMix;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class GenerationTest {
    @Autowired
    GenerationService service;

    @Autowired
    MockMvc mockMvc;

    @Test
    void serviceReturnsData(){
        List<DailyMix> mixes = service.computeDailyMixesForNext3Days();

        assertNotNull(mixes);
        assertFalse(mixes.isEmpty());
        assertNotNull(mixes.get(0));
        assertFalse(mixes.get(0).averageUse().isEmpty());
    }

    @Test
    void callingEndpointIsOk() throws Exception {
        mockMvc.perform(get("/api/mix3days"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").exists())
                .andExpect(jsonPath("$[0].averageUse").exists())
                .andExpect(jsonPath("$[0].cleanPerc").exists());
    }
}
