package com.seer.node.controller;

import com.seer.node.model.biz.Protocol;
import com.seer.node.service.ProtocolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/node/protocol")
@RequiredArgsConstructor
public class ProtocolController {

    private final ProtocolService protocolService;

    @GetMapping("/parse")
    Protocol parseLink(String link) {
        return protocolService.parseLink(link);
    }

}
