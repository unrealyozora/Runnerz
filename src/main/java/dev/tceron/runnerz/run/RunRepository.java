package dev.tceron.runnerz.run;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Repository
public class RunRepository {
    private List<Run> runs=new ArrayList<>();
    List<Run> findAll(){
        return runs;
    }

    @PostConstruct
    private void init(){
        runs.add(new Run(1, "Morning Run", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 5, Location.OUTDOOR));
        runs.add(new Run(2, "Evening Run", LocalDateTime.now(), LocalDateTime.now().plus(30, ChronoUnit.MINUTES), 3, Location.INDOOR));
    }

    Optional<Run> findById(Integer id){
        return runs.stream().filter(run -> run.id().equals(id)).findFirst();
    }

    void create(Run run){
        runs.add(run);
    }

    void update(Run run, Integer id){
        Optional<Run> existingRun=findById(id);
        if (existingRun.isPresent()){
            runs.set(runs.indexOf(existingRun.get()), run);
        }
    }

    void delete(Integer id){
        runs.removeIf(run->run.id().equals(id));
    }
}
