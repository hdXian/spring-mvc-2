package hdxian.upload.controller;

import hdxian.upload.domain.FileStore;
import hdxian.upload.domain.Item;
import hdxian.upload.domain.ItemRepository;
import hdxian.upload.domain.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    // DI
    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String addItemForm() {
        return "/item-form"; // templates/item-form.html
    }

    @GetMapping("/items/{itemId}")
    public String viewItem(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/item-view";
    }

    @PostMapping("/items/new")
    public String addItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {

        // MultipartFiles (in itemForm) -> save files (by fileStore) -> UploadFiles (just file path)
        MultipartFile attachFile = form.getAttachFile();
        UploadFile attachFileInfo = fileStore.storeFile(attachFile);

        List<MultipartFile> imageFiles = form.getImageFiles();
        List<UploadFile> imageFilesInfo = fileStore.storeFiles(imageFiles);

        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFileInfo);
        item.setImageFiles(imageFilesInfo);
        Item saved = itemRepository.save(item);
        log.info("[ItemController] item saved={}", saved);

        redirectAttributes.addAttribute("itemId", item.getId());
        return "redirect:/items/{itemId}";
    }



}
