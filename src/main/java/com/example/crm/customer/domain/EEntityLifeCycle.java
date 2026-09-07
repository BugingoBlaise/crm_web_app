package com.example.crm.customer.domain;

import lombok.Getter;

@Getter
public enum EEntityLifeCycle {
    CREATED,
    ACTIVE,
    APPROVED,
    DEACTIVATED,
}
