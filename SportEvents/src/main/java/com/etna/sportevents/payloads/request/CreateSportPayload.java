package com.etna.sportevents.payloads.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class CreateSportPayload {
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    public CreateSportPayload(String name) {
        this.name = name;
    }

    public CreateSportPayload() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateSportPayload)) return false;
        CreateSportPayload that = (CreateSportPayload) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "CreateSportPayload{" +
                "name='" + name + '\'' +
                '}';
    }
}
