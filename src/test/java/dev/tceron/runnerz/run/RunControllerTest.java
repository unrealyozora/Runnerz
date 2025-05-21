package dev.tceron.runnerz.run;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(RunController.class)
public class RunControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RunRepository repository;

    private final List<Run> runs = new ArrayList<>();

    @BeforeEach
    void setup() {
        runs.add(new Run(1, "test run 1", LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.HOURS), 5, Location.INDOOR, null));
        runs.add(new Run(2, "test run 2", LocalDateTime.now(), LocalDateTime.now().plus(30, ChronoUnit.MINUTES), 5, Location.OUTDOOR, null));
    }


    @Test
    void shouldFindAllRuns() throws Exception {
        when(repository.findAll()).thenReturn(runs);
        mvc.perform(MockMvcRequestBuilders.get("/api/runs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(runs.size())));
    }

    @Test
    void shouldFindRunById() throws Exception {
        Run run = runs.get(0);
        when (repository.findById(0)).thenReturn(Optional.of(run));
        mvc.perform(MockMvcRequestBuilders.get("/api/runs/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(run.id())))
                .andExpect(jsonPath("$.title", is(run.title())))
                .andExpect(jsonPath("$.miles", is(run.miles())))
                .andExpect(jsonPath("$.location", is(run.location().toString())));
    }

    @Test
    void shouldReturnNotFoundInvalidId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/runs/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateRun() throws Exception {
        Run newRun = new Run(3, "test run 3", LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.HOURS), 5, Location.INDOOR, null);

        mvc.perform(MockMvcRequestBuilders.post("/api/runs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRun)))
                .andExpect(status().isCreated());

    }

    @Test
    void shouldUpdateRun() throws Exception {
        Run updatedRun = new Run(1, "updated run", LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.HOURS), 5, Location.INDOOR, null);

        when(repository.findById(1)).thenReturn(Optional.of(runs.get(0)));

        mvc.perform(MockMvcRequestBuilders.put("/api/runs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRun)))
                .andExpect(status().isNoContent());
    }


    @Test
    void shouldDeleteRun() throws Exception {
        when(repository.findById(1)).thenReturn(Optional.of(runs.get(0)));

        mvc.perform(MockMvcRequestBuilders.delete("/api/runs/1"))
                .andExpect(status().isNoContent());
    }
}
