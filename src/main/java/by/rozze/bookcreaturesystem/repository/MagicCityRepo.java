package by.rozze.bookcreaturesystem.repository;

import by.rozze.bookcreaturesystem.entity.MagicCity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MagicCityRepo extends BaseRepo<MagicCity, Long> {

    public MagicCityRepo() {
        super(MagicCity.class);
    }
}
