package by.rozze.bookcreaturesystem.repository;

import by.rozze.bookcreaturesystem.entity.MagicCity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class MagicCityRepo extends BaseRepo<MagicCity> {

    public MagicCityRepo() {
        super(MagicCity.class);
    }

    public List<MagicCity> findAll() {
        EntityManager em = managerFactory.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT m FROM MagicCity m",
                    MagicCity.class
            ).getResultList();
        } finally {
            em.close();
        }
    }
}
