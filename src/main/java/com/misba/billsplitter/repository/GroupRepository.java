package com.misba.billsplitter.repository;

import com.misba.billsplitter.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByMembersId(Long userId);
}