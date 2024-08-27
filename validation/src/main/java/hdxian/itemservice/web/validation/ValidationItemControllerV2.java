package hdxian.itemservice.web.validation;

import hdxian.itemservice.domain.item.Item;
import hdxian.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor // generate constructor with final properties
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "/validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/validation/v2/item";
    }

    @GetMapping("/add")
    public String addItemForm(Model model) {
        // add empty Item for th:object
        model.addAttribute("item", new Item());
        return "/validation/v2/addForm";
    }

    @PostMapping("/add")
    public String addItemV1(@ModelAttribute("item") Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // add validation logics (field errors)
        if (!StringUtils.hasText(item.getItemName())) {
            // new FieldError(String objectName, String fieldName, String defaultMessage)
            // FieldError extends ObjectError
            bindingResult.addError(new FieldError("item", "itemName", "상품명은 필수적으로 기입해야 합니다."));
        }

        if (item.getPrice() == null || item.getPrice() > 1000000 || item.getPrice() < 1000) {
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000원 ~ 1,000,000원까지 허용됩니다."));
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999 || item.getQuantity() < 1) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 1 ~ 9999까지 허용됩니다."));
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int totalPrice = item.getPrice() * item.getQuantity();
            if (totalPrice < 10000) {
                // new ObjectError(String objectName, String defaultMessage)
                bindingResult.addError(new ObjectError("item", "가격 * 수량은 10,000 원 이상이어야 합니다. 현재 값: " + totalPrice));
            }
        }

        // if some errors occurred, render addForm again
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.info("error in {} occurred: {}", error.getObjectName(), error.getDefaultMessage());
            }
            return "/validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true); // put into query parameter

        // /validation/v2/items/1?status=true
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String editItem(@PathVariable("itemId") Long itemId, @ModelAttribute("item") Item updateParam) {
        itemRepository.updateItem(itemId, updateParam);
        // use @PathVariable in redirect url
        return "redirect:/validation/v2/items/{itemId}";
    }


}
