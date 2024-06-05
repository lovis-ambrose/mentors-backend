package com.servicecops.mentors.services.Skill;

import com.alibaba.fastjson.JSONObject;
import com.servicecops.mentors.models.database.SkillModel;
import com.servicecops.mentors.repositories.SkillRepository;
import com.servicecops.mentors.services.base.BaseWebActionsService;
import com.servicecops.mentors.utils.OperationReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillService extends BaseWebActionsService {
    @Autowired
    private SkillRepository skillRepository;
    private final OperationReturnObject res = new OperationReturnObject();
    @Override
    public OperationReturnObject switchActions(String action, JSONObject request) {
        return switch(action) {
            case "createSkill" -> createSkill(request);
            case "allSkills" -> getAllSkills();
            default -> throw new IllegalStateException("Action " + action + " not found in this context");
        };
    }

    private OperationReturnObject createSkill(JSONObject request) {
        String skillName = (String) request.get("name");
        String skillDescription = (String) request.get("description");
        SkillModel skill = SkillModel.builder()
                .name(skillName)
                .description(skillDescription)
                .build();
        SkillModel savedSkill = skillRepository.save(skill);
        res.setCodeAndMessageAndReturnObject(0, "skill saved successfully", savedSkill);
        return res;
    }

    private OperationReturnObject getAllSkills() {
        OperationReturnObject res = new OperationReturnObject();
        res.setCodeAndMessageAndReturnObject(0, "all skills", skillRepository.findAll());
        return res;
    }
}
