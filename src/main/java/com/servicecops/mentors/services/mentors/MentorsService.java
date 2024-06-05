package com.servicecops.mentors.services.mentors;

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
public class MentorsService extends BaseWebActionsService {
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private MentorRepository mentorRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private SystemRoleRepository systemRoleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SkillRepository skillRepository;

    private final OperationReturnObject res = new OperationReturnObject();
    @Override
    public OperationReturnObject switchActions(String action, JSONObject request) {
        return switch (action) {
            case "makeUserMentor" -> makeUserMentor(request);
            case "myMentors" -> getAllMentors(request);
            case "mentor" -> getSingleMentor(request);
            case "respondToSession" -> answerSessionQuestion(request);
            default -> throw new IllegalArgumentException("Action " + action + " not known in this context");
        };
    }

    private OperationReturnObject answerSessionQuestion(JSONObject request) {
        int sessionIdInt = (int)request.get("sessionId");
        Long sessionId = (long) sessionIdInt;
        int mentorIdInt = (int)request.get("mentorId");
        Long mentorId = (long) mentorIdInt;
        String answer = (String) request.get("answer");
        Optional<SessionModel> session = sessionRepository.findById(sessionId);
        if (session.isPresent()) {
            SessionModel actualSession = session.get();
            if (actualSession.getMentorId().equals(mentorId)) {
                actualSession.setAnswer(answer);
                SessionModel savedSession = sessionRepository.save(actualSession);
                res.setCodeAndMessageAndReturnObject(0, "session question answered", savedSession);
            } else {
                throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "This session does not belong to mentor with provided id");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No session found");
        }
       return res;
    }

    private OperationReturnObject getSingleMentor(JSONObject request) {
        int mentorIdInt = (int)request.get("mentorId");
        Long mentorId = (long) mentorIdInt;
        Optional<MentorModel> mentor = mentorRepository.findById(mentorId);
        if (mentor.isPresent()) {
            MentorModel actualMentor = mentor.get();
            res.setCodeAndMessageAndReturnObject(0, "mentor retreived successfully", actualMentor);
        } else {
            res.setCodeAndMessageAndReturnObject(400, "No mentor found", null);
        }
        return res;
    }

    private OperationReturnObject getAllMentors(JSONObject request) {
        List<MentorModel> mentors = mentorRepository.findAll();
        if (mentors.isEmpty()) {
            res.setCodeAndMessageAndReturnObject(0, "no mentors found", null);
        } else {
            res.setCodeAndMessageAndReturnObject(0, "mentors fetched successfully", mentors);
        }
        return res;
    }



    private OperationReturnObject makeUserMentor(JSONObject request) {
//        requiresAuth();
        int userIdInt = (int) request.get("userId");
        Long userId = (long) userIdInt;
        String categoryName = (String) request.get("categoryName");
        String skillName = (String) request.get("skillName");
        Optional<SystemUserModel> user = systemUserRepository.findById(userId);
        if (user.isPresent()) {
            SystemUserModel actualUser = user.get();
           Optional<SystemRoleModel> role = systemRoleRepository.findFirstByRoleCode("MENTOR");
           if (role.isPresent()) {
               SystemRoleModel actualRole = role.get();
               actualUser.setRoleCode(actualRole.getRoleCode());
               Optional<CategoryModel> category =  categoryRepository.findByName(categoryName);
               if (category.isPresent()) {
                   CategoryModel actualCategory = category.get();
                   Optional<SkillModel> skill = skillRepository.findByName(skillName);
                   if (skill.isPresent()) {
                       SkillModel actualSkill = skill.get();
                       MentorModel mentor = MentorModel.builder()
                               .firstName(actualUser.getFirstName())
                               .lastName(actualUser.getLastName())
                               .email(actualUser.getEmail())
                               .categoryId(actualCategory.getId())
                               .skillId(actualSkill.getId())
                               .build();
                       MentorModel savedMentor = mentorRepository.save(mentor);
                       res.setCodeAndMessageAndReturnObject(0, "mentor successfully created", savedMentor);
                   } else {
                       throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No skill found with the provided name");
                   }

               } else {
                   throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No category found with provided name");
               }

               return res;
           } else {
               throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No mentor role found");
           }

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with provided id");
        }
    }

}
