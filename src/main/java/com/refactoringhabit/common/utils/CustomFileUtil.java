package com.refactoringhabit.common.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CustomFileUtil {

    @Value("${file.storage-path}")
    private String filePath;

    @Value("${file.member-default-img}")
    private String fileName;

    public String saveFile(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            setFileName(file);
            this.filePath = this.filePath + File.separator + this.fileName;
            file.transferTo(new File(this.filePath));
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
