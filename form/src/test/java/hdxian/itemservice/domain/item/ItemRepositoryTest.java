package hdxian.itemservice.domain.item;

import hdxian.itemservice.domain.Item;
import hdxian.itemservice.domain.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void afterEach() {
        itemRepository.clearStore();
    }

    @Test
    public void save() {
        // given
        Item item = new Item("itemA", 10000, 11);

        // when
        Item savedItem = itemRepository.save(item);

        // then
        Item findItem = itemRepository.findById(item.getId());
        assertThat(findItem).isEqualTo(savedItem);
    }

    @Test
    public void findAll() {
        // given
        Item item1 = new Item("item1", 10000, 11);
        Item item2 = new Item("item2", 20000, 22);
        itemRepository.save(item1);
        itemRepository.save(item2);

        // when
        List<Item> result = itemRepository.findAll();

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(item1, item2);
    }

    @Test
    public void update() {
        // given
        Item item = new Item("item", 10000, 11);
        Item savedItem = itemRepository.save(item);
        Long savedId = savedItem.getId();

        // when
        Item updateParam = new Item("updatedItem", 20000, 222);
        itemRepository.updateItem(savedId, updateParam);

        // then
        Item findItem = itemRepository.findById(savedId);
        assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());
    }

}
