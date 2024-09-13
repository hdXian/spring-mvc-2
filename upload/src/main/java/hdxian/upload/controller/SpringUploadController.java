package hdxian.upload.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/spring")
public class SpringUploadController {

    @Value("${file.dir}") // use lib of org.springframework (not lombok)
    private String fileDir;

    @GetMapping("/upload")
    public String uploadForm() {
        return "/upload-form";
    }

    @PostMapping("/upload")
    public String saveFile(@RequestParam("itemName") String itemName,
                           @RequestParam("file")MultipartFile file,
                           HttpServletRequest request) throws IOException {

        log.info("request = {}", request);
        log.info("itemName = {}", itemName);
        log.info("file = {}", file);

        if (!file.isEmpty()) {
            String fullPath = fileDir + file.getOriginalFilename();
            log.info("save file: fullPath = {}", fullPath);
            file.transferTo(new File(fullPath)); // transferTo(): throws IOException
        }

        return "redirect:/spring/upload";
    }

}
