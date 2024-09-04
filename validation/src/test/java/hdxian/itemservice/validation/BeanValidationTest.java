package hdxian.itemservice.validation;

import hdxian.itemservice.domain.item.Item;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class BeanValidationTest {

    // just usage of Bean Validations
    @Test
    void beanValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // violate all constraints
        Item item = new Item();
        item.setItemName(" "); // @NotBlank
        item.setPrice(0); // @Range(min=1000, max=1000000)
        item.setQuantity(10000); // @Max(9999)

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        for (ConstraintViolation<Item> violation : violations) {
            System.out.println("violation = " + violation);
            System.out.println("violation = " + violation.getMessage());
        }

    }

}
