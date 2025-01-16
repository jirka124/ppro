package com.example.demo.service;

import com.example.demo.model.Group;
import com.example.demo.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService{

    private GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> getGroupById(long groupId) {
        return groupRepository.findById(groupId);
    }

    public List<Group> getGroupsByIds(List<Long> groupIds) {
        return groupRepository.findAllByIdIn(groupIds);
    }

    @Override
    public void deleteGroupById(long id) {
        groupRepository.deleteById(id);
    }

    @Override
    public void saveGroup(Group group) {
        groupRepository.save(group);
    }
}
