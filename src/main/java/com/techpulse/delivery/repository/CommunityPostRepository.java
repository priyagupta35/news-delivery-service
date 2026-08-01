package com.techpulse.delivery.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.techpulse.delivery.model.CommunityPost;

@Repository
public interface CommunityPostRepository 
    extends JpaRepository<CommunityPost, Integer> {

    // This is the ONLY custom method needed here
    // save(), findById(), deleteById() etc. are all
    // already inherited from JpaRepository — never redeclare them
    List<CommunityPost> findByStatus(String status);
}