package org.example.controller;

import org.example.audit.AuditAspect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(PostController.class)
@Import(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @InjectMocks
    private PostController postController;

    @Mock
    AuditAspect auditAspect;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetPostController() throws Exception {
        String responseBody = "{\n" +
                "  \"userId\": 1,\n" +
                "  \"id\": 1,\n" +
                "  \"title\": \"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\n" +
                "  \"body\": \"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\"\n" +
                "}";

        when(restTemplate.getForEntity(any(String.class), any())).thenReturn(ResponseEntity.ok().body(responseBody));

        String expectedBody = "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto";
        String expectedTitle = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit";
        // Выполнение запроса к контроллеру
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(expectedTitle))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").value(expectedBody));
    }

    @Test
    public void testPostPostController() throws Exception {
        mockMvc.perform(post("/api/posts")
                        .content("{\"userId\": 1,\n" +
                                "    \"title\": \"qui est esse\",\n" +
                                "    \"body\": \"est rerum tempore \"}")
                        .with(csrf())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithAnonymousUser
    void cannotGetIfNotAuthorized() throws Exception {
        mockMvc.perform(get("/api/posts/{id}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testPutPostController() throws Exception {
        mockMvc.perform(put("/api/posts/1")
                        .content("{\"userId\": 1,\n" +
                                "    \"title\": \"qui est esse\",\n" +
                                "    \"body\": \"est rerum tempore \"}")
                        .with(csrf())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeletePostController() throws Exception {
        mockMvc.perform(delete("/api/posts/1")
                        .with(csrf())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


}
