package com.app.solution.util;

public enum Status {
    IN_PROGRESS("IN_PROGRESS"),
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE"),
    NONE("NONE");

    private String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getName(String value) {
        for(Status s : Status.values()) {
            if(s.getValue().equals(value))
                return s.name();
        }
        return Status.NONE.name();
    }
}
