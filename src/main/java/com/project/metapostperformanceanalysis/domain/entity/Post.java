package com.project.metapostperformanceanalysis.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")
@EqualsAndHashCode(of = "id")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "message", nullable = false)
    String message;
    @Column(name = "created_time", nullable = false)
    LocalDateTime createdTime;
    @Column(name = "like_count")
    Integer likeCount;
    @Column(name = "comment_count")
    Integer commentCount;
    @Transient
    public Integer getEngagement() {
        return (likeCount != null ? likeCount : 0) + (commentCount != null ? commentCount : 0);
    }
}
