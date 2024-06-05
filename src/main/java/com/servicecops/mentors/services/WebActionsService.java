package com.servicecops.mentors.services;

import com.alibaba.fastjson.JSONObject;
import com.servicecops.mentors.services.Category.CategoryService;
import com.servicecops.mentors.services.Skill.SkillService;
import com.servicecops.mentors.services.auth.AuthService;
import com.servicecops.mentors.services.mentees.MenteesService;
import com.servicecops.mentors.services.mentors.MentorsService;
import com.servicecops.mentors.services.role.RoleService;
import com.servicecops.mentors.utils.OperationReturnObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebActionsService {

    private final AuthService authService;
    private final MentorsService mentorsService;
    private final RoleService roleService;
    private final MenteesService menteesService;
    private final CategoryService categoryService;
    private final SkillService skillService;
    public OperationReturnObject processAction(String service, String action, JSONObject payload) {
        return switch (service) {
            case "Auth" -> authService.process(action, payload);
            case "Mentors" -> mentorsService.process(action, payload);
            case "Mentees" -> menteesService.process(action, payload);
            case "Role" -> roleService.process(action, payload);
            case "Category" -> categoryService.process(action, payload);
            case "Skill" -> skillService.process(action, payload);
            default -> {
                OperationReturnObject res = new OperationReturnObject();
                res.setReturnCodeAndReturnMessage(404, "UNKNOWN SERVICE");
                yield res;
            }
        };
    }
}
