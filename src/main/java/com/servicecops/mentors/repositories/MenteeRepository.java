package com.servicecops.mentors.repositories;

import com.servicecops.mentors.models.database.MenteeModel;
import com.servicecops.mentors.models.jpahelpers.repository.JetRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenteeRepository extends JetRepository<MenteeModel, Long> {
}
