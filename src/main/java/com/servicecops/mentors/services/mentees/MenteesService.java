package com.servicecops.mentors.services.mentees;

import com.alibaba.fastjson.JSONObject;
import com.servicecops.mentors.models.database.*;
import com.servicecops.mentors.repositories.*;
import com.servicecops.mentors.services.base.BaseWebActionsService;
import com.servicecops.mentors.utils.OperationReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
@Service
public class MenteesService extends BaseWebActionsService {
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private SystemRoleRepository systemRoleRepository;
    @Autowired
    private MenteeRepository menteeRepository;
    @Autowired
    private MentorRepository mentorRepository;
    @Autowired
    private SessionRepository sessionRepository;

    private final OperationReturnObject res = new OperationReturnObject();
    @Override
    public OperationReturnObject switchActions(String action, JSONObject request) {
        return switch (action){
            case "makeUserMentee" -> makeUserMentee(request);
            case "mentees" -> getAllMentees(request);
            case "createSession" -> createSession(request);
            default -> throw new IllegalArgumentException("Action " + action + " not known in this context");
        };
    }

    private OperationReturnObject getAllMentees(JSONObject request) {
        List<MenteeModel> mentees = menteeRepository.findAll();
        if (mentees.isEmpty()) {
            res.setCodeAndMessageAndReturnObject(0, "no mentees found", null);
        } else {
            res.setCodeAndMessageAndReturnObject(0, "mentees fetched successfully", mentees);
        }
        return res;
    }

    private OperationReturnObject makeUserMentee(JSONObject request) {
        int userIdInt = (int) request.get("userId");
        Long userId = (long) userIdInt;
        String occupation = (String) request.get("occupation");
        Optional<SystemUserModel> user = systemUserRepository.findById(userId);
        if (user.isPresent()) {
            SystemUserModel actualUser = user.get();
            Optional<SystemRoleModel> role = systemRoleRepository.findFirstByRoleCode("MENTOR");
            if (role.isPresent()) {
                SystemRoleModel actualRole = role.get();
                actualUser.setRoleCode(actualRole.getRoleCode());
                MenteeModel mentee = MenteeModel.builder()
                        .firstName(actualUser.getFirstName())
                        .lastName(actualUser.getLastName())
                        .email(actualUser.getEmail())
                        .occupation(occupation)
                        .build();
                MenteeModel savedMentor = menteeRepository.save(mentee);
                res.setCodeAndMessageAndReturnObject(0, "mentee successfully created", savedMentor);
                return res;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No mentee role found");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with provided id");
        }
    }

    private OperationReturnObject createSession(JSONObject request) {
//        TODO check if user is authenticated
        int mentorIdInt = (int) request.get("mentorId");
        int menteeIdInt = (int) request.get("mentorId");
        Long mentorId = (long) mentorIdInt;
        Long menteeId = (long) menteeIdInt;
        String question = (String) request.get("question");
        Optional<MentorModel> mentor = mentorRepository.findById(mentorId);
        if (mentor.isPresent()) {
            MentorModel actualMentor = mentor.get();
            SessionModel session = SessionModel.builder()
                    .mentorId(actualMentor.getId())
                    .menteeId(menteeId)
                    .question(question)
                    .build();
            SessionModel savedSession = sessionRepository.save(session);
            res.setCodeAndMessageAndReturnObject(0, "session created successfully", savedSession);
            return res;

        } else {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No mentor found with the provided id");
        }

    }

}
