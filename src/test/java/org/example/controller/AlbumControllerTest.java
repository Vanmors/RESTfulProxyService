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


@WebMvcTest(AlbumController.class)
@Import(AlbumController.class)
public class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @InjectMocks
    private AlbumController albumController;

    @Mock
    AuditAspect auditAspect;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetAlbumController() throws Exception {
        String responseBody = "{\n" +
                "  \"userId\": 1,\n" +
                "  \"id\": 1,\n" +
                "  \"title\": \"quidem molestiae enim\",\n" +
                "}";

        when(restTemplate.getForEntity(any(String.class), any())).thenReturn(ResponseEntity.ok().body(responseBody));

        String expectedTitle = "quidem molestiae enim";
        // Выполнение запроса к контроллеру
        mockMvc.perform(get("/api/albums/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(expectedTitle));
    }

    @Test
    public void testPostAlbumController() throws Exception {
        mockMvc.perform(post("/api/albums")
                        .content("{}")
                        .with(csrf())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithAnonymousUser
    void cannotGetIfNotAuthorized() throws Exception {
        mockMvc.perform(get("/api/albums/{id}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testPutAlbumController() throws Exception {
        mockMvc.perform(put("/api/albums/1")
                        .content("{}")
                        .with(csrf())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteAlbumController() throws Exception {
        mockMvc.perform(delete("/api/albums/1")
                        .content("{}")
                        .with(csrf())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


}
