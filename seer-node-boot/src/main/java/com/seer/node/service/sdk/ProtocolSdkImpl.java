package com.seer.node.service.sdk;

import com.seer.node.model.biz.Protocol;
import com.seer.node.sdk.ProtocolSdk;
import com.seer.node.util.ProtocolUtil;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Collections;
import java.util.List;

@DubboService
public class ProtocolSdkImpl implements ProtocolSdk {

    @Override
    public List<Protocol> getMyProtocols() {
        String linkStr = "ss://YWVzLTEyOC1nY206Znp1SGdjaTFtUWhOaGdKNndFNkxYWHQ0UVo1N0pFR2xQWnZoVjRkSkNBcz0%3D@127.0.0.1:1234#HK";
        return Collections.singletonList(ProtocolUtil.parse(linkStr));
    }

}
