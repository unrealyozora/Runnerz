package dev.tceron.runnerz.run;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Repository
public class RunRepository {
    private static final Logger log = LoggerFactory.getLogger(RunRepository.class);
    private final JdbcClient jdbcClient;
    
    public RunRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Run> findAll() {
        return jdbcClient.sql("SELECT * FROM run")
                .query(Run.class)
                .list();
    }

    public Optional<Run> findById(Integer id) {
        return jdbcClient.sql("SELECT * FROM run WHERE id = :id")
                .param("id", id)
                .query(Run.class)
                .optional();
    }

    public void create(Run run){
        var updated=jdbcClient.sql("INSERT INTO run (id, title, started_on, completed_on, miles, location) VALUES (?, ?, ?, ?, ?, ?)")
                .params(List.of(run.id(), run.title(), run.startedOn(), run.completedOn(), run.miles(), run.location().toString()))
                .update();
        Assert.state(updated==1, "Failed to create run "+ run.title());
    }

    public void update(Run run, Integer id) {
        var updated=jdbcClient.sql("UPDATE run SET title = :title, started_on = :startedOn, completed_on = :completedOn, miles = :miles, location = :location WHERE id = :id")
                .param("title", run.title())
                .param("startedOn", run.startedOn())
                .param("completedOn", run.completedOn())
                .param("miles", run.miles())
                .param("location", run.location().name())
                .param("id", id)
                .update();

        Assert.state(updated==1, "Failed to update run "+ run.title());
    }

    public void delete(Integer id) {
        var updated=jdbcClient.sql("DELETE FROM run WHERE id = :id")
                .param("id", id)
                .update();

        Assert.state(updated==1, "Failed to delete run with id "+ id);
    }
    public int count(){
        return jdbcClient.sql("SELECT * FROM run")
                .query().listOfRows().size();
    }

    public void saveAll(List<Run> runs){
        runs.stream().forEach(this::create);
    }

    public List<Run> findByLocation(String location) {
        return jdbcClient.sql("SELECT * FROM run WHERE location = :location")
                .param("location", location)
                .query(Run.class)
                .list();
    }
}