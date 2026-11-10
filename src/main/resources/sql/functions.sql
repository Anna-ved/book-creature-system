CREATE TABLE IF NOT EXISTS coordinates (
                                           id BIGSERIAL PRIMARY KEY,
                                           x INTEGER NOT NULL CHECK (x <= 161),
                                           y DOUBLE PRECISION NOT NULL
);
CREATE TABLE IF NOT EXISTS magic_city (
                                          id BIGSERIAL PRIMARY KEY,
                                          name VARCHAR(255) NOT NULL CHECK (length(trim(name)) > 0),
                                          area REAL NOT NULL CHECK (area > 0),
                                          population BIGINT NOT NULL CHECK (population > 0),
                                          establishment_date DATE,
                                          governor VARCHAR(50),
                                          capital BOOLEAN NOT NULL,
                                          population_density DOUBLE PRECISION NOT NULL CHECK (population_density > 0),
                                          CONSTRAINT magic_city_governor_allowed
                                              CHECK (governor IN ('HOBBIT', 'ELF', 'GOLLUM')),
                                          CONSTRAINT magic_city_area_finite
                                              CHECK (area < 'Infinity'::double precision),
                                          CONSTRAINT magic_city_density_finite
                                              CHECK (population_density < 'Infinity'::double precision)
);
CREATE TABLE IF NOT EXISTS ring (
                                    id BIGSERIAL PRIMARY KEY,
                                    name VARCHAR(255) NOT NULL CHECK (length(trim(name)) > 0),
                                    weight REAL NOT NULL CHECK (weight > 0),
                                    CONSTRAINT ring_weight_finite
                                        CHECK (weight < 'Infinity'::double precision)
);
CREATE TABLE IF NOT EXISTS book_creature (
                                             id SERIAL PRIMARY KEY,
                                             name VARCHAR(255) NOT NULL CHECK (length(trim(name)) > 0),
                                             coordinates_id BIGINT NOT NULL REFERENCES coordinates(id) ON DELETE RESTRICT,
                                             creation_date DATE NOT NULL DEFAULT CURRENT_DATE,
                                             age INTEGER NOT NULL CHECK (age > 0),
                                             creature_type VARCHAR(50),
                                             creature_location_id BIGINT NOT NULL REFERENCES magic_city(id) ON DELETE RESTRICT,
                                             attack_level REAL NOT NULL CHECK (attack_level > 0),
                                             defense_level INTEGER CHECK (defense_level IS NULL OR defense_level > 0),
                                             ring_id BIGINT REFERENCES ring(id) ON DELETE SET NULL,
                                             version BIGINT NOT NULL DEFAULT 0,
                                             CONSTRAINT book_creature_id_positive CHECK (id > 0),
                                             CONSTRAINT book_creature_type_allowed
                                                 CHECK (creature_type IN ('HOBBIT', 'ELF', 'GOLLUM')),
                                             CONSTRAINT book_creature_attack_finite
                                                 CHECK (attack_level < 'Infinity'::double precision)
);


CREATE OR REPLACE FUNCTION calculate_avg_attack_level()
RETURNS double precision AS $$
BEGIN
RETURN (SELECT COALESCE(AVG(attack_level), 0.0) FROM book_creature);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION group_by_attack_level()
RETURNS TABLE(attack_level real, cnt bigint) AS $$
BEGIN
RETURN QUERY
SELECT b.attack_level, COUNT(*)::bigint
FROM book_creature b
GROUP BY b.attack_level
ORDER BY b.attack_level;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION count_creatures_by_ring_weight_less_than(p_weight real)
RETURNS bigint AS $$
BEGIN
RETURN (
    SELECT COUNT(*)::bigint
    FROM book_creature b
             JOIN ring r ON b.ring_id = r.id
    WHERE r.weight < p_weight
);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION get_creature_with_strongest_ring()
RETURNS TABLE(
    creature_id bigint,
    creature_name varchar,
    creature_age integer,
    ring_name varchar,
    ring_weight real
) AS $$
BEGIN
RETURN QUERY
SELECT b.id::bigint, b.name, b.age, r.name, r.weight
FROM book_creature b
         JOIN ring r ON b.ring_id = r.id
ORDER BY r.weight DESC, r.id ASC
    LIMIT 1;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION destroy_elf_cities()
    RETURNS integer AS $$
DECLARE
    destination_id bigint;
    deleted_count integer;
BEGIN
    SELECT id INTO destination_id
    FROM magic_city
    WHERE governor IS DISTINCT FROM 'ELF'
    ORDER BY id
    LIMIT 1
        FOR SHARE;

    IF destination_id IS NULL AND EXISTS (
        SELECT 1
        FROM book_creature b
                 JOIN magic_city c ON c.id = b.creature_location_id
        WHERE c.governor = 'ELF'
    ) THEN
        RETURN NULL;
    END IF;

    IF destination_id IS NOT NULL THEN
        UPDATE book_creature b
        SET creature_location_id = destination_id,
            version = b.version + 1
        FROM magic_city c
        WHERE c.id = b.creature_location_id AND c.governor = 'ELF';
    END IF;

    DELETE FROM magic_city WHERE governor = 'ELF';
    GET DIAGNOSTICS deleted_count = ROW_COUNT;
    RETURN deleted_count;
END;
$$ LANGUAGE plpgsql;
