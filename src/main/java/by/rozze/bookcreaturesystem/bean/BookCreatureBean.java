package by.rozze.bookcreaturesystem.bean;

import by.rozze.bookcreaturesystem.entity.BookCreature;
import by.rozze.bookcreaturesystem.entity.BookCreatureType;
import by.rozze.bookcreaturesystem.entity.Coordinates;
import by.rozze.bookcreaturesystem.entity.MagicCity;
import by.rozze.bookcreaturesystem.entity.Ring;
import by.rozze.bookcreaturesystem.service.BookCreatureService;
import by.rozze.bookcreaturesystem.service.ReferenceService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class BookCreatureBean implements Serializable {

    @Inject
    private BookCreatureService service;

    @Inject
    private ReferenceService referenceService;

    private List<BookCreature> creatures;
    private BookCreature newCreature;
    private List<Coordinates> coordinates;
    private List<MagicCity> cities;
    private List<Ring> rings;

    private Long coordinatesId;
    private Long cityId;
    private Long ringId;

    @PostConstruct
    public void init() {
        creatures = service.findAll();

        coordinates = referenceService.getAllCoordinates();
        cities = referenceService.getAllCities();
        rings = referenceService.getAllRings();

        newCreature = new BookCreature();
    }

    public BookCreatureType[] getCreatureTypes() {
        return BookCreatureType.values();
    }

    public String create() {

        newCreature.setCoordinates(
                referenceService.getCoordinatesById(coordinatesId)
        );

        newCreature.setCreatureLocation(
                referenceService.getCityById(cityId)
        );

        if (ringId != null) {
            newCreature.setRing(
                    referenceService.getRingById(ringId)
            );
        }

        service.create(newCreature);

        return "index.xhtml?faces-redirect=true";
    }
}