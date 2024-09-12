package hdxian.upload.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {

    @Value("${file.dir}") // use lib of org.springframework (not lombok)
    private String fileDir;

    @GetMapping("/upload")
    public String uploadForm() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
        log.info("request = {}", request);

        String itemName = request.getParameter("itemName");
        log.info("itemName = {}", itemName);

        Collection<Part> parts = request.getParts(); // parts of http multi-part form message
        log.info("parts = {}", parts);

        for (Part part : parts) {
            log.info("==== PART ====");
            log.info("name = {}", part.getName());

            Collection<String> headerNames = part.getHeaderNames();
            for (String headerName : headerNames) {
                log.info("header {}: {}", headerName, part.getHeader(headerName));
            }

            // get submitted File Name (utility. Originally, the content-disposition header must be parsed.)
            log.info("submittedFileName = {}", part.getSubmittedFileName());
            log.info("size = {}", part.getSize()); // part body size

            InputStream inputStream = part.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8); // parse binary data to string. (not seems good... just for study)
            log.info("body = {}", body);

            if (StringUtils.hasText(part.getSubmittedFileName())) {
                String fullPath = fileDir + part.getSubmittedFileName(); // dir path + filename (not recommended. file path can be duplicated.)
                log.info("save file: fullPath = {}", fullPath);
                part.write(fullPath); // save file
            }

        }

        return "upload-form";
    }

}
