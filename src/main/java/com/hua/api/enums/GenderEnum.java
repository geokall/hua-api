package com.hua.api.enums;

public enum GenderEnum {

    MALE("Άρρεν"),
    FEMALE("Θήλυ");

    private final String label;

    GenderEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static GenderEnum getGenderEnumValue(String type) {

        for (GenderEnum gender : GenderEnum.values()) {
            if (gender.getLabel().equals(type)) {
                return gender;
            }
        }

        return null;
    }
}
