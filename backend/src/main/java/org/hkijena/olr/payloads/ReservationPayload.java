package org.hkijena.olr.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ReservationPayload {

    @NotNull
    @JsonProperty("physicalLabId")
    private Long physicalLabId;

    @NotNull
    @JsonProperty("startTime")
    private LocalDateTime startTime;

    @NotNull
    @JsonProperty("endTime")
    private LocalDateTime endTime;

    public Long getPhysicalLabId() {
        return physicalLabId;
    }

    public void setPhysicalLabId(Long physicalLabId) {
        this.physicalLabId = physicalLabId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
