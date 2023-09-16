package app.navigational.RoutingReportSystem.Services;

import app.navigational.RoutingReportSystem.DTOs.UserDTO;
import app.navigational.RoutingReportSystem.Entities.Role;
import app.navigational.RoutingReportSystem.Entities.User;
import app.navigational.RoutingReportSystem.Exceptions.NotFoundException;
import app.navigational.RoutingReportSystem.Mappers.UserMapper;
import app.navigational.RoutingReportSystem.Repositories.RoleRepository;
import app.navigational.RoutingReportSystem.Repositories.UserRepository;
import app.navigational.RoutingReportSystem.Utilities.RoleType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;

    @Transactional(rollbackOn = {Exception.class})
    public void userSignup(@NonNull UserDTO userDTO) {
        User user = userMapper.fromDTO(userDTO);
        Role userRole = new Role(RoleType.USER);
        userRole.setUser(user);
        userRepository.save(user);
        roleRepository.save(userRole);
    }

    @Transactional(rollbackOn = {Exception.class})
    public void userUpdateProfile(@NonNull Integer id, @NonNull UserDTO userDTO) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        User userToBeUpdated = foundUser.get();
        User user = userMapper.fromDTO(userDTO);
        userToBeUpdated.setPassword(user.getPassword());
        userToBeUpdated.setPassword(user.getUsername());
        userRepository.save(userToBeUpdated);
    }

    @Transactional(rollbackOn = {Exception.class})
    public void deleteAccount(@NonNull Integer id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("No User was found to be deleted");
        }
        userRepository.deleteById(id);
    }

    public UserDTO getUserById(@NonNull Integer id) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return userMapper.toDTO(foundUser.get());
    }

    public void promoteToOperator(@NonNull Integer id) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        User user = foundUser.get();
        user.promoteToOperator();
        userRepository.save(user);
    }
}
