package app.navigational.RoutingReportSystem.Controllers;

import app.navigational.RoutingReportSystem.DTOs.UserDTO;
import app.navigational.RoutingReportSystem.Exceptions.NotFoundException;
import app.navigational.RoutingReportSystem.Services.UserService;
import app.navigational.RoutingReportSystem.Utilities.OkResponse;
import app.navigational.RoutingReportSystem.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity addUser(@RequestBody UserDTO userDTO) {
        userService.userSignup(userDTO);
        return new ResponseEntity<>(new OkResponse("User successfully added"), HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity updateUserProfile(@RequestBody UserDTO userDTO,
                                            @PathVariable Integer userId) {
        userService.userUpdateProfile(userId, userDTO);
        return new ResponseEntity<>(new OkResponse("User successfully updated"), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUserProfile(@PathVariable Integer userId) {
        userService.deleteAccount(userId);
        return new ResponseEntity<>(new OkResponse("User successfully removed"), HttpStatus.OK);
    }

    @JsonView(Views.Public.class)
    @GetMapping("/{userId}")
    public UserDTO getUserProfile(@PathVariable Integer userId) {
        UserDTO fetchedUser = userService.getUserById(userId);
        if (fetchedUser == null) {
            throw new NotFoundException("No User was found");
        }
        return fetchedUser;
    }

    @JsonView(Views.Public.class)
    @GetMapping("/")
    public List<UserDTO> getUsers() {
        List<UserDTO> fetchedUsers = userService.getAllUsers();
        if (fetchedUsers.isEmpty()) {
            throw new NotFoundException("No User was found");
        }
        return fetchedUsers;
    }

    @PatchMapping("/{userId}/promote")
    public ResponseEntity promoteAUser(@PathVariable Integer userId) {
        userService.promoteToOperator(userId);
        return new ResponseEntity<>(new OkResponse("User successfully promoted"), HttpStatus.OK);
    }
}
