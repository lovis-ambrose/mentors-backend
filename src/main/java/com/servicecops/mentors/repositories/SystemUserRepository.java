package com.servicecops.mentors.repositories;

import com.servicecops.mentors.models.database.*;
import com.servicecops.mentors.models.jpahelpers.repository.JetRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemUserRepository extends JetRepository<SystemUserModel, Long> {
    SystemUserModel findFirstByUsername(String username);
    Optional<SystemUserModel> findFirstByUsernameOrEmail(String username, String email);
    void deleteUserById(Long id);
}

