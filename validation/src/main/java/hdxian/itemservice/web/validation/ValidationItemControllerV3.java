package hdxian.itemservice.web.validation;

import hdxian.itemservice.domain.item.AddCheck;
import hdxian.itemservice.domain.item.Item;
import hdxian.itemservice.domain.item.ItemRepository;
import hdxian.itemservice.domain.item.UpdateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor // generate constructor with final properties
public class ValidationItemControllerV3 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "/validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/validation/v3/item";
    }

    @GetMapping("/add")
    public String addItemForm(Model model) {
        // add empty Item for th:object
        model.addAttribute("item", new Item());
        return "/validation/v3/addForm";
    }

//    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("bindingResult.getObjectName() = {}", bindingResult.getObjectName());
        log.info("bindingResult.getTarget() = {}", bindingResult.getTarget());

        validateTotalPriceMin(item, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/validation/v3/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true); // put into query parameter

        // /validation/v3/items/1?status=true
        return "redirect:/validation/v3/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV2(@Validated(AddCheck.class) @ModelAttribute("item") Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("bindingResult.getObjectName() = {}", bindingResult.getObjectName());
        log.info("bindingResult.getTarget() = {}", bindingResult.getTarget());

        validateTotalPriceMin(item, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/validation/v3/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true); // put into query parameter

        // /validation/v3/items/1?status=true
        return "redirect:/validation/v3/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/validation/v3/editForm";
    }

//    @PostMapping("/{itemId}/edit")
    public String editItem(@PathVariable("itemId") Long itemId, @Validated @ModelAttribute("item") Item updateParam, BindingResult bindingResult) {

        validateTotalPriceMin(updateParam, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/validation/v3/editForm";
        }

        itemRepository.updateItem(itemId, updateParam);
        // use @PathVariable in redirect url
        return "redirect:/validation/v3/items/{itemId}";
    }

    @PostMapping("/{itemId}/edit")
    public String editItemV2(@PathVariable("itemId") Long itemId, @Validated(UpdateCheck.class) @ModelAttribute("item") Item updateParam, BindingResult bindingResult) {

        validateTotalPriceMin(updateParam, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/validation/v3/editForm";
        }

        itemRepository.updateItem(itemId, updateParam);
        // use @PathVariable in redirect url
        return "redirect:/validation/v3/items/{itemId}";
    }

    private static void validateTotalPriceMin(Item item, BindingResult bindingResult) {
        // object error logic
        if (item.getPrice() != null && item.getQuantity() != null) {
            int totalPrice = item.getPrice() * item.getQuantity();
            if (totalPrice < 10000) {
                // reject(String errorCode, Object[] errorArgs, String defaultMessage);
                bindingResult.reject("totalPriceMin", new Object[]{10000, totalPrice}, null);
            }
        }
    }


}
