package com.servicecops.mentors.repositories;

import com.servicecops.mentors.models.database.SystemPermissionModel;
import com.servicecops.mentors.models.jpahelpers.repository.JetRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemPermissionRepository extends JetRepository<SystemPermissionModel, Long> {
    Optional<SystemPermissionModel> findFirstByPermissionCode(String permission_code);
}
