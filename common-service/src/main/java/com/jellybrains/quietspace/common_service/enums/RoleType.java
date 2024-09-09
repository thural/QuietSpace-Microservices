package com.jellybrains.quietspace.common_service.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    GUEST("GUEST"),
    USER("USER"),
    ADMIN("ADMIN");

    private final String displayName;

    RoleType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
