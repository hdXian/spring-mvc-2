package hdxian.upload.controller;

import hdxian.upload.domain.FileStore;
import hdxian.upload.domain.Item;
import hdxian.upload.domain.ItemRepository;
import hdxian.upload.domain.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
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

    @GetMapping("/items/{itemId}")
    public String viewItem(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/item-view";
    }

    // fileName is uuid
    @GetMapping("/images/{fileName}")
    public ResponseEntity<Resource> downloadImage(@PathVariable("fileName") String fileName) throws MalformedURLException {
        String ext = FileStore.extractExt(fileName);
        String contentType = "image/" + ext;

        String resourceUrl = "file:" + fileStore.getFullPath(fileName);
        UrlResource imageResource = new UrlResource(resourceUrl);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE,contentType)
                .body(imageResource);
    }

    // find attachFile by itemId (Item domain has UploadFile)
    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttachFile(@PathVariable("itemId") Long itemId) throws MalformedURLException {
        Item item = itemRepository.findById(itemId);

        // 파일명 (사용자 저장, 스토리지 저장) 가져오기
        String uploadFileName = item.getAttachFile().getUploadFileName(); // 사용자가 저장했던 파일명
        String storeFileName = item.getAttachFile().getStoreFileName(); // 스토리지에 저장된 파일명 (uuid)
        log.info("[ItemController] download attach file={}", uploadFileName);

        // "file:" 붙인 url 만들어서 Resource 생성하기
        String resourceUrl = "file:" + fileStore.getFullPath(storeFileName);
        UrlResource attachFileResource = new UrlResource(resourceUrl);

        // uploadFileName 인코딩하고 헤더에 attachment 추가하기
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(attachFileResource);
    }


}
