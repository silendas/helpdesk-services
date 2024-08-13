package com.cms.helpdesk.attachments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.helpdesk.attachments.service.AttachmentService;
import com.cms.helpdesk.common.path.BasePath;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping(value = BasePath.BASE_ATTACHMENTS)
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @GetMapping("/view/{filename}")
    public ResponseEntity<InputStreamResource> viewFile(@PathVariable("filename") String filename) {
        return attachmentService.getFile(filename);
    }

}
