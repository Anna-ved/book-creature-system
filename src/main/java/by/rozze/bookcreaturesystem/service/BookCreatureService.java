package by.rozze.bookcreaturesystem.service;

import by.rozze.bookcreaturesystem.entity.BookCreature;
import by.rozze.bookcreaturesystem.repository.BookCreatureRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class BookCreatureService {

    @Inject
    private BookCreatureRepo repository;

    public List<BookCreature> findAll() {
        return repository.findAll();
    }

    public BookCreature getById(Long id) {
        BookCreature creature = repository.findByIdWithRelations(id);
        if (creature == null) {
            throw new IllegalArgumentException("Объект с id " + id + " не найден");
        }
        return creature;
    }

    public BookCreature create(BookCreature bookCreature) {
        return repository.save(bookCreature);
    }

    public BookCreature update(BookCreature bookCreature) {
        return repository.update(bookCreature);
    }

    public void delete(Long id) {
        deleteWithReassignment(id, null);
    }

    public void deleteWithReassignment(Long toDeleteId, Long targetId) {
        boolean deleted = repository.deleteWithReassignment(toDeleteId, targetId);
        if (!deleted) {
            throw new IllegalArgumentException("Объект с id " + toDeleteId + " не найден для удаления");
        }
    }

    public Double calculateAvgAttackLevel() {
        return repository.calculateAvgAttackLevel();
    }

    public List<Object[]> groupByAttackLevel() {
        return repository.groupByAttackLevel();
    }

    public Long countCreaturesByRingWeightLessThan(Float weight) {
        return repository.countCreaturesByRingWeightLessThan(weight);
    }

    public Object[] getCreatureWithStrongestRing() {
        return repository.getCreatureWithStrongestRing();
    }

    public Integer destroyElfCities() {
        return repository.destroyElfCities();
    }
}