package app.navigational.RoutingReportSystem.Entities;

import app.navigational.RoutingReportSystem.Utilities.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NamedEntityGraph(
        name = "user-graph",
        attributeNodes = {
                @NamedAttributeNode("id"),
                @NamedAttributeNode("username"),
                @NamedAttributeNode("password")
        }
)
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @ToString.Exclude
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Role> roles;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Report> reports;

    public User(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        for (Role role : roles) {
            role.setUser(this);
        }
        this.roles = roles;
    }

    public void addRole(Role role) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
    }

    public void promoteToOperator() {
        this.roles.add(new Role(RoleType.OPERATOR));
    }
}
