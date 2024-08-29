package hdxian.itemservice.web.validation;

import hdxian.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        // isAssignableFrom: include subclasses
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Item item = (Item) target;

        if (!StringUtils.hasText(item.getItemName())) {
            // rejectValue(String fieldName, String errorCode);
            errors.rejectValue("itemName", "required");
        }

        if (item.getPrice() == null || item.getPrice() > 1000000 || item.getPrice() < 1000) {
            // rejectValue(String fieldName, String errorCode, Object[] errorArgs, String defaultMessage);
            errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999 || item.getQuantity() < 1) {
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int totalPrice = item.getPrice() * item.getQuantity();
            if (totalPrice < 10000) {
                // reject(String errorCode, Object[] errorArgs, String defaultMessage);
                errors.reject("totalPriceMin", new Object[]{10000, totalPrice}, null);
            }
        }

    }

}
