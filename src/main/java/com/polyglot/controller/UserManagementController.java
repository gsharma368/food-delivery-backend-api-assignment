package com.polyglot.controller;

import com.polyglot.dto.DishReqRes;
import com.polyglot.dto.ReqRes;
import com.polyglot.dto.RestaurantReqRes;
import com.polyglot.entity.Dish;
import com.polyglot.entity.Restaurant;
import com.polyglot.entity.OurUsers;
import com.polyglot.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserManagementController {
    @Autowired
    private UsersManagementService usersManagementService;


    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> regeister(@RequestBody ReqRes reg){
        return ResponseEntity.ok(usersManagementService.register(reg));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.login(req));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.refreshToken(req));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<ReqRes> getAllUsers(){
        return ResponseEntity.ok(usersManagementService.getAllUsers());

    }

    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<ReqRes> getUSerByID(@PathVariable Integer userId){
        return ResponseEntity.ok(usersManagementService.getUsersById(userId));

    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable Integer userId, @RequestBody OurUsers reqres){
        return ResponseEntity.ok(usersManagementService.updateUser(userId, reqres));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = usersManagementService.getMyInfo(email);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<ReqRes> deleteUSer(@PathVariable Integer userId){
        return ResponseEntity.ok(usersManagementService.deleteUser(userId));
    }

    @GetMapping("/get-all-restaurants")
    public ResponseEntity<List<Restaurant>> getAllCourses(){
        return ResponseEntity.ok(usersManagementService.getAllRestaurants());
    }

//    @GetMapping("/get-my-courses")
//    public ResponseEntity<List<Restaurant>> getMyCourses(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        return ResponseEntity.ok(usersManagementService.getMyCourses(email));
//    }
//
//    @GetMapping("/get-my-progress")
//    public ResponseEntity<List<ProgessReqRes>> getMyProgressStats(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        return ResponseEntity.ok(usersManagementService.getMyProgress(email));
//    }

    @GetMapping("/get-restaurant/{restaurantId}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Integer restaurantId){
        return ResponseEntity.ok(usersManagementService.getRestaurantById(restaurantId));
    }

    @PostMapping("/restaurant")
    public ResponseEntity<RestaurantReqRes> addMasterCourse(@RequestBody RestaurantReqRes restaurantReqRes){
        return ResponseEntity.ok(usersManagementService.addMasterRestaurant(restaurantReqRes));
    }

//    @PostMapping("/course/{courseId}/enroll")
//    public ResponseEntity<ReqRes> enrollUserInThisCourse(@PathVariable Integer courseId){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        return ResponseEntity.ok(usersManagementService.enrollUserInCourse(email, courseId));
//    }
//
//    @PutMapping("/course/{courseId}/unenroll")
//    public ResponseEntity<ReqRes> unenrollUserInThisCourse(@PathVariable Integer courseId){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        return ResponseEntity.ok(usersManagementService.unenrollUserInCourse(email, courseId));
//    }
//
//    @PutMapping("/unenrollFromAllCourses")
//    public ResponseEntity<ReqRes> unenrollUserInAllCourse(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        return ResponseEntity.ok(usersManagementService.unenrollUserFromAllCourses(email));
//    }

    @PostMapping("/add-dish")
    public ResponseEntity<DishReqRes> addQuestionsToCourse(@RequestBody DishReqRes dishReqRes){
        return ResponseEntity.ok(usersManagementService.addDishToTheRestaurant(dishReqRes));
    }

//    @PutMapping("/edit-question/{questionId}")
//    public ResponseEntity<QuesAnsReqRes> editQuestionOfACourse(@PathVariable Integer questionId, @RequestBody QuesAnsReqRes quesAnsReqRes){
//        return ResponseEntity.ok(usersManagementService.editQuestionOfTheCourse(questionId, quesAnsReqRes));
//    }

    @GetMapping("/course/{restaurantId}/get-menu")
    public ResponseEntity<List<Dish>> getQuestionsOfTheCourseId(@PathVariable Integer restaurantId){
        return ResponseEntity.ok(usersManagementService.getMenuOfTheRestaurantId(restaurantId));
    }

//    @PostMapping("/attempt-question")
//    public ResponseEntity<QuesAnsReqRes> attemptQuestion(@RequestBody QuesAnsReqRes quesAnsReqRes){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        return ResponseEntity.ok(usersManagementService.attemptQuestion(quesAnsReqRes, email));
//    }
//
//    @GetMapping("/get-unattempted-question-for-course/{courseId}")
//    public ResponseEntity<List<Dish>> getUnAttemptedQuestionForCourse(@PathVariable Integer courseId){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        return ResponseEntity.ok(usersManagementService.getUnAttemptedQuestionForCourse(courseId, email));
//    }


}
