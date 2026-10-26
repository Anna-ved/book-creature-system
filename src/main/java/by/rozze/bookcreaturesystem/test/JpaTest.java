package by.rozze.bookcreaturesystem.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaTest {
    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("bookCreaturePU");

        EntityManager em = emf.createEntityManager();

        System.out.println("JPA подключена успешно!");

        em.close();
        emf.close();
    }
}
