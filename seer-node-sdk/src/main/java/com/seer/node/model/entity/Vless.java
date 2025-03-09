package com.seer.node.model.entity;

import com.seer.node.model.biz.Protocol;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
