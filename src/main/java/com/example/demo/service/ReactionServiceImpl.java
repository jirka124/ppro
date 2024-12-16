package com.example.demo.service;

import com.example.demo.model.Reaction;
import com.example.demo.repository.ReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReactionServiceImpl implements ReactionService{

    private ReactionRepository reactionRepository;

    @Autowired
    public ReactionServiceImpl(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    @Override
    public List<Reaction> getAllReactions() {
        return reactionRepository.findAll();
    }

    public List<Reaction> getAllFromReactions(long userId) {
        return reactionRepository.findAllByFromUserId(userId);
    }

    @Override
    public List<Reaction> getAllToReactions(long userId) {
        return reactionRepository.findAllByToUserId(userId);
    }

    @Override
    public Reaction getReactionById(long id) {
        Optional<Reaction> driver = reactionRepository.findById(id);
        return driver.orElse(null);
    }

    @Override
    public Reaction getReactionByIds(long fromId, long toId) {
        return reactionRepository.findByFromUserIdAndToUserId(fromId, toId).orElse(null);
    }

    @Override
    public void deleteReactionById(long id) {
        reactionRepository.deleteById(id);
    }

    @Override
    public void saveReaction(Reaction reaction) {
        reactionRepository.save(reaction);
    }
}
