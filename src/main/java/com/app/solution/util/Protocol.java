package com.app.solution.util;

public enum Protocol {
    HTTP("HTTP"),
    HTTPS("HTTPS"),
    FTP("FTP"),
    SFTP("SFTP"),
    NONE("NONE");
    private String value;

    Protocol(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getName(String value) {
        for(Protocol p : Protocol.values())
            if(p.getValue().equals(value))
                return p.name();
         return Protocol.NONE.name();
    }
}
