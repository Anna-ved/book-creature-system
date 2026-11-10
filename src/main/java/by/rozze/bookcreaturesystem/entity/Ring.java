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
public class Ring {
    private Long id;

    @NotNull
    @NotBlank(message = "Название кольца не может быть пустым")
    private String name;

    @NotNull(message = "Вес кольца не может быть пустым")
    @Positive(message = "Вес кольца должен быть больше 0")
    private Float weight;
}
