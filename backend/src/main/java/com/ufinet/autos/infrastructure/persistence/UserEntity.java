package com.ufinet.autos.infrastructure.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufinet.autos.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * JPA entity representing a row in the USERS table.
 * Mapped from/to domain {@link User} objects by the persistence adapter.
 * The domain layer never imports this class.
 */
@Entity(name = "User")
@Table(name = "USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AutoEntity> cars;

    /**
     * Maps this JPA entity to a pure domain {@link User} object.
     */
    public User toDomain() {
        return new User(id, username, password);
    }

    /**
     * Creates a JPA entity from a domain {@link User}.
     *
     * @param user the domain object to map
     */
    public static UserEntity fromDomain(User user) {
        UserEntity entity = new UserEntity();
        if (user.getId() != null) entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        return entity;
    }
}
