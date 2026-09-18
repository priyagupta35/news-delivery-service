package com.techpulse.delivery.service;

import java.util.List;
import java.util.Optional;
import com.techpulse.delivery.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techpulse.delivery.model.CommunityPost;
import com.techpulse.delivery.repository.CommunityPostRepository;

@Service
public class CommunityPostService {

    private static final Logger logger =
        LogManager.getLogger(CommunityPostService.class);

    @Autowired
    private CommunityPostRepository communityPostRepository;

    public List<CommunityPost> getApprovedPosts() {
        logger.debug("Fetching approved community posts");
        List<CommunityPost> posts =
            communityPostRepository.findByStatus("APPROVED");
        logger.info("Retrieved {} approved community posts",
            posts.size());
        return posts;
    }

    public Optional<CommunityPost> getPostById(int id) {
        logger.debug("Fetching community post with id: {}", id);
        Optional<CommunityPost> post =
            communityPostRepository.findById(id);
        if (post.isEmpty()) {
            logger.warn("Community post not found with id: {}", id);
        }
        return post;
    }

   public CommunityPost submitPost(
        CommunityPost post, User user) {

    logger.info("New community post submission: {}",
        post.getTitle());

    post.setAuthor(user);
    post.setStatus("PENDING");

    CommunityPost saved =
        communityPostRepository.save(post);

    logger.info(
        "Community post saved with id: {} by user: {} and status PENDING",
        saved.getId(),
        user.getEmail());

    return saved;
}
    

    public CommunityPost updatePostStatus(int id, String status) {
        logger.info("Updating community post id: {} to status: {}",
            id, status);

        CommunityPost post = communityPostRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Community post not found with id: {}", id);
                return new RuntimeException(
                    "Post not found with id: " + id);
            });

        post.setStatus(status);
        CommunityPost updated = communityPostRepository.save(post);
        logger.info("Community post id: {} status updated to: {}",
            id, status);
        return updated;
    }
}