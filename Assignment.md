# Kane Java Technical Assignment

## Overview

This project is developed as part of the Kane Solutions Java Technical Assignment.

The application reads a text file containing visa applicant details, validates the records based on the given constraints, categorizes applicants as Kid or Adult, and generates a CSV file containing the filtered results.

---

## Technology Stack

* Java 17
* Spring Boot 3
* Gradle
* Swagger/OpenAPI
* Lombok

---

## Project Structure

```text
com.kane

├── controller
├── service
├── serviceImpl
├── dto
├── util
├── exception
├── config
```

---

## Assignment Requirements Covered

### Question 1

The application performs the following validations:

1. Validates Email Address
2. Removes records with invalid email IDs
3. Removes records with empty addresses
4. Removes records where the address does not belong to India
5. Categorizes applicants based on age:

    * Age >= 18 → Adult
    * Age < 18 → Kid
6. Maintains the original order of records
7. Generates CSV output with headers

### Sample Output

```csv
Name,Category
Helen,Kid
Toshi,Adult
```

---

## API Details

### Upload TXT File

**Endpoint**

```http
POST /file-service/process
```

**Content-Type**

```text
multipart/form-data
```

**Request Parameter**

| Parameter | Type          | Description     |
| --------- | ------------- | --------------- |
| file      | MultipartFile | Input text file |

**Response**

Generated CSV file containing Name and Category.

---

## Running the Application

### Clone Repository

```bash
git clone https://github.com/jyotp1153/kane-assignment.git
```

### Build Project

```bash
./gradlew clean build
```

### Run Application

```bash
./gradlew bootRun
```

---

## Swagger UI

After starting the application:

```text
http://localhost:9052/kane-service/swagger-ui/index.html
```

Steps:

1. Open Swagger UI
2. Select POST /file-service/process
3. Click "Try it out"
4. Upload input TXT file
5. Click Execute
6. Download generated CSV file

---

## Assumptions

* First field is treated as Name
* Second field is treated as Age
* Last field is treated as Email
* All fields between Age and Email are treated as Address
* Records with invalid or missing mandatory data are excluded from output

---

## SQL Solution (Question 2)

```sql
SELECT
    E1.EMPLOYEE_NAME AS FIRST_EMPLOYEE,
    E2.EMPLOYEE_NAME AS SECOND_EMPLOYEE
FROM EMPLOYEE E1
INNER JOIN EMPLOYEE E2
    ON E1.SALARY < E2.SALARY
ORDER BY
    E1.EMPLOYEE_ID ASC,
    E2.SALARY ASC;
```

---

## Author

Jyot Prakash
