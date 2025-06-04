package site.wijerathne.harshana.edupanel.uitl;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LecturerType {
    FULL_TIME("full-time"),VISITING("visiting");

    private final String type;

    LecturerType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }
}
