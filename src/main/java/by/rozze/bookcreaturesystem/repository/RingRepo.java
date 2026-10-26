package by.rozze.bookcreaturesystem.repository;

import by.rozze.bookcreaturesystem.entity.Ring;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class RingRepo extends BaseRepo<Ring> {

    public RingRepo() {
        super(Ring.class);
    }

    public List<Ring> findAll() {
        EntityManager em = managerFactory.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT r FROM Ring r",
                    Ring.class
            ).getResultList();
        } finally {
            em.close();
        }
    }
}