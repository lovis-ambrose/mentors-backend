package com.servicecops.mentors.repositories;

import com.servicecops.mentors.models.database.MentorModel;
import com.servicecops.mentors.models.jpahelpers.repository.JetRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentorRepository extends JetRepository<MentorModel, Long> {
}
