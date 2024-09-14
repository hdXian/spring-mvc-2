package hdxian.upload.domain;

import lombok.Data;

import java.util.List;

@Data // Getter, Setter, RequiredArgsConstructor, ToString, EqualsAndHashCode
public class Item {
    private Long id;
    private String itemName;
    private UploadFile attachFile; // 첨부파일
    private List<UploadFile> imageFiles; // 이미지 파일 (여러장 저장 가능)
}
