package dev.tceron.runnerz.run;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(JdbcClientRunRepository.class)
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JdbcClientRunRepositoryTest {
    @Autowired
    JdbcClientRunRepository repository;

    @BeforeEach
    void setup(){
        repository.create(new Run(1, "test run 1", LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.HOURS), 5, Location.INDOOR, null));
        repository.create(new Run(2, "test run 2", LocalDateTime.now(), LocalDateTime.now().plus(30, ChronoUnit.MINUTES), 5, Location.OUTDOOR, null));
    }

    @Test
    void shouldFindAllRuns() {
        List<Run> runs = repository.findAll();
        assertEquals(2, runs.size(), "Should return 2 runs");
    }

    @Test
    void shouldFindRunWithValidId() {
        Run run = repository.findById(1).get();
        assertEquals("test run 1", run.title(), "Should return the correct run");
        assertEquals(5, run.miles(), "Should return 3 miles");
    }

    @Test 
    void shouldNotFindRunWithInvalidId() {
        Run run = repository.findById(3).orElse(null);
        assertEquals(null, run, "Should not return a run");
    }

    @Test
    void shouldCreateRun() {
        repository.create( new Run(3, "test run 3", LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.HOURS), 5, Location.INDOOR, null));
        List <Run> runs = repository.findAll();
        assertEquals(3, runs.size(), "Should return 3 runs");
    }

    @Test
    void shouldUpdateRun() {
        repository.update(new Run(1, "test run 1 updated", LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.HOURS), 5, Location.INDOOR, null), 1);
        Run updatedRun = repository.findById(1).get();
        assertEquals("test run 1 updated", updatedRun.title(), "Should return the updated run");
    }

    @Test
    void shouldDeleteRun() {
        int initialSize = repository.findAll().size();
        repository.delete(1);
        List<Run> runs = repository.findAll();
        assertEquals(initialSize-1, runs.size(), "Should return 1 run less");
    }
}
