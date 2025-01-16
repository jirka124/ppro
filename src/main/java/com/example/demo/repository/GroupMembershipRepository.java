package com.example.demo.repository;

import com.example.demo.model.GroupMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Long> {
    List<GroupMembership> findAllByUserId(long userId);
    Optional<GroupMembership> findByUserIdAndGroupId(long userId, long groupId);
}
