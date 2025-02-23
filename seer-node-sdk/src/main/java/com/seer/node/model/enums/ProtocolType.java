package com.seer.node.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProtocolType {

    VLESS("vless"),
    SS("ss");

    private final String value;
}
