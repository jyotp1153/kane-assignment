package com.kane.service;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AssignmentService {

    ResponseEntity<Resource> processFile(
            MultipartFile file);
}
