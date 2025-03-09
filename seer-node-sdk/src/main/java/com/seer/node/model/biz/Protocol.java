package com.seer.node.model.biz;

import com.seer.node.model.enums.ProtocolType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
abstract public class Protocol implements Serializable {

    protected String name;
    protected ProtocolType type;
    protected String server;
    protected Integer port;

}
