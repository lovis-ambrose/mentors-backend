package com.servicecops.mentors.repositories;

import com.servicecops.mentors.models.database.SystemRoleModel;
import com.servicecops.mentors.models.jpahelpers.repository.JetRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemRoleRepository extends JetRepository<SystemRoleModel, Long> {
    Optional<SystemRoleModel> findFirstByRoleCode(String code);
}
