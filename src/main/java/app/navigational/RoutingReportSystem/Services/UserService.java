package app.navigational.RoutingReportSystem.Services;

import app.navigational.RoutingReportSystem.Cache.UserCacheManager;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;
    private UserCacheManager userCacheManager;
    private PasswordEncoder bcryptEncoder;

    @Transactional(rollbackOn = {Exception.class})
    public void userSignup(@NonNull UserDTO userDTO) {
        User user = userMapper.fromDTO(userDTO);
        user.setPassword(bcryptEncoder.encode(userDTO.getPassword()));
        Role userRole = new Role(RoleType.USER);
        userRole.setUser(user);
        user.addRole(userRole);
        userRepository.save(user);
        userCacheManager.addUserCached(user);
    }

    @Transactional(rollbackOn = {Exception.class})
    public void userUpdateProfile(@NonNull Integer id, @NonNull UserDTO userDTO) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        User userToBeUpdated = foundUser.get();
        userToBeUpdated.setPassword(bcryptEncoder.encode(userDTO.getPassword()));
        userToBeUpdated.setUsername(userDTO.getUsername());
        userRepository.save(userToBeUpdated);
        userCacheManager.updateUserCached(id, userToBeUpdated);
    }

    @Transactional(rollbackOn = {Exception.class})
    public void deleteAccount(@NonNull Integer id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("No User was found to be deleted");
        }
        userRepository.deleteById(id);
        userCacheManager.deleteUserCached(id);
    }

    public UserDTO getUserById(@NonNull Integer id) {
        UserDTO userDTO = userCacheManager.getUserByIdCached(id);
        if (userDTO != null) {
            return userDTO;
        }
        User foundUser = userRepository.findByIdJoinFetchRoles(id);
        if (foundUser == null) {
            throw new NotFoundException("User not found");
        }
        userDTO = userMapper.toDTOWithRolesLoaded(foundUser);
        return userDTO;
    }

    public UserDTO getUserEntityByUsername(@NonNull String username) {
        UserDTO userDTO = userCacheManager.getUserByUsernameCached(username);
        if (userDTO != null) {
            return userDTO;
        }
        User foundUser = userRepository.findByUsernameJoinFetch(username);
        if (foundUser == null) {
            throw new NotFoundException("User not found");
        }
        userDTO = userMapper.toDTOWithRolesLoaded(foundUser);
        return userDTO;
    }


    public void promoteToOperator(@NonNull Integer id) {
        User user = userRepository.findByIdJoinFetchRoles(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        user.promoteToOperator();
        userRepository.save(user);
        userCacheManager.updateUserCached(id, user);
    }

    public List<UserDTO> getAllUsers() {
        List<UserDTO> responseList = userCacheManager.getUsersCached();
        if (!responseList.isEmpty()) {
            return responseList;
        }
        responseList = userMapper.toDTOWithRolesLoaded(userRepository.findAllUsersJoinFetchRoles());
        if (responseList.isEmpty()) {
            throw new NotFoundException("No User was found");
        }
        return responseList;
    }

    public RoleType setToRoleType(Set<Role> roles) {
        if (roles.contains(new Role(RoleType.ADMIN))) {
            return RoleType.ADMIN;
        } else if (roles.contains(new Role(RoleType.OPERATOR))) {
            return RoleType.OPERATOR;
        } else {
            return RoleType.USER;
        }
    }
}
