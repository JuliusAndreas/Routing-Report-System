package app.navigational.RoutingReportSystem.Cache;

import app.navigational.RoutingReportSystem.Configurations.RedisConfig;
import app.navigational.RoutingReportSystem.DTOs.UserDTO;
import app.navigational.RoutingReportSystem.Entities.User;
import app.navigational.RoutingReportSystem.Mappers.UserMapper;
import app.navigational.RoutingReportSystem.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Setter
@Getter
@RequiredArgsConstructor
public class UserCacheManager {

    @NonNull
    private final RedissonClient client;

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final UserMapper userMapper;

    private RList<UserDTO> users;

    @PostConstruct
    private void postConstruct() {
        users = client.getList(RedisConfig.GLOBAL_USERS_CACHE_NAME,
                new TypedJsonJacksonCodec(UserDTO.class));
        users.clear();
        users.addAll(userMapper.
                toDTOWithRolesLoaded(userRepository.findAllUsersJoinFetchRoles()));
    }

    public List<UserDTO> getUsersCached() {
        return users;
    }

    public UserDTO getUserByIdCached(Integer id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                return users.get(i);
            }
        }
        return null;
    }

    public UserDTO getUserByUsernameCached(String username) {
        for (UserDTO userDTO : users) {
            if (userDTO.getUsername().equals(username)) {
                return userDTO;
            }
        }
        return null;
    }

    @Transactional(rollbackOn = {Exception.class})
    public void addUserCached(User user) {
        users.add(userMapper.toDTOWithRolesLoaded(user));
    }

    @Transactional(rollbackOn = {Exception.class})
    public void addUserCached(UserDTO userDTO) {
        users.add(userDTO);
    }

    @Transactional(rollbackOn = {Exception.class})
    public Boolean updateUserCached(Integer id, User user) {
        UserDTO userDTO = userMapper.toDTOWithRolesLoaded(user);
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                users.set(i, userDTO);
                return true;
            }
        }
        return false;
    }

    @Transactional(rollbackOn = {Exception.class})
    public Boolean updateUserCached(Integer id, UserDTO userDTO) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                userDTO.setId(users.get(i).getId());
                users.set(i, userDTO);
                return true;
            }
        }
        return false;
    }

    @Transactional(rollbackOn = {Exception.class})
    public Boolean deleteUserCached(Integer id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                users.remove(i);
                return true;
            }
        }
        return false;
    }
}
