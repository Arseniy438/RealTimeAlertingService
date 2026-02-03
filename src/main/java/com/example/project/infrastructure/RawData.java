package com.example.project.infrastructure;

import java.time.LocalDateTime;
import java.util.Map;

public class RawData {

    private final String type;
    private final String source;
    private final String sourceId;
    private final LocalDateTime receivedAt;
    private final Map<String, Object> payload;

    public RawData(String type, String source, String sourceId, LocalDateTime receivedAt, Map<String, Object> payload) {
        this.type = type;
        this.source = source;
        this.sourceId = sourceId;
        this.receivedAt = receivedAt;
        this.payload = Map.copyOf(payload);
    }

    public Object get(String key) {
        return payload.get(key);
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public String getType(){
        return type;
    }
}
