package dev.tceron.runnerz.run;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record Run(
    Integer id,
    @NotEmpty
    String title,
    LocalDateTime startedOn,
    LocalDateTime completedOn,
    @Positive
    Integer miles,
    Location location
){

    public Run{
        if (!completedOn.isAfter(startedOn)){
            throw new IllegalArgumentException("Completed date must be after started date");
        }
    }
}
