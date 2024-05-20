package com.refactoringhabit.common.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class CustomFileUtil {

    @Value("${file.storage-path}")
    private String filePath;

    @Value("${file.member-default-img}")
    private String fileName;

    public String saveFile(Optional<MultipartFile> file) throws IOException {
        if (!file.isEmpty()) {
            setFileName(file.get());
            file.get().transferTo(
                new File(this.filePath + File.separator + this.fileName));
        }
        return this.fileName;
    }

    public void setFileName(MultipartFile file) {
        if (!file.isEmpty()) {
            this.fileName = new SimpleDateFormat("SSSssmmHHddMMyy")
                .format(System.currentTimeMillis())
                + file.getOriginalFilename();
        }
    }
}
