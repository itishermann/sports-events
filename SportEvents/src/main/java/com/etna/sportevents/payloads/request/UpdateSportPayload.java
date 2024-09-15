package com.etna.sportevents.payloads.request;

import javax.validation.constraints.Size;
import java.util.Objects;

public class UpdateSportPayload {
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name = null;

    public UpdateSportPayload(String name) {
        this.name = name;
    }

    public UpdateSportPayload() {
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
        if (!(o instanceof UpdateSportPayload)) return false;
        UpdateSportPayload that = (UpdateSportPayload) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "UpdateSportPayload{" +
                "name='" + name + '\'' +
                '}';
    }
}
