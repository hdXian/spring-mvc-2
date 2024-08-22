package hdxian.itemservice.web.basic;

import hdxian.itemservice.domain.Item;
import hdxian.itemservice.domain.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor // generate constructor with final properties
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        // put item list into model (maybe Map)
        model.addAttribute("items", items);
        // return logical view name (resolved by viewResolver)
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addItemForm() {
        return "basic/addForm";
    }

    // save new item into repository
//    @PostMapping("/add")
    public String addItemV1(@RequestParam("itemName") String itemName,
                            @RequestParam("price") Integer price,
                            @RequestParam("quantity") Integer quantity,
                            Model model)
    {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item"; // show item info after add item
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {

        itemRepository.save(item);

        // functions of @ModelAttribute
        // 1. bind request parameter to object properties
        // 2. put object into model

//        model.addAttribute("item", item);

        return "basic/item"; // show item info after add item
    }

    // it's able to omit name value at @ModelAttribute.
    // default rule of generate attribute name is class name with lowercase first letter.
    // (ex. Item -> item, HelloData -> helloData)
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "basic/item"; // show item info after add item
    }

    // In addition, it's able to omit @ModelAttribute.
//    @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item"; // show item info after add item
    }

    // apply PRG method
//    @PostMapping("/add")
    public String addItemV5(@ModelAttribute("item") Item item) {
        Item savedItem = itemRepository.save(item);
        // redirect to item info page
        return "redirect:/basic/items/" + savedItem.getId();
    }

    // use RedirectAttributes
    @PostMapping("/add")
    public String addItemV6(@ModelAttribute("item") Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        // use redirectAttribute as a parameter (also encoded in auto)
        // the other params generated as query parameter
        return "redirect:/basic/items/{itemId}";
        // ex. itemId = 1 -> redirect url is: /basic/items/1?status=true
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String editItem(@PathVariable("itemId") Long itemId, @ModelAttribute("item") Item updateParam) {
        itemRepository.updateItem(itemId, updateParam);
        // it's able to use @PathVariable at redirect url
        return "redirect:/basic/items/{itemId}";
    }

    @PostConstruct
    public void init() {
        // called after DI
        Item item1 = new Item("testItem1", 10000, 10);
        Item item2 = new Item("testItem2", 20000, 20);
        itemRepository.save(item1);
        itemRepository.save(item2);
    }

}
