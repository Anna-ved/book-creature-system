package by.rozze.bookcreaturesystem.repository;

import by.rozze.bookcreaturesystem.entity.Coordinates;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class CoordinatesRepo extends BaseRepo<Coordinates, Long> {

    public CoordinatesRepo() {
        super(Coordinates.class);
    }

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