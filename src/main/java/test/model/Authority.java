package test.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "authorities")
public class Authority implements Serializable {
    @Id
    @NotNull
    @JoinColumn(name = "username")
    @ManyToOne
    private User user;

    @NotNull
    private String authority;

    public User getUser() {
        return this.user;
    }

    public String getAuthority() {
        return this.authority;
    }

    public Authority() {}

    public Authority(User user, String authority) {
        this.user = user;
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "authority='" + authority + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Authority) {
            Authority a = (Authority) obj;
            return this.authority.equals(a.authority);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.authority);
    }
}
