package com.servicecops.mentors.utils;

import com.servicecops.mentors.models.database.*;
import com.servicecops.mentors.models.jpahelpers.enums.AppDomains;
import com.servicecops.mentors.permissions.Permission;
import com.servicecops.mentors.permissions.Permisions;
import com.servicecops.mentors.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.util.Optional;

/**
 * This class runs on every app boot to set up all the defaults likes permissions, domains, etc
 * To add more actions that shall always run on app start, create a method in here and
 * annotate it with @Bean
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SetUp {
    // Turn on or off system domains
    @Value("${USE_DOMAINS:true}")
    Boolean useDomains;

    @Value("${ADMIN_ROLE_NAME}")
    String adminRoleName;

    @Value("${ADMIN_ROLE_DOMAIN}")
    AppDomains adminDomain;

    @Value("${SUPER_ADMIN_FIRST_NAME}")
    String superAdminFirstName;

    @Value("${SUPER_ADMIN_LAST_NAME}")
    String superAdminLastName;

    @Value("${SUPER_ADMIN_EMAIL}")
    String superAdminEmail;

    @Value("${SUPER_ADMIN_PASSWORD}")
    String superAdminPassword;

    @Value("${SUPER_ADMIN_USERNAME}")
    String superAdminUserName;

    private final SystemDomainRepository domainRepository;
    private final SystemPermissionRepository permissionRepository;
    private final SystemRolePermissionRepository permissionAssignmentRepository;
    private final SystemRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SystemUserRepository systemUserRepository;

    @Bean
    protected void setupDomains(){
        if (useDomains){
            log.info("Domains supported, setting them up.");
            domainRepository.deleteAll();
            for(AppDomains domain: AppDomains.values()){
                // check if domain exists orElse create it
                log.info("Adding "+domain.name()+" domain");
                var md = SystemDomainModel.builder();
                md.domainName(String.valueOf(domain));
                domainRepository.save(md.build());
            }
            log.debug("Domains setup successfully");
        }else{
            log.info("Domains are currently inactive.");
        }
    }



    @Bean
    public void setupPermissions(){
        permissionRepository.deleteAll();
        Permisions obj = new Permisions();
        ReflectionUtils.doWithFields(obj.getClass(), field -> {
            field.setAccessible(true);
            log.info("Adding "+field.getName()+" permission");
            Permission perm = (Permission) field.get(obj);
            SystemPermissionModel permissionsModel = new SystemPermissionModel();
            permissionsModel.setPermissionCode(perm.getCode());
            permissionsModel.setPermissionName(perm.getName());
            if (useDomains) {
                permissionsModel.setPermissionDomain(perm.getDomain());
            }
            permissionRepository.save(permissionsModel);
            log.info(field.getName()+" permission added successfully");
        });
        log.info("Permissions setup successfully");
        // Create the default admin role if not exists
        Optional<SystemRoleModel> check_if_admin_role_exists = roleRepository.findFirstByRoleCode("CLIENT");
        if (check_if_admin_role_exists.isEmpty()){
            // create the role here
            var adminRole = SystemRoleModel.builder();
            adminRole.roleName("Administrator");

            if (useDomains) {
                if (StringUtils.isBlank(adminRoleName)){
                    adminRoleName = "ADMINISTRATOR";
                }
                adminRole.roleCode(adminRoleName);
                if (adminDomain == null){
                    throw new IllegalStateException("Please define the domain enum String to be used for administrators");
                }else{
                    adminRole.roleDomain(adminDomain);
                }
            }
            roleRepository.save(adminRole.build());
        }
        // perform the assignment of admin
        Optional<SystemRolePermissionAssignmentModel> assignmentModel = permissionAssignmentRepository.findFirstByRoleCodeAndPermissionCode("ADMINISTRATOR", "ADMINISTRATOR");
        if (assignmentModel.isEmpty()){
            var assignment = SystemRolePermissionAssignmentModel.builder();
            assignment.permissionCode("ADMINISTRATOR");
            assignment.roleCode(adminRoleName);
            permissionAssignmentRepository.save(assignment.build());
        }
        // assign the admin all the permissions they are supposed to ship with.
        setUpAdminPerms();
    }

    /**
     * By default, the system creates the first role of ADMINISTRATOR, therefore, this method is to assign it its default permissions.
     * This will assign all the permissions that set 'shipWithAdmin' to true.
     */
    private void setUpAdminPerms(){
        Permisions obj = new Permisions();
        ReflectionUtils.doWithFields(obj.getClass(), field -> {
            field.setAccessible(true);
            Permission perm = (Permission) field.get(obj);
            if (perm.getShipWithAdmin()){
                Optional<SystemRolePermissionAssignmentModel> assignmentModel = permissionAssignmentRepository.findFirstByRoleCodeAndPermissionCode("ADMINISTRATOR", perm.getCode());
                if (assignmentModel.isEmpty()){
                    var assignment = SystemRolePermissionAssignmentModel.builder();
                    assignment.permissionCode(perm.getCode());
                    assignment.roleCode(adminRoleName);
                    permissionAssignmentRepository.save(assignment.build());
                }
            }
        });
    }

    @Bean
    public void setUpSuperAdmin() {
        Optional<SystemRoleModel> role = roleRepository.findFirstByRoleCode("ADMINISTRATOR");
        if (role.isPresent()) {
            log.info("Adding super admin");
            SystemRoleModel actualRole = role.get();
            SystemUserModel superAdmin = SystemUserModel.builder()
                    .firstName(superAdminFirstName)
                    .lastName(superAdminLastName)
                    .email(superAdminEmail)
                    .password(passwordEncoder.encode(superAdminPassword))
                    .username(superAdminUserName)
                    .roleCode(actualRole.getRoleCode())
                    .isActive(true)
                    .build();
            systemUserRepository.save(superAdmin);

        } else {
            throw new IllegalStateException("failed to create super admin");
        }

    }
}
