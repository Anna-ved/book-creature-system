package by.rozze.bookcreaturesystem.repository;

import by.rozze.bookcreaturesystem.entity.BookCreature;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BookCreatureRepo extends BaseRepo<BookCreature> {

    private final EntityManagerFactory managerFactory =
            Persistence.createEntityManagerFactory("bookCreaturePU");

    public BookCreatureRepo() {
        super(BookCreature.class);
    }

    public List<BookCreature> findAll(){
        EntityManager entityManager = managerFactory.createEntityManager();
        try{
            return entityManager.createQuery("SELECT b FROM BookCreature b",  BookCreature.class).getResultList();
        }

        finally{
            entityManager.close();
        }
    }

    public boolean deleteById(Long id) {
        EntityManager entityManager = managerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            BookCreature entity = entityManager.find(BookCreature.class, id);

            if (entity == null) {
                entityManager.getTransaction().rollback();
                return false;
            }

            entityManager.remove(entity);
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

            BookCreature updated =
                    entityManager.merge(bookCreature);

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

}
