package user.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

// import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long  id;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "user_name", nullable = false, unique = true)
    String userName;

    @Column(name = "password")
    String password;

    @Column(name = "active", nullable = false)
    @Builder.Default
    Boolean active = true;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonManagedReference
    Set<Role> roles;

}
