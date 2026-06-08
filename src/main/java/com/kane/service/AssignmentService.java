package com.kane.service;

import com.kane.dto.PersonResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AssignmentService {

    ResponseEntity<Resource> processFile(MultipartFile file);

    List<PersonResponseDto> getAllPersonDetail(MultipartFile file);
}
