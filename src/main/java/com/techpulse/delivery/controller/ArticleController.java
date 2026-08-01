package com.techpulse.delivery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.techpulse.delivery.model.Article;
import com.techpulse.delivery.service.ArticleService;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    // RestTemplate used to call News Ingestion Service
    // This is how two microservices communicate
    @Autowired
    private RestTemplate restTemplate;

    @Value("${ingestion.service.url}")
    private String ingestionServiceUrl;

    // GET /api/articles
    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    // GET /api/articles/1
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(
            @PathVariable int id) {
        return articleService.getArticleById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/articles/approved
    @GetMapping("/approved")
    public ResponseEntity<List<Article>> getApprovedArticles() {
        return ResponseEntity.ok(
            articleService.getApprovedArticles());
    }

    // GET /api/articles/category/1
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Article>> getByCategory(
            @PathVariable int categoryId) {
        return ResponseEntity.ok(
            articleService.getArticlesByCategory(categoryId));
    }

    // POST /api/articles
    @PostMapping
    public ResponseEntity<Article> createArticle(
            @RequestBody Article article) {
        return ResponseEntity.ok(
            articleService.saveArticle(article));
    }

    // POST /api/articles/fetch
    // Delivery Service calls Ingestion Service via REST
    // This is the microservices communication in action
    @PostMapping("/fetch")
    public ResponseEntity<String> fetchNews() {
        String ingestionUrl = ingestionServiceUrl
            + "/api/ingestion/fetch";
        restTemplate.postForObject(
            ingestionUrl, null, String.class);
        return ResponseEntity.ok(
            "News ingestion triggered via Ingestion Service");
    }

    // DELETE /api/articles/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(
            @PathVariable int id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}