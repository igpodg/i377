package test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @NotNull
    @Column(name = "username")
    private String userName;

    @NotNull
    private String password;

    @NotNull
    private boolean enabled;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Authority> authorities;

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public List<Authority> getAuthorities() {
        return this.authorities;
    }

    public void addAuthority(Authority authority) {
        if (this.authorities == null) {
            this.authorities = new ArrayList<>();
        }

        this.authorities.add(authority);
    }

    public User() {}

    public User(String userName, String password, boolean enabled, String firstName) {
        this.userName = userName;
        this.password = password;
        this.enabled = enabled;
        this.firstName = firstName;
        this.authorities = null;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", firstName='" + firstName + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
