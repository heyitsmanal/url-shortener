package dev.manal.shorty.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CreateLinkRequest {

    @NotBlank
    @URL(message = "Must be a valid absolute URL (http/https)")
    private String targetUrl;

    // optional custom short code; if null we'll generate one
    @Size(min = 1, max = 16, message = "Code length must be 1..16")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "Allowed: letters, digits, _ and -")
    private String customCode;

    // optional audit info
    @Size(max = 100)
    private String createdBy;
}
