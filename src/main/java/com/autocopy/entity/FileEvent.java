package com.autocopy.entity;

public class FileEvent implements DispatchEvent{
    private EventType eventType;
    private String path;
    private String name;

    public FileEvent(EventType eventType, String filepath, String filename) {
        this.eventType = eventType;
        this.path = filepath;
        this.name = filename;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getName() {
        return name;
    }
}
