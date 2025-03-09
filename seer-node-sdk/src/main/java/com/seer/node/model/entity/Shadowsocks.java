package com.seer.node.model.entity;

import com.seer.node.model.biz.Protocol;
import com.seer.node.model.enums.ProtocolType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Shadowsocks extends Protocol {

    private String cipher;
    private String password;

}
