package com.seer.node.model.biz;

import com.seer.node.model.enums.ProtocolType;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Protocol {

    private String name;
    private ProtocolType type;
    private String server;
    private Integer port;

}
