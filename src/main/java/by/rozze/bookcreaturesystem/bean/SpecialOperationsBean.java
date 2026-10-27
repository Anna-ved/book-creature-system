package by.rozze.bookcreaturesystem.bean;

import by.rozze.bookcreaturesystem.service.BookCreatureService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class SpecialOperationsBean implements Serializable {

    @Inject
    private BookCreatureService service;

    @Inject
    @Push(channel = "creaturesPush")
    private PushContext pushContext;

    // Операция 1: Средний attackLevel
    private Double avgAttackLevel;

    // Операция 2: Группировка по attackLevel
    private List<AttackGroupDto> attackGroups = new ArrayList<>();

    // Операция 3: Количество существ с весом кольца меньше заданного
    private Float ringWeightThreshold;
    private Long countRingLessThan;

    // Операция 4: Персонаж с самым сильным кольцом
    private StrongestRingDto strongestRingCreature;

    // Операция 5: Уничтожить города эльфов
    private Integer destroyedCitiesCount;

    public void runAvgAttackLevel() {
        try {
            avgAttackLevel = service.calculateAvgAttackLevel();
            addInfoMessage("Средний уровень атаки рассчитан: " + String.format("%.2f", avgAttackLevel));
        } catch (Exception e) {
            addErrorMessage("Ошибка расчета среднего уровня атаки: " + e.getMessage());
        }
    }

    public void runGroupByAttack() {
        try {
            attackGroups.clear();
            List<Object[]> rows = service.groupByAttackLevel();
            for (Object[] row : rows) {
                Float attack = row[0] != null ? ((Number) row[0]).floatValue() : 0.0f;
                Long count = row[1] != null ? ((Number) row[1]).longValue() : 0L;
                attackGroups.add(new AttackGroupDto(attack, count));
            }
            addInfoMessage("Группировка успешно выполнена (групп: " + attackGroups.size() + ")");
        } catch (Exception e) {
            addErrorMessage("Ошибка группировки по атаке: " + e.getMessage());
        }
    }

    public void runCountRingLessThan() {
        try {
            if (ringWeightThreshold == null) {
                addErrorMessage("Укажите пороговый вес кольца");
                return;
            }
            countRingLessThan = service.countCreaturesByRingWeightLessThan(ringWeightThreshold);
            addInfoMessage("Найдено существ: " + countRingLessThan);
        } catch (Exception e) {
            addErrorMessage("Ошибка подсчета: " + e.getMessage());
        }
    }

    public void runStrongestRing() {
        try {
            Object[] row = service.getCreatureWithStrongestRing();
            if (row != null) {
                Long id = row[0] != null ? ((Number) row[0]).longValue() : null;
                String name = row[1] != null ? (String) row[1] : "";
                Integer age = row[2] != null ? ((Number) row[2]).intValue() : 0;
                String ringName = row[3] != null ? (String) row[3] : "";
                Float ringWeight = row[4] != null ? ((Number) row[4]).floatValue() : 0.0f;
                strongestRingCreature = new StrongestRingDto(id, name, age, ringName, ringWeight);
                addInfoMessage("Персонаж найден: " + name);
            } else {
                strongestRingCreature = null;
                addInfoMessage("Персонажи с кольцами не найдены");
            }
        } catch (Exception e) {
            addErrorMessage("Ошибка поиска: " + e.getMessage());
        }
    }

    public void runDestroyElfCities() {
        try {
            destroyedCitiesCount = service.destroyElfCities();
            addInfoMessage("Уничтожено городов эльфов: " + destroyedCitiesCount);
            if (pushContext != null) {
                pushContext.send("update");
            }
        } catch (Exception e) {
            addErrorMessage("Ошибка уничтожения городов: " + e.getMessage());
        }
    }

    private void addInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AttackGroupDto implements Serializable {
        private Float attackLevel;
        private Long count;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrongestRingDto implements Serializable {
        private Long creatureId;
        private String creatureName;
        private Integer creatureAge;
        private String ringName;
        private Float ringWeight;
    }
}
