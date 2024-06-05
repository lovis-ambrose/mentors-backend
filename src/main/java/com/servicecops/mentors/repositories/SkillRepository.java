package com.servicecops.mentors.repositories;

import com.servicecops.mentors.models.database.SkillModel;
import com.servicecops.mentors.models.jpahelpers.repository.JetRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SkillRepository extends JetRepository<SkillModel, Long> {
    Optional<SkillModel> findByName(String name);
}
