package com.project.metapostperformanceanalysis.repository;

import com.project.metapostperformanceanalysis.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByMetaPostId(String id);
    @Query("SELECT p FROM Post p ORDER BY (p.likeCount + p.commentCount) DESC")
    List<Post> findTopByEngagement(Pageable pageable);

}
