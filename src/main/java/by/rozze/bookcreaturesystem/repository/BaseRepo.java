package by.rozze.bookcreaturesystem.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;

public abstract class BaseRepo<T, ID> {

    @PersistenceUnit(unitName = "bookCreaturePU")
    protected EntityManagerFactory managerFactory;

    private final Class<T> entityClass;

    protected BaseRepo(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T save(T entity) {
        EntityManager entityManager = managerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();

            return entity;
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public T findById(ID id) {
        EntityManager entityManager = managerFactory.createEntityManager();

        try {
            return entityManager.find(entityClass, id);
        } finally {
            entityManager.close();
        }
    }
}