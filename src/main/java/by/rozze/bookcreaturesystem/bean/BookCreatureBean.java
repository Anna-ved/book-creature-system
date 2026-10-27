package by.rozze.bookcreaturesystem.bean;

import by.rozze.bookcreaturesystem.entity.BookCreature;
import by.rozze.bookcreaturesystem.entity.BookCreatureType;
import by.rozze.bookcreaturesystem.entity.Coordinates;
import by.rozze.bookcreaturesystem.entity.MagicCity;
import by.rozze.bookcreaturesystem.entity.Ring;
import by.rozze.bookcreaturesystem.service.BookCreatureService;
import by.rozze.bookcreaturesystem.service.ReferenceService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter
@Setter
public class BookCreatureBean implements Serializable {

    @Inject
    private BookCreatureService service;

    @Inject
    private ReferenceService referenceService;

    @Inject
    @Push(channel = "creaturesPush")
    private PushContext pushContext;

    private List<BookCreature> creatures;
    private BookCreature newCreature = new BookCreature();
    private BookCreature selectedCreature = new BookCreature();
    private BookCreature editingCreature = new BookCreature();
    private BookCreature creatureToDelete = new BookCreature();

    private List<Coordinates> coordinates;
    private List<MagicCity> cities;
    private List<Ring> rings;

    private Long coordinatesId;
    private Long cityId;
    private Long ringId;

    private Long editCoordinatesId;
    private Long editCityId;
    private Long editRingId;

    private Long reassignTargetId;

    @PostConstruct
    public void init() {
        loadData();
        newCreature = new BookCreature();
        selectedCreature = new BookCreature();
        editingCreature = new BookCreature();
        creatureToDelete = new BookCreature();
    }

    public void loadData() {
        creatures = service.findAll();
        coordinates = referenceService.getAllCoordinates();
        cities = referenceService.getAllCities();
        rings = referenceService.getAllRings();
    }

    public BookCreatureType[] getCreatureTypes() {
        return BookCreatureType.values();
    }

    public String create() {
        try {
            newCreature.setCoordinates(referenceService.getCoordinatesById(coordinatesId));
            newCreature.setCreatureLocation(referenceService.getCityById(cityId));

            if (ringId != null) {
                newCreature.setRing(referenceService.getRingById(ringId));
            }

            service.create(newCreature);
            notifyClients();
            return "index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка создания: " + e.getMessage(), null));
            return null;
        }
    }

    public void prepareView(BookCreature creature) {
        if (creature != null) {
            this.selectedCreature = service.getById(creature.getId());
        }
    }

    public void prepareEdit(BookCreature creature) {
        if (creature != null) {
            this.editingCreature = service.getById(creature.getId());
            this.editCoordinatesId = (editingCreature.getCoordinates() != null) ? editingCreature.getCoordinates().getId() : null;
            this.editCityId = (editingCreature.getCreatureLocation() != null) ? editingCreature.getCreatureLocation().getId() : null;
            this.editRingId = (editingCreature.getRing() != null) ? editingCreature.getRing().getId() : null;
        }
    }

    public void update() {
        try {
            if (editingCreature == null) return;

            editingCreature.setCoordinates(referenceService.getCoordinatesById(editCoordinatesId));
            editingCreature.setCreatureLocation(referenceService.getCityById(editCityId));

            if (editRingId != null) {
                editingCreature.setRing(referenceService.getRingById(editRingId));
            } else {
                editingCreature.setRing(null);
            }

            service.update(editingCreature);
            loadData();
            notifyClients();

            PrimeFaces.current().executeScript("PF('editDialog').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Существо успешно обновлено", null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка обновления: " + e.getMessage(), null));
        }
    }

    public void prepareDelete(BookCreature creature) {
        this.creatureToDelete = creature;
        this.reassignTargetId = null;
    }

    public void confirmDelete() {
        try {
            if (creatureToDelete != null) {
                service.deleteWithReassignment(creatureToDelete.getId(), reassignTargetId);
                loadData();
                notifyClients();

                PrimeFaces.current().executeScript("PF('deleteDialog').hide();");
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Существо удалено", null));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка удаления: " + e.getMessage(), null));
        }
    }

    public List<BookCreature> getReassignCandidates() {
        if (creatures == null || creatureToDelete == null) {
            return creatures != null ? creatures : new ArrayList<>();
        }
        return creatures.stream()
                .filter(c -> !c.getId().equals(creatureToDelete.getId()))
                .collect(Collectors.toList());
    }

    private void notifyClients() {
        try {
            if (pushContext != null) {
                pushContext.send("update");
            }
        } catch (Exception ignored) {
        }
    }
}