package com.seer.node.service.impl;

import com.seer.node.model.biz.Protocol;
import com.seer.node.service.ProtocolService;
import com.seer.node.util.ProtocolUtil;
import org.springframework.stereotype.Service;

@Service
public class ProtocolServiceImpl implements ProtocolService {

    @Override
    public Protocol parseLink(String link) {
        return ProtocolUtil.parse(link);
    }

}
