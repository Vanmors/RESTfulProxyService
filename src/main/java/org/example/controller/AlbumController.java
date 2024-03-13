package org.example.controller;


import org.example.audit.Audit;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    private final RestTemplate restTemplate;

    public AlbumController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Audit
    @Cacheable("albums")
    @GetMapping("{id}")
    public ResponseEntity<String> getAlbum(@PathVariable Long id) {
        return restTemplate.getForEntity(BASE_URL + "albums/" + id, String.class);
    }

    @Audit
    @PostMapping("")
    public ResponseEntity<String> createAlbum(@RequestBody String post) {
        return restTemplate.postForEntity(BASE_URL + "albums", post, String.class);
    }

    @Audit
    @CachePut("albums")
    @PutMapping("{id}")
    public ResponseEntity<String> updateAlbum(@PathVariable Long id, @RequestBody String post) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(post, headers);
        return restTemplate.exchange(BASE_URL + "albums/" + id, HttpMethod.PUT, requestEntity, String.class);
    }

    @Audit
    @CacheEvict("albums")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        restTemplate.delete(BASE_URL + "albums/" + id);
        return ResponseEntity.noContent().build();
    }
}
