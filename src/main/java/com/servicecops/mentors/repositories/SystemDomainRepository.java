package com.servicecops.mentors.repositories;

import com.servicecops.mentors.models.database.SystemDomainModel;
import com.servicecops.mentors.models.jpahelpers.repository.JetRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemDomainRepository extends JetRepository<SystemDomainModel, Long> {
}
