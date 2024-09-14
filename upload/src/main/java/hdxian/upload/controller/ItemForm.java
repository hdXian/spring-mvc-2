package hdxian.upload.controller;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// will be used in @ModelAttribute
@Data
public class ItemForm {

    private Long itemId;
    private String itemName;

    // MultipartFile can be used in @ModelAttribute
    private MultipartFile attachFile;
    private List<MultipartFile> imageFiles;
}
