package hdxian.upload.domain;

import lombok.Data;

@Data // Getter, Setter, RequiredArgsConstructor, ToString, EqualsAndHashCode
public class UploadFile {

    private String UploadFileName; // 사용자가 올린 파일명
    private String storeFileName; // 스토리지에 저장될 파일명 (uuid + ext)

    public UploadFile(String uploadFileName, String storeFileName) {
        UploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

}
