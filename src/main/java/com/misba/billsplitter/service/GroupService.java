package com.misba.billsplitter.service;

import com.misba.billsplitter.dto.GroupRequest;
import com.misba.billsplitter.dto.GroupResponse;
import com.misba.billsplitter.model.Group;
import com.misba.billsplitter.model.User;
import com.misba.billsplitter.repository.GroupRepository;
import com.misba.billsplitter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupResponse createGroup(GroupRequest request, String email) {
        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<User> members = new ArrayList<>(userRepository.findAllById(request.getMemberIds()));
        if (!members.contains(creator)) {
            members.add(creator);
        }

        Group group = new Group();
        group.setName(request.getName());
        group.setCreatedBy(creator);
        group.setMembers(members);
        groupRepository.save(group);

        return mapToResponse(group);
    }

    public List<GroupResponse> getMyGroups(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return groupRepository.findByMembersId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private GroupResponse mapToResponse(Group group) {
        GroupResponse response = new GroupResponse();
        response.setId(group.getId());
        response.setName(group.getName());
        response.setCreatedBy(group.getCreatedBy().getName());
        response.setMembers(group.getMembers()
                .stream()
                .map(User::getName)
                .collect(Collectors.toList()));
        return response;
    }
}