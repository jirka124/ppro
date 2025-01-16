package com.example.demo.service;

import com.example.demo.model.Group;
import com.example.demo.model.GroupMembership;
import com.example.demo.repository.GroupMembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupMembershipServiceImpl implements GroupMembershipService{

    private GroupMembershipRepository groupMembershipRepository;

    @Autowired
    public GroupMembershipServiceImpl(GroupMembershipRepository groupMembershipRepository) {
        this.groupMembershipRepository = groupMembershipRepository;
    }

    @Override
    public List<GroupMembership> getAllGroupMemberships() {
        return groupMembershipRepository.findAll();
    }

    @Override
    public List<Group> getGroupsForUser(long userId) {
        List<GroupMembership> memberships = groupMembershipRepository.findAllByUserId(userId);
        return memberships.stream()
                .map(GroupMembership::getGroup)
                .collect(Collectors.toList());
    }

    public Optional<GroupMembership> getMembershipByUserAndGroup(long userId, long groupId) {
        return groupMembershipRepository.findByUserIdAndGroupId(userId, groupId);
    }

    @Override
    public void deleteGroupMembershipById(long id) {
        groupMembershipRepository.deleteById(id);
    }

    @Override
    public void saveGroupMembership(GroupMembership groupMembership) {
        groupMembershipRepository.save(groupMembership);
    }
}
