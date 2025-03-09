package com.seer.node.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.seer.node.model.biz.Protocol;
import com.seer.node.model.entity.Shadowsocks;
import com.seer.node.model.entity.Vless;
import com.seer.node.model.enums.ProtocolType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class ProtocolUtil {

    private static final Map<String, Function<String, Protocol>> PARSERS = Map.of(
            "vless:", ProtocolUtil::parseVless,
            "ss:", ProtocolUtil::parseSs
    );

    public static Protocol parse(String link) {
        if (StrUtil.isEmpty(link)) {
            return null;
        }
        return PARSERS.entrySet().stream()
                .filter(entry -> link.startsWith(entry.getKey()))
                .findFirst()
                .map(entry -> entry.getValue().apply(link))
                .orElse(null);
    }

    private static Shadowsocks parseSs(String link) {
        log.info("处理ss订阅 {}", link);
        try {
            // 移除 "ss://" 前缀
            String uriPart = link.substring(5);
            String[] uriAndFragment = uriPart.split("#", 2);
            String uri = uriAndFragment[0];
            String name = uriAndFragment.length > 1 ? URLDecoder.decode(uriAndFragment[1], StandardCharsets.UTF_8) : "Unnamed";

            // 分离用户信息和主机端口部分
            String[] userAndHost = uri.split("@", 2);
            if (userAndHost.length != 2) {
                throw new IllegalArgumentException("Invalid SS URI format");
            }

            // 解码 Base64 部分（cipher:password）
            String base64Credentials = userAndHost[0];
            String decodedCredentials = new String(Base64.decode(base64Credentials), StandardCharsets.UTF_8);
            String[] credentialParts = decodedCredentials.split(":", 2);
            if (credentialParts.length != 2) {
                throw new IllegalArgumentException("Invalid credentials format");
            }
            String cipher = credentialParts[0];
            String password = credentialParts[1];

            // 解析主机和端口
            String[] hostAndPort = userAndHost[1].split(":", 2);
            if (hostAndPort.length != 2) {
                throw new IllegalArgumentException("Invalid host:port format");
            }
            String server = hostAndPort[0];
            int port = Integer.parseInt(hostAndPort[1]);

            var ss = Shadowsocks.builder()
                    .cipher(cipher)
                    .password(password)
                    .build();
            ss.setName(name);
            ss.setType(ProtocolType.SS);
            ss.setServer(server);
            ss.setPort(port);
            return ss;
        } catch (Exception e) {
            System.err.println("Failed to parse SS link: " + e.getMessage());
            return null;
        }
    }

    private static Vless parseVless(String link) {
        log.info("处理vless订阅 {}", link);
        try {
            Assert.isTrue(link.startsWith("vless://"), "Unsupported protocol: " + link);
            String raw = link.substring("vless://".length());

            // 分离 UUID 和后续部分
            int atIndex = raw.indexOf('@');
            Assert.isTrue(atIndex > 0, "Unsupported protocol: " + link);

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
            var vless = Vless.builder()
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
            vless.setName(name.isEmpty() ? "Unnamed" : cn.hutool.core.net.URLDecoder.decode(name, StandardCharsets.UTF_8));
            vless.setType(ProtocolType.VLESS);
            vless.setServer(server);
            vless.setPort(port);
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
