package com.refactoringhabit.common.utils;

import static com.refactoringhabit.member.domain.enums.MemberType.MEMBER;

import com.refactoringhabit.member.domain.enums.MemberType;
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
    private String memberFileName;

    @Value("${file.host-default-img}")
    private String hostFileName;

    public String saveProfileImage(Optional<MultipartFile> file, MemberType type) throws IOException {

        if (file.isPresent()) {
            String getFileName = setFileName(file.get());
            file.get().transferTo(
                new File(this.filePath + File.separator + getFileName));
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
}
