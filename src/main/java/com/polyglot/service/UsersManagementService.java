package com.polyglot.service;

import com.polyglot.dto.DishReqRes;
import com.polyglot.dto.ReqRes;
import com.polyglot.dto.RestaurantReqRes;
import com.polyglot.entity.Restaurant;
import com.polyglot.entity.OurUsers;
import com.polyglot.entity.Dish;
import com.polyglot.repository.CartRepo;
import com.polyglot.repository.RestaurantRepo;
import com.polyglot.repository.DishRepo;
import com.polyglot.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UsersManagementService {

    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private RestaurantRepo restaurantRepo;
    @Autowired
    private DishRepo dishRepo;
    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private JWTUtilsHelper jwtUtilsHelper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public ReqRes register(ReqRes registrationRequest){
        ReqRes resp = new ReqRes();

        try {
            OurUsers ourUser = new OurUsers();
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setCity(registrationRequest.getCity());
            ourUser.setRole(registrationRequest.getRole());
            ourUser.setName(registrationRequest.getName());
            ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            OurUsers ourUsersResult = usersRepo.save(ourUser);
            if (ourUsersResult.getId()>0) {
                resp.setOurUsers((ourUsersResult));
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }

        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }


    public ReqRes login(ReqRes loginRequest){
        ReqRes response = new ReqRes();
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getPassword()));
            var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtilsHelper.generateJWTToken(user);
            var refreshToken = jwtUtilsHelper.generateJWTRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }





    public ReqRes refreshToken(ReqRes refreshTokenReqiest){
        ReqRes response = new ReqRes();
        try{
            String ourEmail = jwtUtilsHelper.extractUsername(refreshTokenReqiest.getToken());
            OurUsers users = usersRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtilsHelper.isTokenValid(refreshTokenReqiest.getToken(), users)) {
                var jwt = jwtUtilsHelper.generateJWTToken(users);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenReqiest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }


    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();

        try {
            List<OurUsers> result = usersRepo.findAll();
            if (!result.isEmpty()) {
                reqRes.setOurUsersList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    public ReqRes getUsersById(Integer id) {
        ReqRes reqRes = new ReqRes();
        try {
            OurUsers usersById = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setOurUsers(usersById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Users with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes deleteUser(Integer userId) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                usersRepo.deleteById(userId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes updateUser(Integer userId, OurUsers updatedUser) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                OurUsers existingUser = userOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setName(updatedUser.getName());
                existingUser.setCity(updatedUser.getCity());
                existingUser.setRole(updatedUser.getRole());

                // Check if password is present in the request
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    // Encode the password and update it
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                OurUsers savedUser = usersRepo.save(existingUser);
                reqRes.setOurUsers(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes getMyInfo(String email){
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setOurUsers(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }

        }catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;

    }

    public RestaurantReqRes addMasterRestaurant(RestaurantReqRes courseReqRes) {
        RestaurantReqRes resp = new RestaurantReqRes();

        try {
            Restaurant addRestaurant = new Restaurant();
            addRestaurant.setTitle(courseReqRes.getTitle());
            addRestaurant.setContent(courseReqRes.getContent());
            addRestaurant.setDescription(courseReqRes.getDescription());
            Restaurant restaurantResult = restaurantRepo.save(addRestaurant);
            if (restaurantResult.getId()>0) {
                resp.setRestaurant((restaurantResult));
                resp.setMessage("Restaurant Saved Successfully");
                resp.setStatusCode(200);
            }

        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> result = restaurantRepo.findAll();
        return result;
    }

    public Restaurant getRestaurantById(Integer restaurantId) {
        Optional<Restaurant> courseOptional = restaurantRepo.findById(restaurantId);
        return courseOptional.get();
    }

    public DishReqRes addDishToTheRestaurant(DishReqRes dishReqRes) {
        DishReqRes resp = new DishReqRes();

        try {
            Dish addDish = new Dish();
            addDish.setName(dishReqRes.getDishName());
            addDish.setPrice(dishReqRes.getDishPrice());
            addDish.setDescription(dishReqRes.getDishDescription());
            addDish.setDishType(dishReqRes.getDishType());
            addDish.setCuisine(dishReqRes.getDishCuisine());
            addDish.setRestaurantId(dishReqRes.getRestaurantId());

            Dish addedDish = dishRepo.save(addDish);

            // Added the question to the course
            Optional<Restaurant> restaurantOptional = restaurantRepo.findById(dishReqRes.getRestaurantId());
            List<Integer> menuDishesIds = new ArrayList<>(restaurantOptional.get().getMenuDishes());
            menuDishesIds.add(addedDish.getId());

            restaurantOptional.get().setMenuDishes(menuDishesIds);
            Restaurant savedRestaurant = restaurantRepo.save(restaurantOptional.get());

            if (addedDish.getId()>0) {
                resp.setDishName(addedDish.getName());
                resp.setDishPrice(addedDish.getPrice());
                resp.setDishType(addedDish.getDishType());
                resp.setDishDescription(addedDish.getDescription());
                resp.setDishCuisine(addedDish.getCuisine());

                resp.setRestaurantId(addedDish.getRestaurantId());

                resp.setMessage("Dish Added Successfully to the Restaurant Content");
                resp.setStatusCode(200);
            }

        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public List<Dish> getMenuOfTheRestaurantId(Integer restaurantId) {
        Optional<Restaurant> restaurantOptional = restaurantRepo.findById(restaurantId);
        List<Integer> menuOfRestaurant = restaurantOptional.get().getMenuDishes();
        List<Dish> menuDishes = new ArrayList<>();
        for(Integer dishId : menuOfRestaurant){
            Optional dishOptional = dishRepo.findById(dishId);
            if(dishOptional.isPresent()){
                menuDishes.add((Dish) dishOptional.get());
            }
        }
        return menuDishes;
    }

    public DishReqRes editDishOfTheRestaurant(Integer dishId, DishReqRes dishReqRes) {
        DishReqRes resp = new DishReqRes();

        try {

            Optional<Dish> dishOptional = dishRepo.findById(dishId);
            if (!dishOptional.isPresent()) {
                resp.setStatusCode(404);
                resp.setMessage("Dish not found for updation.");
                return resp;
            }

            dishOptional.get().setName(dishReqRes.getDishName());
            dishOptional.get().setPrice(dishReqRes.getDishPrice());
            dishOptional.get().setDescription(dishReqRes.getDishDescription());
            dishOptional.get().setDishType(dishReqRes.getDishType());
            dishOptional.get().setCuisine(dishReqRes.getDishCuisine());
            dishOptional.get().setRestaurantId(dishReqRes.getRestaurantId() );
            Dish editedDish = dishRepo.save(dishOptional.get());

//            Dish addDish = new Dish();
//            addDish.setName(dishReqRes.getDishName());
//            addDish.setPrice(dishReqRes.getDishPrice());
//            addDish.setDescription(dishReqRes.getDishDescription());
//            addDish.setDishType(dishReqRes.getDishType());
//            addDish.setCuisine(dishReqRes.getDishCuisine());
//            addDish.setRestaurantId(dishReqRes.getRestaurantId());
//            Dish editedDish = dishRepo.save(addDish);

            // Added the dish to the restaurant
            Optional<Restaurant> restaurantOptional = restaurantRepo.findById(dishReqRes.getRestaurantId());
            List<Integer> menuDishesIds = new ArrayList<>(restaurantOptional.get().getMenuDishes());
            menuDishesIds.remove(Integer.valueOf(editedDish.getId()));
            menuDishesIds.add(editedDish.getId());

            restaurantOptional.get().setMenuDishes(menuDishesIds);
            Restaurant savedRestaurant = restaurantRepo.save(restaurantOptional.get());

            //dishRepo.deleteById(dishId);

            if (editedDish.getId()>0) {
                resp.setDishName(editedDish.getName());
                resp.setDishPrice(editedDish.getPrice());
                resp.setDishType(editedDish.getDishType());
                resp.setDishDescription(editedDish.getDescription());
                resp.setDishCuisine(editedDish.getCuisine());
                resp.setRestaurantId(editedDish.getRestaurantId());
                resp.setDishId(editedDish.getId());

                resp.setMessage("Dish Edited Successfully to the Restaurant Content");
                resp.setStatusCode(200);
            }


        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public List<Dish> getAllDishes() {
        List<Dish> result = dishRepo.findAll();
        return result;
    }

    public ReqRes deleteDishByDishId(Integer dishId) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<Dish> dishOptional = dishRepo.findById(dishId);
            if (dishOptional.isPresent()) {

                // Delete the dish from the restaurant menu
                Optional<Restaurant> restaurantOptional = restaurantRepo.findById(dishOptional.get().getRestaurantId());
                List<Integer> menuDishesIds = new ArrayList<>(restaurantOptional.get().getMenuDishes());
                menuDishesIds.remove(Integer.valueOf(dishId));

                restaurantOptional.get().setMenuDishes(menuDishesIds);
                Restaurant savedRestaurant = restaurantRepo.save(restaurantOptional.get());

                dishRepo.deleteById(dishId);

                reqRes.setStatusCode(200);
                reqRes.setMessage("Dish deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Dish not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting Dish: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes deleteRestaurantById(Integer restaurantId) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<Restaurant> dishOptional = restaurantRepo.findById(restaurantId);
            if (dishOptional.isPresent()) {
                restaurantRepo.deleteById(restaurantId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Restaurant deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Restaurant not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting Restaurant: " + e.getMessage());
        }
        return reqRes;
    }
}
