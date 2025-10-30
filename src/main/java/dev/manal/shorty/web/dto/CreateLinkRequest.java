package dev.manal.shorty.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLinkRequest {

    @NotBlank
    @URL(message = "Must be a valid absolute URL (http/https)")
    private String targetUrl;

    // optional custom short code; if null or empty we'll generate one
    @Pattern(regexp = "^[A-Za-z0-9_-]*$", message = "Allowed: letters, digits, _ and -")
    private String customCode;

    // optional audit info
    private String createdBy;
}
