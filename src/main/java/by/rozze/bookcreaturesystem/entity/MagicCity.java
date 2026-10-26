package by.rozze.bookcreaturesystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "magic_city")
public class MagicCity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank(message = "Название города не может быть пустым")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Площадь города не может быть пустой")
    @Positive(message = "Площадь города должна быть больше 0")
    @Column(nullable = false)
    private Float area;

    @NotNull(message = "Население города не может быть пустым")
    @Positive(message = "Население города должно быть больше 0")
    @Column(nullable = false)
    private Long population;

    @Column(name = "establishment_date")
    private java.time.LocalDate establishmentDate;

    @Enumerated(EnumType.STRING)
    private BookCreatureType governor;

    @NotNull(message = "Необходимо указать, является ли город столицей")
    @Column(nullable = false)
    private Boolean capital;

    @Positive(message = "Плотность населения должна быть больше 0")
    @Column(name = "population_density", nullable = false)
    private double populationDensity;
}
