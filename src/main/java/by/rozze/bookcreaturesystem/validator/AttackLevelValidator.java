package by.rozze.bookcreaturesystem.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator("positiveAttackLevel")
public class AttackLevelValidator implements Validator<Float> {

    @Override
    public void validate(FacesContext context, UIComponent component, Float value) {
        if (value != null && (!Float.isFinite(value) || value <= 0.0f)) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Уровень атаки должен быть конечным числом больше нуля.",
                    null));
        }
    }
}