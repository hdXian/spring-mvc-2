package hdxian.itemservice.web.form;

import hdxian.itemservice.domain.item.Item;
import hdxian.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor // generate constructor with final properties
public class FormItemController {

    private final ItemRepository itemRepository;

    // add "regions" attribute in Model when any controller called
    // as same as adding "model.addAttribute("regions", regions)" statement in all methods
    @ModelAttribute("regionChoices")
    public Map<String, String> regions() {
        // LinkedHashMap - sequential HashMap
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions; // for display region choices on page
    }

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
    public String addItemForm(Model model) {
        // add empty Item for th:object
        model.addAttribute("item", new Item());
        return "/form/addForm";
    }

    // use RedirectAttributes
    @PostMapping("/add")
    public String addItem(@ModelAttribute("item") Item item, RedirectAttributes redirectAttributes) {
        // parse parameter open=on as true
        // if checkbox is not checked, parameter "open" isn't exist in request.
        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/form/items/{itemId}";
        // /basic/items/1?status=true
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
