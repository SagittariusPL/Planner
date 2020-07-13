package pl.straszewski.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String password;
    @NonNull
    private String role;
    @NonNull
    private String email;
    @NonNull
    private Boolean enabled;

    public UserEntity( String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role ="ROLE_USER";
        this.enabled =false;
    }

}