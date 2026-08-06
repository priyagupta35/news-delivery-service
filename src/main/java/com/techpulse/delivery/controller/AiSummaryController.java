package com.techpulse.delivery.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techpulse.delivery.service.AiSummaryService;
import com.techpulse.delivery.service.ArticleService;




@RestController
@RequestMapping("/api/articles")
public class AiSummaryController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AiSummaryService aiSummaryService;

    // GET /api/articles/1/summary
    // Returns AI generated summary for a specific article
    @GetMapping("/{id}/summary")
    public ResponseEntity<String> getAiSummary(
            @PathVariable int id) {

        return articleService.getArticleById(id)
            .map(article -> {
                String content = article.getSummary() != null
                    ? article.getSummary()
                    : article.getTitle();
                String summary = aiSummaryService.summariseArticle(
                    article.getTitle(), content);
                return ResponseEntity.ok(summary);
            })
            .orElse(ResponseEntity.notFound().build());
    }
}