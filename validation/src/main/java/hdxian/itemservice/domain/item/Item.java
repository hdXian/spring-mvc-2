package hdxian.itemservice.domain.item;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data // contains @Getter, @Setter, @RequiredArgsConstructor, @EqualsAndHashCode, @ToString
public class Item {
    private Long id;

    @NotBlank(message = "상품명에 공백은 입력할 수 없습니다. (어노테이션 속성)")
    private String itemName;

    @NotNull(message = "가격을 필수로 입력해야 합니다. (어노테이션 속성)")
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull(message = "수량을 필수로 입력해야 합니다. (어노테이션 속성)")
    @Max(value = 9999, message = "최대 9999까지 입력할 수 있습니다. (어노테이션 속성)")
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

}
