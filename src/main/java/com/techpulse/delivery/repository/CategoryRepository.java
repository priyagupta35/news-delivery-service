package com.techpulse.delivery.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techpulse.delivery.model.Article;
import com.techpulse.delivery.model.Category;
import com.techpulse.delivery.model.Source;

@Repository
    public interface CategoryRepository extends JpaRepository<Category,Integer>
    {

    Optional<Article> findById(Long categoryId);

    Optional<Source> findByName(String string);

    }

