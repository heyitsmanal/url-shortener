package dev.manal.shorty.service;

import dev.manal.shorty.domain.Click;
import dev.manal.shorty.domain.Link;
import dev.manal.shorty.repository.ClickRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class ClickService {

    private final ClickRepository clickRepository;

    @Value("${security.ipPepper:dev-pepper}")
    private String ipPepper;

    @Transactional
    public void recordClick(Link link, HttpServletRequest request) {
        String ip = clientIp(request);
        String ipHash = sha256(ip + "|" + ipPepper);
        String userAgent = request.getHeader("User-Agent");
        String referrer = request.getHeader("Referer");

        Click click = Click.builder()
                .link(link)
                .ts(Instant.now())
                .ipHash(ipHash)
                .userAgent(userAgent)
                .referrer(referrer)
                .build();

        clickRepository.save(click);
    }

    private static String clientIp(HttpServletRequest req) {
        String h = req.getHeader("X-Forwarded-For");
        if (h != null && !h.isBlank()) {
            int comma = h.indexOf(',');
            return comma > 0 ? h.substring(0, comma).trim() : h.trim();
        }
        return req.getRemoteAddr();
    }

    private static String sha256(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] d = md.digest(s.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(d);
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}
