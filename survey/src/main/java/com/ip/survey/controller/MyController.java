package com.ip.survey.controller;

import com.ip.survey.classes.Question;
import com.ip.survey.classes.Survey;
import com.ip.survey.classes.User;
import com.ip.survey.configuration.MyUserDetails;
import com.ip.survey.database.UserRepository;
import com.ip.survey.dto.UserRegisterDto;
import com.ip.survey.image.ImageService;
import com.ip.survey.myservice.MyService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.security.PermitAll;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Controller
public class MyController {

    @Autowired
    private MyService myService;

    @Autowired
    private HttpServletRequest request;

    private User user;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(){
        return "home";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerGet(@ModelAttribute("registerstatus") String registerStatus, Model model,
                              WebRequest request){
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        if(!registerStatus.isEmpty()){
            model.addAttribute("errorMessage", registerStatus);
        }
        model.addAttribute("user", userRegisterDto);
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public RedirectView RegisterPost(@ModelAttribute("user") @Validated UserRegisterDto userRegisterDto,
                                    HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String result = myService.registerUser(userRegisterDto);
        if(result.equals("success")){
            try {
                request.login(userRegisterDto.getUsername(), userRegisterDto.getPassword());
            } catch (ServletException e) {
                redirectAttributes.addAttribute(
                        "Automatic logging resulted in an error! Please log in manually!"
                        , result);
                return new RedirectView("register");
            }
            return new RedirectView("user");
        }
        else {
            redirectAttributes.addAttribute("message", result);
            return new RedirectView("register");
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGet(){
        return "login";
    }

    /*
    By Spring Security /login POST
                       /logout GET
     */

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String user(@AuthenticationPrincipal MyUserDetails user, Model model){
        if(user!=null) {
            this.user = myService.setupUser(user.getUsername());
        }
        model.addAttribute("user", this.user);
        return "userhome";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createSurvey(){
        if(user == null){
            return "redirect:user";
        }
        return "create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Object> createSurveyPost(@RequestBody String jsonString){
        JSONObject jsonObject = new JSONObject(jsonString);
        HashMap<String, String> response = myService.createSurvey(jsonObject, this.user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Request made when creating a survey.
    //Saves image in a folder and returns success.
    @RequestMapping(value = "/uploadimage", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Object> uploadImage(@RequestParam("image") MultipartFile multipartFile){
        //Uploads and image
        ImageService imageService = new ImageService();
        return imageService.saveImage(multipartFile);
    }

    @RequestMapping(value = "/survey/vote{surveyUrl}", method = RequestMethod.GET)
    public String getSurvey(@RequestParam(value = "surveyUrl") String surveyUrl, Model model){
        Survey survey = myService.getSurveyByUrl(surveyUrl);
        model.addAttribute("survey", survey);
        return "survey";
    }

    @RequestMapping(value = "/postresults", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Object> postSurveyResults(@RequestBody String jsonString){
        JSONObject jsonObject = new JSONObject(jsonString);
        myService.appendVotes(jsonObject);
        HashMap<String, String> responseMap = new HashMap<>();
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @RequestMapping(value = "/survey/results{resultsUrl}", method = RequestMethod.GET)
    public String getResults(@RequestParam(value = "resultsUrl") String resultsUrl, Model model){
        model.addAttribute("survey", myService.getSurveyByResultsUrl(resultsUrl));
        return "results";
    }

    @RequestMapping(value = "disablesurvey", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Object> disableSurvey(@RequestBody String jsonString){
        JSONObject jsonObject = new JSONObject(jsonString);
        HttpStatus httpStatus = myService.switchSurveyOpenById(jsonObject.getInt("surveyId"), false);
        HashMap<String, String> responseMap = new HashMap<>();
        responseMap.put("type", "disable");
        responseMap.put("surveyId", jsonObject.getString("surveyId"));
        return new ResponseEntity<>(responseMap, httpStatus);
    }

    @RequestMapping(value = "enablesurvey", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Object> enableSurvey(@RequestBody String jsonString){
        JSONObject jsonObject = new JSONObject(jsonString);
        HttpStatus httpStatus = myService.switchSurveyOpenById(jsonObject.getInt("surveyId"), true);
        HashMap<String, String> responseMap = new HashMap<>();
        responseMap.put("type", "enable");
        responseMap.put("surveyId", jsonObject.getString("surveyId"));
        return new ResponseEntity<>(responseMap, httpStatus);
    }

}

//<img th:src="${object.imageFileName}"/>
// <img th:src="'/images/' + ${object.imageFileName}"/>
//http://localhost:8080/survey/vote?surveyUrl=Jl75sdCduYddBnESZ9eHBVoBpsXCg5Jk
//http://localhost:8080/survey/results?resultsUrl=TyzccVON7dW2rxn51S1xzvhWGf7xlPVB

//<form th:action="${'survey/vote?surveyUrl=' + survey.surveyUrl}" method="get">