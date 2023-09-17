package app.navigational.RoutingReportSystem.Controllers;

import app.navigational.RoutingReportSystem.DTOs.UserDTO;
import app.navigational.RoutingReportSystem.Exceptions.NotFoundException;
import app.navigational.RoutingReportSystem.Services.UserService;
import app.navigational.RoutingReportSystem.Utilities.OkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{userId}")
    public UserDTO getUserProfile(@PathVariable Integer userId) {
        UserDTO fetchedUser = userService.getUserById(userId);
        if (fetchedUser == null) {
            throw new NotFoundException("No User was found");
        }
        return fetchedUser;
    }
}
