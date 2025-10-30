package dev.manal.shorty.web;

import dev.manal.shorty.core.BadRequestException;
import dev.manal.shorty.service.LinkService;
import dev.manal.shorty.web.dto.CreateLinkRequest;
import dev.manal.shorty.web.dto.LinkResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/links")
@RequiredArgsConstructor
@Validated
public class LinkController {

    private final LinkService linkService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LinkResponse create(
            @RequestHeader(name = "X-API-Key", required = false) String apiKey,
            @Valid @RequestBody CreateLinkRequest req
    ) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new BadRequestException("Missing X-API-Key");
        }
        return linkService.create(req);
    }

    @GetMapping
    public Page<LinkResponse> list(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return linkService.search(q, page, size)
                .map(link -> {
                    long clickCount = linkService.countClicksFor(link.getId());
                    return LinkResponse.from(link, clickCount);
                });
    }


    @GetMapping("/{code}")
    public LinkResponse getByCode(@PathVariable String code) {
        var link = linkService.getByCodeOrThrow(code);
        return LinkResponse.from(link, 0L);
    }

    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String code) {
        linkService.deleteByCode(code);
    }
}
