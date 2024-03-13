package org.example.controller;

import org.example.audit.Audit;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    private final RestTemplate restTemplate;

    public PostController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Audit
    @Cacheable("posts")
    @GetMapping("{id}")
    public ResponseEntity<String> getPost(@PathVariable Long id) {
        return restTemplate.getForEntity(BASE_URL + "posts/" + id, String.class);
    }

    @Audit
    @PostMapping()
    public ResponseEntity<String> createPost(@RequestBody String post) {
        return restTemplate.postForEntity(BASE_URL + "posts", post, String.class);
    }

    @Audit
    @PutMapping("{id}")
    public ResponseEntity<String> updatePost(@PathVariable Long id, @RequestBody String post) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(post, headers);
        return restTemplate.exchange(BASE_URL + "posts/" + id, HttpMethod.PUT, requestEntity, String.class);
    }

    @Audit
    @CacheEvict("posts")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        restTemplate.delete(BASE_URL + "posts/" + id);
        return ResponseEntity.noContent().build();
    }
}
