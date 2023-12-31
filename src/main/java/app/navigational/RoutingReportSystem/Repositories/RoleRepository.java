package app.navigational.RoutingReportSystem.Repositories;

import app.navigational.RoutingReportSystem.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("select r from Role r where r.user.id = :queryId")
    Set<Role> findRolesByUserId(@Param("queryId") Integer id);

}
