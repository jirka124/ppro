package com.example.demo.repository;

import com.example.demo.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findAllByFromUserId(long userId);
    List<Reaction> findAllByToUserId(long userId);
    Optional<Reaction> findByFromUserIdAndToUserId(long fromUserId, long toUserId);
}
