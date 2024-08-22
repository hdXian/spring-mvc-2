package hdxian.itemservice.domain.item;

import lombok.Data;

import java.util.List;

@Data // contains @Getter, @Setter, @RequiredArgsConstructor, @EqualsAndHashCode, @ToString
public class Item {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    private Boolean open; // 상품 판매 여부
    private List<String> regions; // 판매 지역
    private ItemType itemType; // 상품 종류
    private String deliveryCode; // 배송 방식

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

}
