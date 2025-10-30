package dev.manal.shorty.web;

import dev.manal.shorty.domain.Link;
import dev.manal.shorty.service.ClickService;
import dev.manal.shorty.service.LinkService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class RedirectController {

    private final LinkService linkService;
    private final ClickService clickService;

    @GetMapping("/r/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code, HttpServletRequest request) {
        Link link = linkService.getByCodeOrThrow(code);
        clickService.recordClick(link, request);
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, link.getTargetUrl())
                .build();
    }
}
