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
public class Vless extends Protocol {

    private String uuid;
    private String network;
    private Boolean udp;
    private Boolean tls;
    private String flow;
    private String servername;
    private String publicKey;
    private String shortId;
    private String clientFingerprint;

}
