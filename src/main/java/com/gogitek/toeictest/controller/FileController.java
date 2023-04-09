package com.gogitek.toeictest.controller;

import com.gogitek.toeictest.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService storage;
    private
    @GetMapping("/{filename:.+}")
    @ResponseBody ResponseEntity<?> getFile(@PathVariable String filename) {
        var file = storage.loadFiles(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
