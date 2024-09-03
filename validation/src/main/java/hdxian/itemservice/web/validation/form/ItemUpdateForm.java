package hdxian.itemservice.web.validation.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

// for binding html form data to update item
@Data // contains @Getter, @Setter, @RequiredArgsConstructor, @EqualsAndHashCode, @ToString
public class ItemUpdateForm {

    @NotNull
    private Integer id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    private Integer quantity; // has no limit when update

}
