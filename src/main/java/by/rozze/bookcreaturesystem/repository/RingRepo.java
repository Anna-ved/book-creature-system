package by.rozze.bookcreaturesystem.repository;

import by.rozze.bookcreaturesystem.entity.Ring;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RingRepo extends BaseRepo<Ring, Long> {

    public RingRepo() {
        super(Ring.class);
    }
}