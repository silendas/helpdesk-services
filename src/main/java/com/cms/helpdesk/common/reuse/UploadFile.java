package com.cms.helpdesk.common.reuse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public class UploadFile {

    public void upload(MultipartFile multipartFile, String path, String name) throws IOException {
        Path directoryPath = Paths.get(path);

        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        Path filePath = directoryPath.resolve(name);

        multipartFile.transferTo(filePath.toFile());
    }
}
