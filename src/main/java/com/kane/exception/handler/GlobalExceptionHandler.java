package com.prism_sfa.exception.handler;

import com.kane.constant.ApiErrorCodes;
import com.kane.dto.res.error.ErrorResponse;
import com.kane.exception.BaseException;
import com.kane.exception.NoSuchElementFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;

@RestController
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String TRACE = "trace";

    @Value("${application.config.trace}")
    private boolean printStackTrace;

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFound(
            NoHandlerFoundException ex,
            HttpServletRequest request) {

        String api = request.getMethod() + " " + request.getRequestURI();
        String query = request.getQueryString();

        log.error("❌ INVALID API HIT | API: {} | Query: {} | IP: {}",
                api,
                query,
                request.getRemoteAddr());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("API not found: " + request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ApiErrorCodes.INVALID_INPUT.getErrorCode(), ApiErrorCodes.INVALID_INPUT.getErrorMessage());
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ExceptionHandler(ClientAbortException.class)
    public void handleClientAbortException(ClientAbortException exception) {
        // ClientAbortException means the caller closed the HTTP connection while the server was writing.
        // It is not an application failure, so avoid routing it through the generic 500 handler.
        log.debug("Client aborted response before it was fully written: {}", exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception exception, WebRequest request) {
        log.error("Unknown error occurred", exception);
        return buildErrorResponse(exception, "Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception exception, HttpStatus httpStatus, WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception exception, String message, HttpStatus httpStatus, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);

        if (printStackTrace && isTraceOn(request)) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private ResponseEntity<ErrorResponse> buildCustomErrorResponse(BaseException exception, HttpStatus httpStatus, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getErrorCode(), exception.getMessage());

        if (printStackTrace && isTraceOn(request)) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private boolean isTraceOn(WebRequest request) {
        String[] value = request.getParameterValues(String.valueOf(TRACE));
        return Objects.nonNull(value)
                && value.length > 0
                && value[0].contentEquals("true");
    }

    @ExceptionHandler(NoSuchElementFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NoSuchElementFoundException exception, WebRequest request) {
        log.error("Failed to find the requested element", exception);
        return buildCustomErrorResponse(exception, HttpStatus.NOT_FOUND, request);
    }

}


