package com.example.demo.service;

import com.example.demo.model.Reaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReactionService {

    List<Reaction> getAllReactions();
    List<Reaction> getAllFromReactions(long userId);
    List<Reaction> getAllToReactions(long userId);
    Reaction getReactionById(long id);
    Reaction getReactionByIds(long fromId, long toId);
    void deleteReactionById(long id);
    void saveReaction(Reaction reaction);
}
