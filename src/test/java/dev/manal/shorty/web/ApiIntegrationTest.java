package dev.manal.shorty.web;

import dev.manal.shorty.UrlShortenerApplication;
import dev.manal.shorty.test.containers.PostgresTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UrlShortenerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ApiIntegrationTest extends PostgresTestBase {

    @Autowired
    MockMvc mvc;

    @Test
    void create_and_redirect_only() throws Exception {
        // create
        String json = """
          {"targetUrl":"https://example.org","customCode":"itest","createdBy":"test"}
          """;
        mvc.perform(post("/api/v1/links")
                        .header("X-API-Key", "dev-key")
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("\"code\":\"itest\"")));

        // redirect (records click)
        mvc.perform(get("/r/itest"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://example.org"));
    }
}
