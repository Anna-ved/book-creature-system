package by.rozze.bookcreaturesystem.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {
    private  Long id;

    @NotNull(message = "Координата X не может быть пустой")
    @Max(value = 161, message = "Максимальное значение поля: 161")
    private Integer x;

    @NotNull(message = "Координата Y не может быть пустой")
    private Double y;
}
