package by.rozze.bookcreaturesystem.repository;

import by.rozze.bookcreaturesystem.entity.BookCreature;
import by.rozze.bookcreaturesystem.entity.BookCreatureType;
import by.rozze.bookcreaturesystem.entity.Coordinates;
import by.rozze.bookcreaturesystem.entity.MagicCity;
import by.rozze.bookcreaturesystem.entity.Ring;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.persistence.config.CacheIsolationType;
import org.eclipse.persistence.descriptors.RelationalDescriptor;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.mappings.ManyToOneMapping;
import org.eclipse.persistence.mappings.converters.EnumTypeConverter;
import org.eclipse.persistence.platform.database.PostgreSQLPlatform;
import org.eclipse.persistence.sequencing.NativeSequence;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.JNDIConnector;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.sessions.server.ClientSession;
import org.eclipse.persistence.sessions.server.Server;

import javax.sql.DataSource;
import java.util.function.Function;

@ApplicationScoped
public class EclipseLinkStore {

    @Resource(lookup = "jdbc/BookCreature")
    private DataSource dataSource;

    private Server server;

    @PostConstruct
    void start() {
        DatabaseLogin login = new DatabaseLogin(new PostgreSQLPlatform());
        login.setConnector(new JNDIConnector(dataSource));
        login.useExternalConnectionPooling();

        Project project = new Project();
        project.setLogin(login);

        RelationalDescriptor coordinates = descriptor(login, Coordinates.class, "coordinates");
        coordinates.addDirectMapping("x", "x");
        coordinates.addDirectMapping("y", "y");
        project.addDescriptor(coordinates);

        RelationalDescriptor city = descriptor(login, MagicCity.class, "magic_city");
        city.addDirectMapping("name", "name");
        city.addDirectMapping("area", "area");
        city.addDirectMapping("population", "population");
        city.addDirectMapping("establishmentDate", "establishment_date");
        city.addMapping(typeMapping("governor", "governor"));
        city.addDirectMapping("capital", "capital");
        city.addDirectMapping("populationDensity", "population_density");
        project.addDescriptor(city);

        RelationalDescriptor ring = descriptor(login, Ring.class, "ring");
        ring.addDirectMapping("name", "name");
        ring.addDirectMapping("weight", "weight");
        project.addDescriptor(ring);

        RelationalDescriptor creature = descriptor(login, BookCreature.class, "book_creature");
        creature.addDirectMapping("version", "version");
        creature.useVersionLocking("book_creature.version", false);
        creature.addDirectMapping("name", "name");
        creature.addDirectMapping("creationDate", "creation_date");
        creature.addDirectMapping("age", "age");
        creature.addMapping(typeMapping("creatureType", "creature_type"));
        creature.addDirectMapping("attackLevel", "attack_level");
        creature.addDirectMapping("defenseLevel", "defense_level");
        creature.addMapping(reference("coordinates", Coordinates.class, "coordinates_id", "coordinates"));
        creature.addMapping(reference("creatureLocation", MagicCity.class, "creature_location_id", "magic_city"));
        creature.addMapping(reference("ring", Ring.class, "ring_id", "ring"));
        project.addDescriptor(creature);

        server = project.createServerSession();
        server.login();
    }

    @PreDestroy
    void stop() {
        if (server != null && server.isConnected()) {
            server.logout();
        }
    }

    public <T> T read(Function<ClientSession, T> operation) {
        ClientSession session = server.acquireClientSession();
        try {
            return operation.apply(session);
        } finally {
            session.release();
        }
    }

    public <T> T write(Function<UnitOfWork, T> operation) {
        return read(session -> {
            UnitOfWork unit = session.acquireUnitOfWork();
            try {
                T result = operation.apply(unit);
                unit.commit();
                return result;
            } finally {
                unit.release();
            }
        });
    }

    private RelationalDescriptor descriptor(DatabaseLogin login, Class<?> type, String table) {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(type);
        descriptor.addTableName(table);
        descriptor.setPrimaryKeyFieldName(table + ".id");
        descriptor.addDirectMapping("id", "id");
        descriptor.setCacheIsolation(CacheIsolationType.ISOLATED);

        String sequenceName = table + "_id_seq";
        login.addSequence(new NativeSequence(sequenceName, 1, false));
        descriptor.setSequenceNumberName(sequenceName);
        descriptor.setSequenceNumberFieldName(table + ".id");
        return descriptor;
    }

    private DirectToFieldMapping typeMapping(String attribute, String column) {
        DirectToFieldMapping mapping = new DirectToFieldMapping();
        mapping.setAttributeName(attribute);
        mapping.setFieldName(column);
        mapping.setConverter(new EnumTypeConverter(mapping, BookCreatureType.class, false));
        return mapping;
    }

    private ManyToOneMapping reference(String attribute, Class<?> type, String column, String targetTable) {
        ManyToOneMapping mapping = new ManyToOneMapping();
        mapping.setAttributeName(attribute);
        mapping.setReferenceClass(type);
        mapping.addForeignKeyFieldName("book_creature." + column, targetTable + ".id");
        mapping.dontUseIndirection();
        mapping.useOuterJoinFetch();
        return mapping;
    }
}