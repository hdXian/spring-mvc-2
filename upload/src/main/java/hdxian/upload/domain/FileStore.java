package hdxian.upload.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component // will be DI
public class FileStore {

    @Value("${file.dir}")
    private String fileDir; // includes "/" at the end

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    // MultipartFile로 넘어온 파일 정보를 스토리지에 저장 (경로를 담은 UploadFile 리턴)
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            log.error("[FileStore] multipartFile is empty");
            return null;
        }

        // UploadFile.uploadFileName (업로드한 파일의 원래 이름) 생성
        String originalFilename = multipartFile.getOriginalFilename();

        // UploadFile.storeFileName (스토리지에 저장할 파일 이름) 생성
        String storeFileName = createStoreFileName(originalFilename);

        // 파일 저장
        String fullPath = getFullPath(storeFileName);
        log.info("[FileStore] save file in storage={}", fullPath);
        multipartFile.transferTo(new File(fullPath));

        UploadFile result = new UploadFile(originalFilename, storeFileName);
        log.info("[FileStore] uploadFile created: {}", result);
        return result;
    }

    // MultipartFile을 리스트로 받아 모두 저장 (UploadFile 리스트 리턴)
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> result = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                UploadFile uploadFile = storeFile(multipartFile); // save file in storage and returns UploadFile
                result.add(uploadFile);
            }
        }
        return result;
    }

    // <uuid>.<ext>
    private static String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // abcabc.png -> png
    private static String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }


}
