package test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import test.dao.UserDao;
import test.model.User;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserDao dao;

    private enum Privilege {
        GUEST,
        USER,
        ADMIN
    }

    private Privilege getPrivilege(Collection<? extends GrantedAuthority> authorities) {
        Privilege highestRole = Privilege.GUEST;
        for (GrantedAuthority authority : authorities) {
            if (highestRole == Privilege.GUEST && authority.toString().equals("ROLE_USER")) {
                highestRole = Privilege.USER;
            } else if (authority.toString().equals("ROLE_ADMIN")) {
                highestRole = Privilege.ADMIN;
            }
        }
        return highestRole;
    }

    @GetMapping("users")
    public List<User> getUsers(Authentication authentication,
                               HttpServletResponse response) {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Privilege highestRole = getPrivilege(authorities);
        if (highestRole == Privilege.ADMIN) {
            return dao.findAll();
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return null;
    }

    @GetMapping("users/{userName}")
    //@PreAuthorize("#userName == principal.name && hasRole('ROLE_USER')")
    public User getUserByName(@PathVariable String userName,
                              Authentication authentication,
                              HttpServletResponse response,
                              Principal principal) {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        //System.out.println(authorities);
        Privilege highestRole = getPrivilege(authorities);

        if (highestRole == Privilege.ADMIN ||
                (highestRole == Privilege.USER && principal != null && principal.getName().equals(userName))) {

            return dao.findByUserName(userName);
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return null;
    }
}
