package test.initialization;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import test.dao.UserDao;
import test.model.Authority;
import test.model.User;

@Component
public class DbInitializer implements InitializingBean {
    @Autowired
    private UserDao userDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        // adding users
        User user = new User(
                "user", "$2a$10$YoWXbqqtuR/Vy43eZX4TTOaecZzl6lBCpY64mcnhhUFdkNNtGkDs2",
                true, "User");
        User admin = new User(
                "admin", "$2a$10$Um5IRlfjOxabmC/uLzTDcOp8D0X.4oBd/bbQ1lOae6RFYsAn5Lzca",
                true, "Admin");

        // adding authorities
        user.addAuthority(new Authority(user, "ROLE_USER"));
        admin.addAuthority(new Authority(admin, "ROLE_ADMIN"));

        // saving users
        userDao.save(user);
        userDao.save(admin);
    }
}
