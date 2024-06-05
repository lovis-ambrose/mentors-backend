package com.servicecops.mentors.services.Category;

import com.alibaba.fastjson.JSONObject;
import com.servicecops.mentors.models.database.CategoryModel;
import com.servicecops.mentors.repositories.CategoryRepository;
import com.servicecops.mentors.services.base.BaseWebActionsService;
import com.servicecops.mentors.utils.OperationReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends BaseWebActionsService {
    @Autowired
    private CategoryRepository categoryRepository;
    private final OperationReturnObject res = new OperationReturnObject();

    @Override
    public OperationReturnObject switchActions(String action, JSONObject request) {
        return switch(action) {
            case "createCategory" -> createCategory(request);
            case "getAllCategories" -> getAllCategories();
            default -> throw new IllegalStateException("Action " + action + " not known in this context");
        };
    }

    private OperationReturnObject createCategory(JSONObject request) {
        String categoryName = (String) request.get("name");
        String description = (String) request.get("description");
        CategoryModel category = CategoryModel.builder()
                .name(categoryName)
                .description(description)
                .build();
        CategoryModel savedCategory = categoryRepository.save(category);
        res.setCodeAndMessageAndReturnObject(0, "category created successfully", savedCategory);
        return res;
    }

    private OperationReturnObject getAllCategories() {
        OperationReturnObject res = new OperationReturnObject();
        res.setCodeAndMessageAndReturnObject(0, "All categories", categoryRepository.findAll());
        return res;
    }
}
