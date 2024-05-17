package com.QuizApplication.QuizApplication.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepo extends JpaRepository<VerificationToken,Long> {
    Optional<VerificationToken> findByToken(String token);

    @Query(value = "delete from verification_token where token = ?1",nativeQuery = true)
    void deleteToken(String token);

    @Query(value = "select * from verification_token where token = ?1",nativeQuery = true)
    VerificationToken find(String oldToken);

    @Query(value = "select token from verification_token where user_id = ?1",nativeQuery = true)
    String getTokenByUser(Long id);
}
