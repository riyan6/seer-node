package com.seer.node.model.entity;

import com.seer.node.model.biz.Protocol;
import com.seer.node.model.enums.ProtocolType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Shadowsocks extends Protocol {

    private String cipher;
    private String password;

}
