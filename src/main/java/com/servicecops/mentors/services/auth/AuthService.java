package com.servicecops.mentors.services.auth;

import com.alibaba.fastjson.JSONObject;
import com.servicecops.mentors.config.ApplicationConf;
import com.servicecops.mentors.config.JwtUtility;
import com.servicecops.mentors.models.database.SystemRoleModel;
import com.servicecops.mentors.models.database.SystemUserModel;
import com.servicecops.mentors.repositories.SystemRoleRepository;
import com.servicecops.mentors.repositories.SystemUserRepository;
import com.servicecops.mentors.services.base.BaseWebActionsService;
import com.servicecops.mentors.utils.OperationReturnObject;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class AuthService extends BaseWebActionsService {
    private final AuthenticationManager authenticationManager;
    private final ApplicationConf userDetailService;
    private final JwtUtility jwtUtility;
    private final PasswordEncoder passwordEncoder;
    private final SystemRoleRepository systemRoleRepository;
    private final SystemUserRepository systemUserRepository;

    private final OperationReturnObject res = new OperationReturnObject();

    private OperationReturnObject login(JSONObject request){

        List<String> requiredFields = new ArrayList<>();
        requiredFields.add("username");
        requiredFields.add("password");
        requires(requiredFields, request);

        String username= request.getString("username");
        String password= request.getString("password");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        final SystemUserModel userDetails = userDetailService.loadUserByUsername(username);
        final String token = jwtUtility.generateToken(userDetails);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token); // this is the jwt token the user can user from now on.
        response.put("user", userDetails);
        res.setCodeAndMessageAndReturnObject(0, "Welcome back " + userDetails.getUsername(), response);

        return res;
    }

    private OperationReturnObject signUp(JSONObject request) {
        System.out.println("am here");
        String firstName = (String) request.get("firstName");
        String lastName = (String) request.get("lastName");
        String userName= (String) request.get("userName");
        String email = (String) request.get("email");
        String password = (String) request.get("password");
        String hashedPassword = passwordEncoder.encode(password);
        System.out.println("below here");
        Optional<SystemRoleModel> role = systemRoleRepository.findFirstByRoleCode("CLIENT");
        if (role.isPresent()) {
            SystemRoleModel actualRole = role.get();
            SystemUserModel user = SystemUserModel.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .username(userName)
                    .email(email)
                    .password(hashedPassword)
                    .roleCode(actualRole.getRoleCode())
                    .isActive(true)
                    .authorities(new ArrayList<>())
                    .build();
            SystemUserModel savedUser = systemUserRepository.save(user);
            System.out.println(savedUser);
            String token = jwtUtility.generateToken(savedUser);
            res.setCodeAndMessageAndReturnObject(0, "user created successfully", token);
        } else {
            res.setCodeAndMessageAndReturnObject(0, "something went wrong", null);
        }
        return res;
    }

    private OperationReturnObject deleteUser(JSONObject request) {
        Long userId = request.getLong("userId");
        systemUserRepository.deleteById(userId);
        res.setCodeAndMessageAndReturnObject(0, "user deleted successfully", null);
        return res;
    }


    @Override
    public OperationReturnObject switchActions(String action, JSONObject request) {
        return switch (action){
            case "login" -> login(request);
            case "signup" -> signUp(request);
            case "deleteUser" -> deleteUser(request);
            default -> throw new IllegalArgumentException("Action " + action + " not known in this context");
        };
    }


}
