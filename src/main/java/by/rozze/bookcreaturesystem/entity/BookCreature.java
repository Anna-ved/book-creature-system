package by.rozze.bookcreaturesystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book_creature")
public class BookCreature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Long version;

    @NotNull
    @NotBlank(message = "Имя существа не может быть пустым")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Координаты должны быть указаны")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates;

    @NotNull
    @Column(name = "creation_date", nullable = false, updatable = false)
    private java.time.LocalDate creationDate;

    @Positive(message = "Возраст должен быть больше 0")
    @Column(nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(name = "creature_type")
    private BookCreatureType creatureType;

    @NotNull(message = "Город должен быть указан")
    @ManyToOne
    @JoinColumn(name = "creature_location_id", nullable = false)
    private MagicCity creatureLocation;

    @Positive(message = "Уровень атаки должен быть больше 0")
    @Column(name = "attack_level", nullable = false)
    private float attackLevel;

    @Positive(message = "Уровень защиты должен быть больше 0")
    @Column(name = "defense_level")
    private Integer defenseLevel;

    @ManyToOne
    @JoinColumn(name = "ring_id")
    private Ring ring;

    @PrePersist
    private void generateCreationDate() {
        if (creationDate == null) {
            creationDate = LocalDate.now();
        }
    }
}
