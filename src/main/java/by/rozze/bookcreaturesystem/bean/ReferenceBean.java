package by.rozze.bookcreaturesystem.bean;

import by.rozze.bookcreaturesystem.entity.BookCreatureType;
import by.rozze.bookcreaturesystem.entity.Coordinates;
import by.rozze.bookcreaturesystem.entity.MagicCity;
import by.rozze.bookcreaturesystem.entity.Ring;
import by.rozze.bookcreaturesystem.service.ReferenceService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Named
@ViewScoped
@Getter
@Setter
public class ReferenceBean implements Serializable {

    @Inject
    private ReferenceService service;

    private Coordinates coordinates = new Coordinates();
    private MagicCity city = new MagicCity();
    private Ring ring = new Ring();

    public BookCreatureType[] getCreatureTypes() {
        return BookCreatureType.values();
    }

    public void saveCoordinates() {
        service.createCoordinates(coordinates);
        coordinates = new Coordinates();
    }

    public void saveCity() {
        service.createCity(city);
        city = new MagicCity();
    }

    public void saveRing() {
        service.createRing(ring);
        ring = new Ring();
    }
}