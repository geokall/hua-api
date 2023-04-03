package com.hua.api.enums;

public enum EventTypeEnum {

    PASSWORD("PASSWORD_CHANGE"),
    REGISTRATION("REGISTRATION");

    private final String label;

    EventTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static EventTypeEnum getEventTypeValue(String type) {

        for (EventTypeEnum gender : EventTypeEnum.values()) {
            if (gender.getLabel().equals(type)) {
                return gender;
            }
        }

        return null;
    }
}
