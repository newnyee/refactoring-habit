package com.refactoringhabit.common.utils;

import static com.refactoringhabit.member.domain.enums.MemberType.MEMBER;

import com.refactoringhabit.member.domain.enums.MemberType;
import com.refactoringhabit.member.domain.exception.FileSaveFailedException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
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
    private String memberFileName;

    @Value("${file.host-default-img}")
    private String hostFileName;

    public String saveProfileImage(Optional<MultipartFile> file, MemberType type) {
        if (file.isPresent()) {
            String getFileName = setFileName(file.get());
            try {
                file.get().transferTo(
                    new File(this.filePath + File.separator + getFileName));
            } catch (IOException e) {
                log.error("[{}] {}", e.getClass().getSimpleName(), e.getMessage());
                throw new FileSaveFailedException();
            }
            return getFileName;
        }

        if (type.equals(MEMBER)) {
            return this.memberFileName;
        }
        return this.hostFileName;
    }

    public String setFileName(MultipartFile file) {
        return new SimpleDateFormat("SSSssmmHHddMMyy")
            .format(System.currentTimeMillis())
            + file.getOriginalFilename();
    }

    public String saveImageFiles(List<MultipartFile> files) {
        StringJoiner joiner = new StringJoiner("|");
        files.forEach(file -> {
            String getFileName = setFileName(file);
            joiner.add(getFileName);
            try {
                file.transferTo(new File(this.filePath + File.separator + getFileName));
            } catch (IOException e) {
                log.error("[{}] ex", e.getClass().getSimpleName(), e);
                throw new FileSaveFailedException();
            }
        });
        return joiner.toString();
    }
}
