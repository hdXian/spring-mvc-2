package hdxian.itemservice.web.validation;

import hdxian.itemservice.domain.item.Item;
import hdxian.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor // generate constructor with final properties
public class ValidationItemControllerV1 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "/validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/validation/v1/item";
    }

    @GetMapping("/add")
    public String addItemForm(Model model) {
        // add empty Item for th:object
        model.addAttribute("item", new Item());
        return "/validation/v1/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute("item") Item item, RedirectAttributes redirectAttributes, Model model) {

        // add validation logics (field errors)
        Map<String, String> errors = new HashMap<>();

        if (!StringUtils.hasText(item.getItemName())) { // if item name is empty
            errors.put("ItemName", "상품명은 필수로 기입해야 합니다.");
        }

        if (item.getPrice() == null || item.getPrice() > 1000000 || item.getPrice() < 1000) {
            errors.put("price", "가격은 1,000원 ~ 1,000,000원까지 허용됩니다.");
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999 || item.getQuantity() < 1) {
            errors.put("quantity", "수량은 1 ~ 9999까지 허용됩니다.");
        }

        if (item.getPrice() != null || item.getQuantity() != null) {
            int totalPrice = item.getPrice() * item.getQuantity();
            if (totalPrice < 10000) {
                errors.put("globalError", "가격 * 수량은 10,000 원 이상이어야 합니다. 현재 값: " + totalPrice);
            }
        }

        if (!(errors.isEmpty())) {
//            model.addAttribute("item", item); // item already added by @ModelAttribute
            for (String errorKey : errors.keySet()) {
                log.info("error {} occurred: {}", errorKey, errors.get(errorKey));
            }
            model.addAttribute("errors", errors);
            return "/validation/v1/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        // /basic/items/1?status=true
        return "redirect:/validation/v1/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String editItem(@PathVariable("itemId") Long itemId, @ModelAttribute("item") Item updateParam) {
        itemRepository.updateItem(itemId, updateParam);
        // use @PathVariable in redirect url
        return "redirect:/validation/v1/items/{itemId}";
    }


}
