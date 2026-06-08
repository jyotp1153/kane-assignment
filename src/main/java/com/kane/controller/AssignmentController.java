package com.kane.controller;

import com.kane.dto.res.person.PersonResponseDto;
import com.kane.service.AssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file-service")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Operation(summary = "Upload TXT file and generate CSV")
    @PostMapping(value = "/process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> process(
            @Parameter(description = "Upload Input TXT File")
            @RequestParam("file")
            MultipartFile file) {
        return assignmentService.processFile(file);
    }

    @Operation(summary = "Upload TXT file and see all valid detail")
    @PostMapping(value = "/getAllValidDetail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<PersonResponseDto>> getAllValidDetail(
            @Parameter(description = "Upload Input TXT File")
            @RequestParam("file")
            MultipartFile file) {
        return ResponseEntity.ok(assignmentService.getAllPersonDetail(file));
    }
}