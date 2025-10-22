package dev.manal.shorty.service;

import dev.manal.shorty.core.CodeGenerator;
import dev.manal.shorty.core.ConflictException;
import dev.manal.shorty.core.NotFoundException;
import dev.manal.shorty.domain.Link;
import dev.manal.shorty.repository.LinkRepository;
import dev.manal.shorty.web.dto.CreateLinkRequest;
import dev.manal.shorty.web.dto.LinkResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;

    @Transactional
    public LinkResponse create(CreateLinkRequest req) {
        String code = req.getCustomCode();
        if (code != null && !code.isBlank()) {
            // ensure uniqueness for user-specified code
            if (linkRepository.findByCode(code).isPresent()) {
                throw new ConflictException("Code already in use: " + code);
            }
        } else {
            // generate unique code (retry up to 5 times)
            for (int i = 0; i < 5; i++) {
                String candidate = CodeGenerator.randomCode(8);
                if (linkRepository.findByCode(candidate).isEmpty()) {
                    code = candidate;
                    break;
                }
            }
            if (code == null) throw new ConflictException("Unable to generate unique code");
        }

        Link link = Link.builder()
                .id(UUID.randomUUID())
                .code(code)
                .targetUrl(req.getTargetUrl())
                .createdBy(req.getCreatedBy())
                .build();

        link = linkRepository.save(link);

        return LinkResponse.builder()
                .id(link.getId())
                .code(link.getCode())
                .targetUrl(link.getTargetUrl())
                .createdAt(link.getCreatedAt())
                .createdBy(link.getCreatedBy())
                .totalClicks(0L) // will compute later
                .build();
    }

    @Transactional(readOnly = true)
    public Link getByCodeOrThrow(String code) {
        return linkRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Link not found for code: " + code));
    }

    @Transactional(readOnly = true)
    public Page<Link> search(String q, int page, int size) {
        var pageable = PageRequest.of(page, size);
        if (q == null || q.isBlank()) {
            return linkRepository.findAll(pageable);
        }
        return linkRepository.findByCodeContainingIgnoreCaseOrTargetUrlContainingIgnoreCase(q, q, pageable);
    }
}
