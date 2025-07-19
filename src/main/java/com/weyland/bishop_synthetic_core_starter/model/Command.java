package com.weyland.bishop_synthetic_core_starter.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class Command {

    @Size(max = 1000)
    private String description;

    private Priority priority;

    @Size(max = 100)
    private String author;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(:\\d{2})?Z")
    private String time;

    public Command(String description, Priority priority, String author, String time) {
        this.description = description;
        this.priority = priority;
        this.author = author;
        this.time = time;
    }
}
