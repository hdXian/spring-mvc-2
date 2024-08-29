package hdxian.itemservice.validation;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

@Slf4j
public class MessageCodesResolverTest {

    // able to add custom MessageCodesResolver because it's Interface.
    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject() {
        // resolveMessageCodes(String errorCode, String objectName);
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");

        for (String messageCode : messageCodes)
            log.info("Object messageCode = {}", messageCode);

        /* generated messageCodes
         * [errorCode].[objectName]
         * [errorCode]
         */
        Assertions.assertThat(messageCodes).containsExactly("required.item", "required");
        // bindingResult.reject(errorCode, args, defaultMessage)
        // -> errorCodes = codesResolver.resolverMessageCodes(errorCode, objectName)
        // -> objErr = new ObjectError(objectName, errorCodes, ...)
        // -> this.addError(objErr) ...
    }

    @Test
    void messageCodesResolverField() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);

        for (String messageCode : messageCodes)
            log.info("Field messageCode = {}", messageCode);

        /* generated messageCodes
         * [errorCode].[objectName].[fieldName]
         * [errorCode].[fieldName]
         * [errorCode].[fieldType]
         * [errorCode]
         */
        Assertions.assertThat(messageCodes).containsExactly("required.item.itemName", "required.itemName", "required.java.lang.String", "required");
    }

}
