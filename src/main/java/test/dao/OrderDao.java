package test.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import test.model.Order;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.sql.*;
import java.util.List;

@Repository
public class OrderDao {
    //@Autowired
    //private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Order save(Order order) {
        if (order.getId() == null)
            em.persist(order);
        else
            em.merge(order);
        return order;
    }

    public List<Order> findAll() {
        return em.createQuery("select o from Order o", Order.class).getResultList();
    }

    @Transactional
    public void delete(Long id) {
        em.createQuery("delete from Order o where o.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Transactional
    public void deleteAll() {
        em.createQuery("delete from Order o")
                .executeUpdate();
    }

    public Order findById(Long id) {
        TypedQuery<Order> query = em.createQuery("select o from Order o where o.id = :id", Order.class);
        query.setParameter("id", id);

        return query.getSingleResult();
    }
}
