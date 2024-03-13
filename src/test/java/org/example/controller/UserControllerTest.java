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


@WebMvcTest(UserController.class)
@Import(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @InjectMocks
    private UserController userController;

    @Mock
    AuditAspect auditAspect;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @WithMockUser(username = "userViewer", roles = {"USERS_VIEWER"})
    public void testGetUsersController() throws Exception {
        String responseBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"Leanne Graham\",\n" +
                "    \"username\": \"Bret\",\n" +
                "    \"email\": \"Sincere@april.biz\",\n" +
                "    \"address\": {\n" +
                "        \"street\": \"Kulas Light\",\n" +
                "        \"suite\": \"Apt. 556\",\n" +
                "        \"city\": \"Gwenborough\",\n" +
                "        \"zipcode\": \"92998-3874\",\n" +
                "        \"geo\": {\n" +
                "            \"lat\": \"-37.3159\",\n" +
                "            \"lng\": \"81.1496\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"phone\": \"1-770-736-8031 x56442\",\n" +
                "    \"website\": \"hildegard.org\",\n" +
                "    \"company\": {\n" +
                "        \"name\": \"Romaguera-Crona\",\n" +
                "        \"catchPhrase\": \"Multi-layered client-server neural-net\",\n" +
                "        \"bs\": \"harness real-time e-markets\"\n" +
                "    }\n" +
                "}";

        when(restTemplate.getForEntity(any(String.class), any())).thenReturn(ResponseEntity.ok().body(responseBody));
        // Выполнение запроса к контроллеру
        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Leanne Graham"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("Bret"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("Sincere@april.biz"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("1-770-736-8031 x56442"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.website").value("hildegard.org"));
    }

    @Test
    public void testPostUsersController() throws Exception {
        mockMvc.perform(post("/api/users")
                        .content("{   \"name\": \"Ervin Howell\",\n" +
                                "    \"username\": \"Antonette\",\n" +
                                "    \"email\": \"Shanna@melissa.tv\",\n" +
                                "    \n" +
                                "    \"phone\": \"010-692-6593 x09125\",\n" +
                                "    \"website\": \"anastasia.net\"}")
                        .with(csrf())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithAnonymousUser
    void cannotGetIfNotAuthorized() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testPutUsersController() throws Exception {
        mockMvc.perform(put("/api/users/{id}", 2L)
                        .content("{    \"id\": 2,\n" +
                                "    \"name\": \"Ervin Howell\",\n" +
                                "    \"username\": \"Antonette\",\n" +
                                "    \"email\": \"Shanna@melissa.tv\",\n" +
                                "    \n" +
                                "    \"phone\": \"010-692-6593 x09125\",\n" +
                                "    \"website\": \"anastasia.net\"}")
                        .with(csrf())
                        .with(user("userEditor").roles("USERS_EDITOR")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteUsersController() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", 1L)
                        .with(csrf())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


}
