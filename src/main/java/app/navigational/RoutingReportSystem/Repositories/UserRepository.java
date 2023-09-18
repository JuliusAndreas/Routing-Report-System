package app.navigational.RoutingReportSystem.Repositories;

import app.navigational.RoutingReportSystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :queryUsername")
    User findByUsernameJoinFetch(@Param("queryUsername") String username);

    @Query("SELECT u FROM User u JOIN FETCH u.roles")
    List<User> findAllUsersJoinFetchRoles();

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id = :queryId")
    User findByIdJoinFetchRoles(@Param("queryId") Integer queryId);
}
