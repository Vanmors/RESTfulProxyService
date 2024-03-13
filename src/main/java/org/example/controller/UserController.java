package org.example.controller;

import org.example.audit.Audit;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    private final RestTemplate restTemplate;

    public UserController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Audit
    @Cacheable("users")
    @GetMapping("{id}")
    public ResponseEntity<String> getUser(@PathVariable Long id) {
        return restTemplate.getForEntity(BASE_URL + "users/" + id, String.class);
    }

    @Audit
    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody String post) {
        return restTemplate.postForEntity(BASE_URL + "users", post, String.class);
    }

    @Audit
    @PutMapping("{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody String post) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(post, headers);
        return restTemplate.exchange(BASE_URL + "users/" + id, HttpMethod.PUT, requestEntity, String.class);
    }

    @Audit
    @CacheEvict("users")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        restTemplate.delete(BASE_URL + "users/" + id);
        return ResponseEntity.noContent().build();
    }
}
