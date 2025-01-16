package com.example.demo.service;

import com.example.demo.model.Group;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface GroupService {

    List<Group> getAllGroups();
    Optional<Group> getGroupById(long groupId);
    List<Group> getGroupsByIds(List<Long> groupIds);
    void deleteGroupById(long id);
    void saveGroup(Group group);
}
