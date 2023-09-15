package app.navigational.RoutingReportSystem.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NamedEntityGraph(
        name = "role-graph",
        attributeNodes = {
                @NamedAttributeNode("id"),
                @NamedAttributeNode("roleName"),
                @NamedAttributeNode(value = "user", subgraph = "user-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "user-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("id"),
                                @NamedAttributeNode("username"),
                                @NamedAttributeNode("password")
                        }
                )
        }
)
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "rolename")
    private String roleName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public Role(String roleName) {
        this.roleName = roleName;
    }
}
