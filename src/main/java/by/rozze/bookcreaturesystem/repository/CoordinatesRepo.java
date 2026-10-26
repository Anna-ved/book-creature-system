package by.rozze.bookcreaturesystem.repository;

import by.rozze.bookcreaturesystem.entity.Coordinates;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

@ApplicationScoped
public class CoordinatesRepo extends BaseRepo<Coordinates> {

    public CoordinatesRepo() {
        super(Coordinates.class);
    }

    private final EntityManagerFactory managerFactory =
            Persistence.createEntityManagerFactory("bookCreaturePU");

    public List<Coordinates> findAll() {
        EntityManager em = managerFactory.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT c FROM Coordinates c",
                    Coordinates.class
            ).getResultList();
        } finally {
            em.close();
        }
    }
}