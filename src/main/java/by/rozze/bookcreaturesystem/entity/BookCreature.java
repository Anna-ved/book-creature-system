package by.rozze.bookcreaturesystem.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCreature {

    @Positive(message = "ID должен быть больше нуля")
    private Integer id;

    private Long version;

    @NotNull
    @NotBlank(message = "Имя существа не может быть пустым")
    private String name;

    @NotNull(message = "Координаты должны быть указаны")
    private Coordinates coordinates;

    @NotNull
    private java.time.LocalDate creationDate;

    @Positive(message = "Возраст должен быть больше 0")
    private int age;

    private BookCreatureType creatureType;

    @NotNull(message = "Город должен быть указан")
    private MagicCity creatureLocation;

    @Positive(message = "Уровень атаки должен быть больше 0")
    private float attackLevel;

    @Positive(message = "Уровень защиты должен быть больше 0")
    private Integer defenseLevel;

    private Ring ring;

}
