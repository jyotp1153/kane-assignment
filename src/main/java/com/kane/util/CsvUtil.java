package com.kane.util;

import com.kane.dto.PersonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CsvUtil {
    public PersonDto parsePerson(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = line.split(",");
        if (parts.length < 4) {
            return null;
        }
        String name = parts[0].trim();
        Integer age = null;
        String category = null;
        if (!parts[1].trim().isEmpty()) {
            try {
                age = Integer.parseInt(parts[1].trim());
                category = age < 18 ? "Kid" : "Adult";
            } catch (NumberFormatException ex) {
                age = null;
            }
        }
        String email = parts[parts.length - 1].trim();
        StringBuilder address = new StringBuilder();
        for (int i = 2;
             i < parts.length - 1; i++) {
            address.append(parts[i].trim());
            if (i != parts.length - 2) {
                address.append(", ");
            }
        }
        return new PersonDto(
                name,
                age,
                address.toString(),
                email,
                category);
    }
}
