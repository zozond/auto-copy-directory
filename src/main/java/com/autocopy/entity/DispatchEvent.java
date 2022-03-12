package com.autocopy.entity;

public interface DispatchEvent {
    EventType getEventType();
    String getPath();
    String getName();
}
