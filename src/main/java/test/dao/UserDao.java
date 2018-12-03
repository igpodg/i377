package test.dao;

import org.springframework.stereotype.Repository;
import test.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class UserDao {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public User save(User user) {
        //if (user.getUserName() == null)
        //    em.persist(user);
        //else
        //    em.merge(user);
        em.persist(user);
        return user;
    }

    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class).getResultList();
    }

    @Transactional
    public void delete(String userName) {
        em.createQuery("delete from User u where u.userName = :username")
                .setParameter("username", userName)
                .executeUpdate();
    }

    @Transactional
    public void deleteAll() {
        em.createQuery("delete from User u")
                .executeUpdate();
    }

    public User findByUserName(String userName) {
        TypedQuery<User> query = em.createQuery(
                "select u from User u where u.userName = :username", User.class);
        query.setParameter("username", userName);

        return query.getSingleResult();
    }
}
