package com.example.demo.service;

import com.example.demo.model.Group;
import com.example.demo.model.GroupMembership;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface GroupMembershipService {

    List<GroupMembership> getAllGroupMemberships();
    List<Group> getGroupsForUser(long userId);
    Optional<GroupMembership> getMembershipByUserAndGroup(long userId, long groupId);
    void deleteGroupMembershipById(long id);
    void saveGroupMembership(GroupMembership groupMembership);
}
