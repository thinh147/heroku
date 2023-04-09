package com.gogitek.toeictest.controller.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileRequest {
    private String fileName;
    private MultipartFile file;
}
