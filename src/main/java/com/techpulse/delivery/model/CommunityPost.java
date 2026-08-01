package com.techpulse.delivery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="community_posts")
public class CommunityPost {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="title",nullable=false)
    private String title;

    @Column(name="content",columnDefinition="TEXT",nullable=false)
    private  String content;
   //MANY POSTS CAN BELONG TO ONE USER
    @ManyToOne
    @JoinColumn(name="author_id")
    private  User author;

    //MANY POSTS CAN BELONG TO ONE CATEGORY
    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;


    @Column(name = "status",columnDefinition="ENUM('PENDING','APPROVED','REJECTED')")
    private String status;

    public CommunityPost() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    


}
