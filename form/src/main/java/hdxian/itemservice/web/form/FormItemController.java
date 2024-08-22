package hdxian.itemservice.web.form;

import hdxian.itemservice.domain.item.Item;
import hdxian.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor // generate constructor with final properties
public class FormItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        // put item list into model (maybe Map)
        model.addAttribute("items", items);
        // return logical view name (resolved by viewResolver)
        return "/form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/form/item";
    }

    @GetMapping("/add")
    public String addItemForm() {
        return "/form/addForm";
    }

    // use RedirectAttributes
    @PostMapping("/add")
    public String addItemV6(@ModelAttribute("item") Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        // use redirectAttribute as a parameter (also encoded in auto)
        // the other params generated as query parameter
        return "redirect:/form/items/{itemId}";
        // ex. itemId = 1 -> redirect url is: /basic/items/1?status=true
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String editItem(@PathVariable("itemId") Long itemId, @ModelAttribute("item") Item updateParam) {
        itemRepository.updateItem(itemId, updateParam);
        // it's able to use @PathVariable at redirect url
        return "redirect:/form/items/{itemId}";
    }

}
