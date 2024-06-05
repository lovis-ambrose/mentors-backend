package com.servicecops.mentors.services.role;

import com.alibaba.fastjson.JSONObject;
import com.servicecops.mentors.models.database.SystemRoleModel;
import com.servicecops.mentors.models.database.SystemRolePermissionAssignmentModel;
import com.servicecops.mentors.models.database.SystemUserModel;
import com.servicecops.mentors.models.jpahelpers.enums.AppDomains;
import com.servicecops.mentors.repositories.SystemPermissionRepository;
import com.servicecops.mentors.repositories.SystemRolePermissionRepository;
import com.servicecops.mentors.repositories.SystemRoleRepository;
import com.servicecops.mentors.repositories.SystemUserRepository;
import com.servicecops.mentors.services.base.BaseWebActionsService;
import com.servicecops.mentors.utils.OperationReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseWebActionsService {
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private SystemRoleRepository systemRoleRepository;
    @Autowired
    private SystemRolePermissionRepository systemRolePermissionRepository;
    private final OperationReturnObject res = new OperationReturnObject();

    @Override
    public OperationReturnObject switchActions(String action, JSONObject request) {
        return switch(action) {
            case "addRole" -> addRole(request);
            default -> throw new IllegalStateException("Action " + action + " not found in this context");
        };
    }

    public OperationReturnObject addRole(JSONObject request) {
        String username = (String) request.get("username");
        String roleName = (String) request.get("roleName");
        SystemUserModel admin = systemUserRepository.findFirstByUsername(username);
        if (admin != null) {
            SystemRoleModel role = SystemRoleModel.builder()
                    .roleName(roleName)
                    .roleCode(roleName.toUpperCase())
                    .roleDomain(AppDomains.CLIENT_SIDE)
                    .build();
            SystemRoleModel savedRole = systemRoleRepository.save(role);
            SystemRolePermissionAssignmentModel systemRolePermissionAssignmentModel=  SystemRolePermissionAssignmentModel.builder()
                    .roleCode(savedRole.getRoleCode())
                    .permissionCode(roleName.toUpperCase())
                    .build();
            systemRolePermissionRepository.save(systemRolePermissionAssignmentModel);
            res.setCodeAndMessageAndReturnObject(0, "role saved successfully", savedRole);
            return res;
        } else {
            res.setCodeAndMessageAndReturnObject(400, "failed to save role", null);
            return res;
        }

    }
}
