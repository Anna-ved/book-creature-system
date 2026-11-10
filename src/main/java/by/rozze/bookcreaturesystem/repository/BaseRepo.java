package by.rozze.bookcreaturesystem.repository;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.queries.ReadObjectQuery;
import org.eclipse.persistence.sessions.Session;

import java.util.List;

public abstract class BaseRepo<T, ID> {

    @Inject
    protected EclipseLinkStore store;

    @Inject
    private Validator validator;

    private final Class<T> entityClass;

    protected BaseRepo(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T save(T entity) {
        validate(entity);
        return store.write(unit -> entityClass.cast(unit.registerNewObject(entity)));
    }

    public T findById(ID id) {
        return store.read(session -> readById(session, entityClass, id));
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return store.read(session -> {
            ReadAllQuery query = new ReadAllQuery(entityClass);
            query.addAscendingOrdering("id");
            return (List<T>) session.executeQuery(query);
        });
    }

    protected <E> E readById(Session session, Class<E> type, Object id) {
        ReadObjectQuery query = new ReadObjectQuery(type);
        query.setSelectionId(id);
        return type.cast(session.executeQuery(query));
    }

    protected void validate(T entity) {
        var violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}