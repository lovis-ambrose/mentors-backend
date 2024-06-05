package com.servicecops.mentors.repositories;

import com.servicecops.mentors.models.database.SessionModel;
import com.servicecops.mentors.models.jpahelpers.repository.JetRepository;
import org.springframework.stereotype.Service;

@Service
public interface SessionRepository extends JetRepository<SessionModel, Long> {
}
