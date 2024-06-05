package com.servicecops.mentors.repositories;

import com.servicecops.mentors.models.database.CategoryModel;
import com.servicecops.mentors.models.jpahelpers.repository.JetRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JetRepository<CategoryModel, Long> {
    Optional<CategoryModel> findByName(String name);
}
