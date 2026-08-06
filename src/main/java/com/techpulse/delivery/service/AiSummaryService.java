package com.techpulse.delivery.service;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiSummaryService {

    private static final Logger logger =
        LogManager.getLogger(AiSummaryService.class);

    private final ChatClient chatClient;

    public AiSummaryService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String summariseArticle(String title, String content) {
        if (content == null || content.isBlank()) {
            logger.warn("Cannot summarise article with empty content: {}",
                title);
            return "No content available to summarise.";
        }

        logger.info("Generating AI summary for article: {}", title);

        String prompt = String.format(
            "Summarise the following technology article in 2 to 3 " +
            "clear sentences. Be concise and focus on the key points.\n\n" +
            "Title: %s\n\nContent: %s", title, content);

        try {
            String summary = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

            logger.info("AI summary generated successfully for: {}",
                title);
            return summary;

        } catch (Exception e) {
            logger.error("Failed to generate AI summary for article: {}",
                title, e);
            return "Summary generation failed. Please try again.";
        }
    }
}