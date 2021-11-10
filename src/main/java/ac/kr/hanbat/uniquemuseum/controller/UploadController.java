package ac.kr.hanbat.uniquemuseum.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

    @Value("${ac.kr.hanbat.upload.path}") // application.properties의 변수
    private String uploadPath;

    @PreAuthorize("permitAll()")
    @PostMapping("/uploadAjax")
    public void uploadFile(MultipartFile[] uploadFiles) { // 파일 업로드 예시
        for(MultipartFile file : uploadFiles) {

            // 이미지 파일만 업로드 가능(검사)
            if(file.getContentType().startsWith("image") == false) {
                log.warn("this file is not image type");
                return;
            }
            
            // 실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로
            String originalName = file.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            log.info("fileName: " + fileName);

            // 날짜 폴더 생성
            String folderPath = makeFolder();

            // UUID 생성
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름 중간에 "_"를 이용해서 구분
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            Path savePath = Paths.get(saveName);

            try {
                file.transferTo(savePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);

        // make folder ------
        File uploadPathFolder = new File(uploadPath, folderPath);

        if(uploadPathFolder.exists() == false) uploadPathFolder.mkdirs();
        log.info("bbbbbbbbbbbb: " + folderPath);
        return folderPath;
    }
}
