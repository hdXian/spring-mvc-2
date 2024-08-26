package hdxian.itemservice.web.validation;

import hdxian.itemservice.domain.item.Item;
import hdxian.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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

    // use RedirectAttributes
    @PostMapping("/add")
    public String addItem(@ModelAttribute("item") Item item, RedirectAttributes redirectAttributes) {
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
