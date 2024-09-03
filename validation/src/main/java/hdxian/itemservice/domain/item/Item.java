package hdxian.itemservice.domain.item;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data // contains @Getter, @Setter, @RequiredArgsConstructor, @EqualsAndHashCode, @ToString
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000") deprecated since jdk 17
public class Item {

    @NotNull
    private Long id;

    @NotBlank(message = "상품명에 공백은 입력할 수 없습니다. (어노테이션 속성)")
    private String itemName;

    @NotNull(message = "가격을 필수로 입력해야 합니다. (어노테이션 속성)")
    @Range(min = 1000, max = 1000000, message = "1000 ~ 1000000 사이의 값을 허용합니다. (어노테이션 속성)")
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
