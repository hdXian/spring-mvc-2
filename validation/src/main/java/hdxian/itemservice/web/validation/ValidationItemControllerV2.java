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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
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
    private final ItemValidator itemValidator;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        log.info("init binder = {}", dataBinder);
        dataBinder.addValidators(itemValidator);
    }

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

    // argument BindingResult must be located after @ModelAttribute
//    @PostMapping("/add")
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
//            for (ObjectError error : bindingResult.getAllErrors()) {
//                log.info("error in {} occurred: {}", error.getObjectName(), error.getDefaultMessage());
//            }
            log.info("errors={}", bindingResult);
            return "/validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true); // put into query parameter

        // /validation/v2/items/1?status=true
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // add validation logics (field errors)
        if (!StringUtils.hasText(item.getItemName())) {
            // new FieldError(String objectName, String fieldName, Object rejectedValue, Boolean bindingFailure, String[] codes, Object[] arguments, String defaultMessage)
            // FieldError extends ObjectError
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품명은 필수적으로 기입해야 합니다."));
        }

        if (item.getPrice() == null || item.getPrice() > 1000000 || item.getPrice() < 1000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000원 ~ 1,000,000원까지 허용됩니다."));
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999 || item.getQuantity() < 1) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 1 ~ 9999까지 허용됩니다."));
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int totalPrice = item.getPrice() * item.getQuantity();
            if (totalPrice < 10000) {
                // new ObjectError(String objectName, String[] codes, Object[] arguments, String defaultMessage)
                bindingResult.addError(new ObjectError("item", null, null, "가격 * 수량은 10,000 원 이상이어야 합니다. 현재 값: " + totalPrice));
            }
        }

        // if some errors occurred, render addForm
        if (bindingResult.hasErrors()) {
//            for (ObjectError error : bindingResult.getAllErrors()) {
//                log.info("error in {} occurred: {}", error.getObjectName(), error.getDefaultMessage());
//            }
            log.info("errors={}", bindingResult);
            return "/validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true); // put into query parameter

        // /validation/v2/items/1?status=true
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute("item") Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // add validation logics (field errors)
        if (!StringUtils.hasText(item.getItemName())) {
            // new FieldError(String objectName, String fieldName, Object rejectedValue, Boolean bindingFailure, String[] codes, Object[] arguments, String defaultMessage)
            // FieldError extends ObjectError
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
        }

        if (item.getPrice() == null || item.getPrice() > 1000000 || item.getPrice() < 1000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price", "gdh.item.price"}, new Object[]{1000, 1000000}, null));
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999 || item.getQuantity() < 1) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int totalPrice = item.getPrice() * item.getQuantity();
            if (totalPrice < 10000) {
                // new ObjectError(String objectName, String[] codes, Object[] arguments, String defaultMessage)
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, totalPrice}, null));
            }
        }

        // if some errors occurred, render addForm
        if (bindingResult.hasErrors()) {
//            for (ObjectError error : bindingResult.getAllErrors()) {
//                log.info("error in {} occurred: {}", error.getObjectName(), error.getDefaultMessage());
//            }
            log.info("errors={}", bindingResult);
            return "/validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true); // put into query parameter

        // /validation/v2/items/1?status=true
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV4(@ModelAttribute("item") Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // if typeMismatch occurs, return immediately
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/validation/v2/addForm";
        }

        log.info("bindingResult.getObjectName() = {}", bindingResult.getObjectName());
        log.info("bindingResult.getTarget() = {}", bindingResult.getTarget());

        // add validation logics (field errors)
        if (!StringUtils.hasText(item.getItemName())) {
            // rejectValue(String fieldName, String errorCode);
            bindingResult.rejectValue("itemName", "required");
        }

        if (item.getPrice() == null || item.getPrice() > 1000000 || item.getPrice() < 1000) {
            // rejectValue(String fieldName, String errorCode, Object[] errorArgs, String defaultMessage);
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999 || item.getQuantity() < 1) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int totalPrice = item.getPrice() * item.getQuantity();
            if (totalPrice < 10000) {
                // reject(String errorCode, Object[] errorArgs, String defaultMessage);
                bindingResult.reject("totalPriceMin", new Object[]{10000, totalPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
//            for (ObjectError error : bindingResult.getAllErrors()) {
//                log.info("error in {} occurred: {}", error.getObjectName(), error.getDefaultMessage());
//            }
            log.info("errors={}", bindingResult);
            return "/validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true); // put into query parameter

        // /validation/v2/items/1?status=true
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV5(@ModelAttribute("item") Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        log.info("bindingResult.getObjectName() = {}", bindingResult.getObjectName());
        log.info("bindingResult.getTarget() = {}", bindingResult.getTarget());

        // validate (Object target, Errors errors)
        if (itemValidator.supports(Item.class)){
            itemValidator.validate(item, bindingResult);
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true); // put into query parameter

        // /validation/v2/items/1?status=true
        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute("item") Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // @Validated -  dataBinder will validate object Item

        log.info("bindingResult.getObjectName() = {}", bindingResult.getObjectName());
        log.info("bindingResult.getTarget() = {}", bindingResult.getTarget());

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
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
