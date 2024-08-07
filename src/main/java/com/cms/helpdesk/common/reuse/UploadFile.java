package com.cms.helpdesk.common.reuse;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadFile {

    public static void upload(String fileName, String filePath, MultipartFile file) throws IOException {
        
        Path uploadPath = Paths.get(filePath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePathWithName = uploadPath.resolve(fileName);

        file.transferTo(filePathWithName.toFile());
    }

}
