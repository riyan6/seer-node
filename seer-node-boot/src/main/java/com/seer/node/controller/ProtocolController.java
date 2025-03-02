package com.seer.node.controller;

import cn.hutool.core.util.StrUtil;
import com.seer.node.model.biz.Protocol;
import com.seer.node.model.in.ProtocolParseIn;
import com.seer.node.service.ProtocolService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/node/protocol")
@RequiredArgsConstructor
public class ProtocolController {

    private final ProtocolService protocolService;

    @PostMapping("/parse")
    Protocol parseLink(@RequestBody ProtocolParseIn in) {
        Assert.isTrue(StrUtil.isNotBlank(in.getLink()), "link参数不能为空");
        return protocolService.parseLink(in.getLink());
    }

}
