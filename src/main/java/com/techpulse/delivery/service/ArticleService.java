package com.techpulse.delivery.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techpulse.delivery.model.Article;
import com.techpulse.delivery.repository.ArticleRepository;

@Service
public class ArticleService {

    private static final Logger logger =
        LogManager.getLogger(ArticleService.class);

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        logger.debug("Fetching all articles");
        List<Article> articles = articleRepository.findAll();
        logger.info("Retrieved {} articles", articles.size());
        return articles;
    }

    public Optional<Article> getArticleById(int id) {
        logger.debug("Fetching article with id: {}", id);
        Optional<Article> article = articleRepository.findById(id);
        if (article.isEmpty()) {
            logger.warn("Article not found with id: {}", id);
        }
        return article;
    }

    public List<Article> getArticlesByCategory(int categoryId) {
        logger.debug("Fetching articles for category id: {}", categoryId);
        return articleRepository.findByCategoryId(categoryId);
    }

    public List<Article> getApprovedArticles() {
        logger.debug("Fetching approved articles");
        return articleRepository.findByStatus("APPROVED");
    }

    public Article saveArticle(Article article) {
        logger.info("Saving article: {}", article.getTitle());
        Article saved = articleRepository.save(article);
        logger.info("Article saved with id: {}", saved.getId());
        return saved;
    }

    public void deleteArticle(int id) {
        logger.info("Deleting article with id: {}", id);
        articleRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Cannot delete — article not found " +
                    "with id: {}", id);
                return new RuntimeException(
                    "Article not found with id: " + id);
            });
        articleRepository.deleteById(id);
        logger.info("Article deleted successfully with id: {}", id);
    }
}