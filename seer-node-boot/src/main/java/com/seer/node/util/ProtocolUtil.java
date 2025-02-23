package com.seer.node.util;

import com.seer.node.model.biz.Protocol;
import com.seer.node.model.entity.Vless;
import com.seer.node.model.enums.ProtocolType;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ProtocolUtil {

    public static Protocol parse(String link) {
        if (link != null && link.startsWith("vless:")) {
            return parseVless(link);
        }
        return null;
    }

    private static Vless parseVless(String link) {
        try {
            // 移除协议头 "vless://"
            if (!link.startsWith("vless://")) {
                throw new IllegalArgumentException("Unsupported protocol: " + link);
            }
            String raw = link.substring("vless://".length());

            // 分离 UUID 和后续部分
            int atIndex = raw.indexOf('@');
            if (atIndex == -1) {
                throw new IllegalArgumentException("Invalid VLESS link format: missing '@'");
            }
            String uuid = raw.substring(0, atIndex);
            String remaining = raw.substring(atIndex + 1);

            // 分离服务器地址、端口和参数
            int hashIndex = remaining.indexOf('#');
            String serverAndParams = hashIndex == -1 ? remaining : remaining.substring(0, hashIndex);
            String name = hashIndex == -1 ? "" : remaining.substring(hashIndex + 1);

            int questionMarkIndex = serverAndParams.indexOf('?');
            String serverPort = questionMarkIndex == -1 ? serverAndParams : serverAndParams.substring(0, questionMarkIndex);
            String query = questionMarkIndex == -1 ? "" : serverAndParams.substring(questionMarkIndex + 1);

            // 分离服务器和端口
            int colonIndex = serverPort.lastIndexOf(':');
            if (colonIndex == -1) {
                throw new IllegalArgumentException("Invalid server:port format");
            }
            String server = serverPort.substring(0, colonIndex);
            int port = Integer.parseInt(serverPort.substring(colonIndex + 1));

            // 解析查询参数
            Map<String, String> params = parseQuery(query);

            // 构建 Vless 对象
            Vless vless = Vless.builder()
                    .name(name.isEmpty() ? "Unnamed" : name)
                    .type(ProtocolType.VLESS)
                    .server(server)
                    .port(port)
                    .uuid(uuid)
                    .network(params.getOrDefault("type", "tcp"))
                    .udp(params.containsKey("udp") ? Boolean.parseBoolean(params.get("udp")) : null)
                    .tls("reality".equals(params.get("security")) || "tls".equals(params.get("security")))
                    .flow(params.getOrDefault("flow", ""))
                    .servername(params.getOrDefault("sni", ""))
                    .publicKey(params.getOrDefault("pbk", ""))
                    .shortId(params.getOrDefault("sid", ""))
                    .clientFingerprint(params.getOrDefault("fp", ""))
                    .build();

            return vless;

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse VLESS link: " + e.getMessage(), e);
        }
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return params;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                params.put(key, value);
            }
        }
        return params;
    }


}
