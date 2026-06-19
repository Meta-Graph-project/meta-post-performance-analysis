package com.project.metapostperformanceanalysis.domain.entity;

import com.project.metapostperformanceanalysis.domain.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tokens")
@EqualsAndHashCode(of = "id")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "access_token", nullable = false, length = 512)
    String accessToken;

    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    TokenType tokenType;

    @Column(name = "expires_at")
    LocalDateTime expiresAt;

    @Column(name = "created_at")
    LocalDateTime createdAt;


}
