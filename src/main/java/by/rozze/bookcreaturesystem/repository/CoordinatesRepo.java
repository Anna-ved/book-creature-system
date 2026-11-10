package by.rozze.bookcreaturesystem.repository;

import by.rozze.bookcreaturesystem.entity.Coordinates;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CoordinatesRepo extends BaseRepo<Coordinates, Long> {

    public CoordinatesRepo() {
        super(Coordinates.class);
    }
}