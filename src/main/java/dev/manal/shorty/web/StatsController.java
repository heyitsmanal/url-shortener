package dev.manal.shorty.web;

import dev.manal.shorty.domain.Link;
import dev.manal.shorty.dto.LinkStatsResponse;
import dev.manal.shorty.service.LinkService;
import dev.manal.shorty.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/links/{code}/stats")
public class StatsController {

    private final LinkService linkService;
    private final StatsService statsService;

    @GetMapping
    public LinkStatsResponse summary(@PathVariable String code) {
        Link link = linkService.getByCodeOrThrow(code);
        return statsService.statsFor(link);
    }
}
