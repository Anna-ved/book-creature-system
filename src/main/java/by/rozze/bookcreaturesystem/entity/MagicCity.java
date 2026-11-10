package by.rozze.bookcreaturesystem.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MagicCity {

    private Long id;

    @NotNull
    @NotBlank(message = "Название города не может быть пустым")
    private String name;

    @NotNull(message = "Площадь города не может быть пустой")
    @Positive(message = "Площадь города должна быть больше 0")
    private Float area;

    @NotNull(message = "Население города не может быть пустым")
    @Positive(message = "Население города должно быть больше 0")
    private Long population;

    @PastOrPresent(message = "Дата основания не может быть в будущем")
    private java.time.LocalDate establishmentDate;

    private BookCreatureType governor;

    @NotNull(message = "Необходимо указать, является ли город столицей")
    private Boolean capital;

    @Positive(message = "Плотность населения должна быть больше 0")
    private double populationDensity;
}
