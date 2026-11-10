package by.rozze.bookcreaturesystem.repository;

import by.rozze.bookcreaturesystem.entity.BookCreature;
import by.rozze.bookcreaturesystem.entity.Ring;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class BookCreatureRepo extends BaseRepo<BookCreature, Integer> {

    public BookCreatureRepo() {
        super(BookCreature.class);
    }

    public List<BookCreature> findAll() {
        EntityManager entityManager = managerFactory.createEntityManager();
        try {
            return entityManager.createQuery(
                    "SELECT DISTINCT b FROM BookCreature b " +
                            "LEFT JOIN FETCH b.coordinates " +
                            "LEFT JOIN FETCH b.creatureLocation " +
                            "LEFT JOIN FETCH b.ring " +
                            "ORDER BY b.id", BookCreature.class
            ).getResultList();
        } finally {
            entityManager.close();
        }
    }

    public BookCreature findByIdWithRelations(Integer id) {
        EntityManager entityManager = managerFactory.createEntityManager();
        try {
            List<BookCreature> list = entityManager.createQuery(
                    "SELECT b FROM BookCreature b " +
                            "LEFT JOIN FETCH b.coordinates " +
                            "LEFT JOIN FETCH b.creatureLocation " +
                            "LEFT JOIN FETCH b.ring " +
                            "WHERE b.id = :id", BookCreature.class
            ).setParameter("id", id).getResultList();

            return list.isEmpty() ? null : list.get(0);
        } finally {
            entityManager.close();
        }
    }

    public boolean deleteById(Integer id) {
        return deleteWithReassignment(id, null);
    }

    public boolean deleteWithReassignment(Integer creatureToDeleteId, Integer targetCreatureId) {
        EntityManager entityManager = managerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            BookCreature toDelete = entityManager.find(BookCreature.class, creatureToDeleteId);
            if (toDelete == null) {
                entityManager.getTransaction().rollback();
                return false;
            }

            Ring ring = toDelete.getRing();

            if (ring != null && targetCreatureId == null) {
                throw new IllegalArgumentException("Выберите, кому передать кольцо.");
            }

            if (targetCreatureId != null) {
                if (ring == null) {
                    throw new IllegalArgumentException(
                            "У существа больше нет кольца. Откройте удаление заново.");
                }
                if (creatureToDeleteId.equals(targetCreatureId)) {
                    throw new IllegalArgumentException("Выберите другое существо.");
                }

                BookCreature target = entityManager.find(BookCreature.class, targetCreatureId);

                if (target == null) {
                    throw new IllegalArgumentException(
                            "Получателя уже удалили. Выберите другое существо.");
                }
                if (target.getRing() != null) {
                    throw new IllegalArgumentException(
                            "У получателя уже есть кольцо. Выберите другое существо.");
                }

                target.setRing(ring);
            }
            entityManager.remove(toDelete);
            entityManager.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public BookCreature update(BookCreature bookCreature) {
        EntityManager entityManager = managerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            BookCreature updated = entityManager.merge(bookCreature);
            entityManager.getTransaction().commit();
            return updated;
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public Double calculateAvgAttackLevel() {
        EntityManager entityManager = managerFactory.createEntityManager();
        try {
            Object result = entityManager.createNativeQuery("SELECT calculate_avg_attack_level()").getSingleResult();
            return result != null ? ((Number) result).doubleValue() : 0.0;
        } finally {
            entityManager.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> groupByAttackLevel() {
        EntityManager entityManager = managerFactory.createEntityManager();
        try {
            return entityManager.createNativeQuery("SELECT * FROM group_by_attack_level()").getResultList();
        } finally {
            entityManager.close();
        }
    }

    public Long countCreaturesByRingWeightLessThan(Float weight) {
        EntityManager entityManager = managerFactory.createEntityManager();
        try {
            Object result = entityManager.createNativeQuery("SELECT count_creatures_by_ring_weight_less_than(?1)")
                    .setParameter(1, weight)
                    .getSingleResult();
            return result != null ? ((Number) result).longValue() : 0L;
        } finally {
            entityManager.close();
        }
    }

    @SuppressWarnings("unchecked")
    public Object[] getCreatureWithStrongestRing() {
        EntityManager entityManager = managerFactory.createEntityManager();
        try {
            List<Object[]> list = entityManager.createNativeQuery("SELECT * FROM get_creature_with_strongest_ring()").getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            entityManager.close();
        }
    }

    public Integer destroyElfCities() {
        EntityManager entityManager = managerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Object result = entityManager.createNativeQuery("SELECT destroy_elf_cities()").getSingleResult();
            entityManager.getTransaction().commit();
            return result == null ? null : ((Number) result).intValue();
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
