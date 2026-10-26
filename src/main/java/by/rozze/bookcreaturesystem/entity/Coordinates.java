package by.rozze.bookcreaturesystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coordinates")
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @NotNull(message = "Координата X не может быть пустой")
    @Column(nullable = false)
    @Max(value = 161, message = "Максимальное значение поля: 161")
    private Integer x;

    @NotNull(message = "Координата Y не может быть пустой")
    @Column(nullable = false)
    private Double y;
}
