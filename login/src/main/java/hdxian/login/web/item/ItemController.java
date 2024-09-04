package hdxian.login.web.item;

import hdxian.login.domain.item.Item;
import hdxian.login.domain.item.ItemRepository;
import hdxian.login.web.item.form.ItemAddForm;
import hdxian.login.web.item.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Slf4j
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor // generate constructor with final properties
public class ItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "/items/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/items/items";
    }

    @GetMapping("/add")
    public String addItemForm(Model model) {
        // add empty Item for th:object
        model.addAttribute("item", new Item());
        return "/items/addForm";
    }

    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemAddForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("bindingResult.getObjectName() = {}", bindingResult.getObjectName());
        log.info("bindingResult.getTarget() = {}", bindingResult.getTarget());

        validateTotalPriceMin(form, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/items/addForm";
        }

        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true); // became query parameter

        return "redirect:/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/items/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String editItem(@PathVariable("itemId") Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm updateForm, BindingResult bindingResult) {
        log.info("bindingResult.getObjectName() = {}", bindingResult.getObjectName());
        log.info("bindingResult.getTarget() = {}", bindingResult.getTarget());

        // when itemId not equals between URL and Request parameter
        // ex) URL is ".../3/edit" but updateForm.getId() is 4
        if (itemId != updateForm.getId().longValue()) {
            bindingResult.reject("invalidItemId");
        }

        validateTotalPriceMin(updateForm, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/items/editForm";
        }

        Item itemParam = new Item();
        itemParam.setItemName(updateForm.getItemName());
        itemParam.setPrice(updateForm.getPrice());
        itemParam.setQuantity(updateForm.getQuantity());

        itemRepository.updateItem(itemId, itemParam);
        // use @PathVariable in redirect url
        return "redirect:/items/{itemId}";
    }


    private static void validateTotalPriceMin(ItemAddForm addForm, BindingResult bindingResult) {
        // object error logic
        if (addForm.getPrice() != null && addForm.getQuantity() != null) {
            int totalPrice = addForm.getPrice() * addForm.getQuantity();
            if (totalPrice < 10000) {
                // reject(String errorCode, Object[] errorArgs, String defaultMessage);
                bindingResult.reject("totalPriceMin", new Object[]{10000, totalPrice}, null);
            }
        }
    }


    // overloading
    private static void validateTotalPriceMin(ItemUpdateForm updateForm, BindingResult bindingResult) {
        // object error logic
        if (updateForm.getPrice() != null && updateForm.getQuantity() != null) {
            int totalPrice = updateForm.getPrice() * updateForm.getQuantity();
            if (totalPrice < 10000) {
                // reject(String errorCode, Object[] errorArgs, String defaultMessage);
                bindingResult.reject("totalPriceMin", new Object[]{10000, totalPrice}, null);
            }
        }
    }


}
