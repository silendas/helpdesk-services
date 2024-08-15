package com.cms.helpdesk.attachments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentRes {

    private Long id;
    private String filename;
    private String filetype;
    private String fileurl;
}
