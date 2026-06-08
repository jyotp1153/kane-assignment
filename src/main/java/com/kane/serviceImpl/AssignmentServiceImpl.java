package com.kane.serviceImpl;

import com.kane.constant.ApiErrorCodes;
import com.kane.dto.PersonDto;
import com.kane.dto.res.person.PersonResponseDto;
import com.kane.exception.NoSuchElementFoundException;
import com.kane.service.AssignmentService;
import com.kane.util.CsvUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final CsvUtil csvUtil;

    @Override
    public ResponseEntity<Resource> processFile(MultipartFile file) {
        List<PersonResponseDto> outputList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                PersonDto person = csvUtil.parsePerson(line);
                if (person == null) {
                    continue;
                }
                if (!isValidEmail(person.getEmail())) {
                    continue;
                }
                if (!hasAddress(person.getAddress())) {
                    continue;
                }
                if (!isIndian(person.getAddress())) {
                    continue;
                }
                outputList.add(new PersonResponseDto(person.getName(), person.getCategory(), person.getAge(), person.getAddress(), person.getEmail()));
            }

            byte[] csv = generateCsv(outputList);
            ByteArrayResource resource = new ByteArrayResource(csv);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=output.csv").contentType(MediaType.parseMediaType("text/csv")).body(resource);

        } catch (Exception ex) {
            throw new NoSuchElementFoundException(ApiErrorCodes.FILE_PARSING_ERROR.getErrorCode(), ApiErrorCodes.FILE_PARSING_ERROR.getErrorMessage());
        }
    }

    @Override
    public List<PersonResponseDto> getAllPersonDetail(MultipartFile file) {
        return getAllValidResult(file);
    }

    private List<PersonResponseDto> getAllValidResult(MultipartFile file) {
        List<PersonResponseDto> outputList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                PersonDto person = csvUtil.parsePerson(line);
                if (person == null) {
                    continue;
                }
                if (!isValidEmail(person.getEmail())) {
                    continue;
                }
                if (!hasAddress(person.getAddress())) {
                    continue;
                }
                if (!isIndian(person.getAddress())) {
                    continue;
                }
                outputList.add(new PersonResponseDto(person.getName(), person.getCategory(), person.getAge(), person.getAddress(), person.getEmail()));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return outputList;
    }

    private String getCategory(Integer age) {
        return age >= 18 ? "Adult" : "Kid";
    }

    private boolean hasAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }

    private boolean isIndian(String address) {
        return address.toLowerCase().contains("india");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private byte[] generateCsv(List<PersonResponseDto> outputList) {

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Name,Category\n");
        for (PersonResponseDto dto : outputList) {
            csvBuilder.append(dto.getName()).append(",").append(dto.getCategory()).append("\n");
        }
        return csvBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }
}