package by.rozze.bookcreaturesystem.bean;

import by.rozze.bookcreaturesystem.entity.BookCreatureType;
import by.rozze.bookcreaturesystem.entity.Coordinates;
import by.rozze.bookcreaturesystem.entity.MagicCity;
import by.rozze.bookcreaturesystem.entity.Ring;
import by.rozze.bookcreaturesystem.service.ReferenceService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
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
        try {
            service.createCoordinates(coordinates);
            coordinates = new Coordinates();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Координаты успешно сохранены", null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка сохранения координат: " + e.getMessage(), null));
        }
    }

    public void saveCity() {
        try {
            service.createCity(city);
            city = new MagicCity();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Город успешно сохранен", null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка сохранения города: " + e.getMessage(), null));
        }
    }

    public void saveRing() {
        try {
            service.createRing(ring);
            ring = new Ring();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Кольцо успешно сохранено", null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка сохранения кольца: " + e.getMessage(), null));
        }
    }
}