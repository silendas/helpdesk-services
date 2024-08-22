package com.cms.helpdesk.attachments.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cms.helpdesk.attachments.model.Attachment;
import com.cms.helpdesk.attachments.repository.AttachmentRepository;
import com.cms.helpdesk.common.path.AttachmentPath;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    public ResponseEntity<InputStreamResource> getFile(String filename) {

        try {
            Attachment attachment = attachmentRepository.findByFilename(filename)
                    .orElseThrow(() -> new IllegalArgumentException("Attachment not found with filename: " + filename));

            File file = new File(AttachmentPath.STORAGE_PATH_ATTACHMENT, filename);
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                    .contentType(MediaType.parseMediaType(attachment.getFileType()))
                    .contentLength(file.length())
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
