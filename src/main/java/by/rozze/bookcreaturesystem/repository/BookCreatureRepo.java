package by.rozze.bookcreaturesystem.repository;

import by.rozze.bookcreaturesystem.entity.BookCreature;
import by.rozze.bookcreaturesystem.entity.Coordinates;
import by.rozze.bookcreaturesystem.entity.MagicCity;
import by.rozze.bookcreaturesystem.entity.Ring;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.persistence.queries.DataReadQuery;
import org.eclipse.persistence.sessions.Session;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class BookCreatureRepo extends BaseRepo<BookCreature, Integer> {

    public BookCreatureRepo() {
        super(BookCreature.class);
    }

    @Override
    public BookCreature save(BookCreature creature) {
        if (creature.getCreationDate() == null) {
            creature.setCreationDate(LocalDate.now());
        }
        validate(creature);
        return store.write(unit -> {
            setReferences(unit, creature, creature);
            return (BookCreature) unit.registerNewObject(creature);
        });
    }

    public BookCreature findByIdWithRelations(Integer id) {
        return findById(id);
    }

    public boolean deleteById(Integer id) {
        return deleteWithReassignment(id, null);
    }

    public boolean deleteWithReassignment(Integer creatureToDeleteId, Integer targetCreatureId) {
        return store.write(unit -> {
            BookCreature toDelete = readById(unit, BookCreature.class, creatureToDeleteId);
            if (toDelete == null) {
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

                BookCreature target = readById(unit, BookCreature.class, targetCreatureId);
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

            unit.deleteObject(toDelete);
            return true;
        });
    }

    public BookCreature update(BookCreature creature) {
        validate(creature);
        return store.write(unit -> {
            BookCreature current = readById(unit, BookCreature.class, creature.getId());
            if (current == null || !Objects.equals(current.getVersion(), creature.getVersion())) {
                throw new IllegalArgumentException("Существо уже изменили или удалили. Откройте его заново.");
            }

            current.setName(creature.getName());
            current.setAge(creature.getAge());
            current.setCreatureType(creature.getCreatureType());
            current.setAttackLevel(creature.getAttackLevel());
            current.setDefenseLevel(creature.getDefenseLevel());
            setReferences(unit, current, creature);
            return current;
        });
    }

    private void setReferences(Session session, BookCreature destination, BookCreature source) {
        destination.setCoordinates(requireReference(session, Coordinates.class,
                source.getCoordinates().getId(), "Эти координаты больше недоступны."));
        destination.setCreatureLocation(requireReference(session, MagicCity.class,
                source.getCreatureLocation().getId(), "Этот город больше недоступен."));

        Ring ring = source.getRing();
        destination.setRing(ring == null ? null : requireReference(session, Ring.class,
                ring.getId(), "Это кольцо больше недоступно."));
    }

    private <T> T requireReference(Session session, Class<T> type, Long id, String message) {
        T reference = id == null ? null : readById(session, type, id);
        if (reference == null) {
            throw new IllegalArgumentException(message);
        }
        return reference;
    }

    public Double calculateAvgAttackLevel() {
        return store.read(session -> {
            Number result = scalar(session, new DataReadQuery("SELECT calculate_avg_attack_level()"));
            return result == null ? 0.0 : result.doubleValue();
        });
    }

    public List<Object[]> groupByAttackLevel() {
        return store.read(session -> rows(session, "SELECT * FROM group_by_attack_level()"));
    }

    public Long countCreaturesByRingWeightLessThan(Float weight) {
        return store.read(session -> {
            DataReadQuery query = new DataReadQuery("SELECT count_creatures_by_ring_weight_less_than(#weight)");
            query.addArgument("weight", Float.class);
            query.setResultType(DataReadQuery.VALUE);
            Number result = (Number) session.executeQuery(query, List.of(weight));
            return result == null ? 0L : result.longValue();
        });
    }

    public Object[] getCreatureWithStrongestRing() {
        return store.read(session -> {
            List<Object[]> result = rows(session, "SELECT * FROM get_creature_with_strongest_ring()");
            return result.isEmpty() ? null : result.get(0);
        });
    }

    public Integer destroyElfCities() {
        return store.write(unit -> {
            unit.beginEarlyTransaction();
            Number result = scalar(unit, new DataReadQuery("SELECT destroy_elf_cities()"));
            return result == null ? null : result.intValue();
        });
    }

    private Number scalar(Session session, DataReadQuery query) {
        query.setResultType(DataReadQuery.VALUE);
        return (Number) session.executeQuery(query);
    }

    @SuppressWarnings("unchecked")
    private List<Object[]> rows(Session session, String sql) {
        DataReadQuery query = new DataReadQuery(sql);
        query.setResultType(DataReadQuery.ARRAY);
        return (List<Object[]>) session.executeQuery(query);
    }
}