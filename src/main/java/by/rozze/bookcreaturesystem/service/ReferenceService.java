package by.rozze.bookcreaturesystem.service;

import by.rozze.bookcreaturesystem.entity.Coordinates;
import by.rozze.bookcreaturesystem.entity.MagicCity;
import by.rozze.bookcreaturesystem.entity.Ring;
import by.rozze.bookcreaturesystem.repository.CoordinatesRepo;
import by.rozze.bookcreaturesystem.repository.MagicCityRepo;
import by.rozze.bookcreaturesystem.repository.RingRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ReferenceService {

    @Inject
    private CoordinatesRepo coordinatesRepo;

    @Inject
    private MagicCityRepo magicCityRepo;

    @Inject
    private RingRepo ringRepo;

    public List<Coordinates> getAllCoordinates() {
        return coordinatesRepo.findAll();
    }

    public List<MagicCity> getAllCities() {
        return magicCityRepo.findAll();
    }

    public List<Ring> getAllRings() {
        return ringRepo.findAll();
    }

    public Coordinates getCoordinatesById(Long id) {
        return coordinatesRepo.findById(id);
    }

    public MagicCity getCityById(Long id) {
        return magicCityRepo.findById(id);
    }

    public Ring getRingById(Long id) {
        return ringRepo.findById(id);
    }

    public Coordinates createCoordinates(Coordinates coordinates) {
        return coordinatesRepo.save(coordinates);
    }

    public MagicCity createCity(MagicCity city) {
        return magicCityRepo.save(city);
    }

    public Ring createRing(Ring ring) {
        return ringRepo.save(ring);
    }
}