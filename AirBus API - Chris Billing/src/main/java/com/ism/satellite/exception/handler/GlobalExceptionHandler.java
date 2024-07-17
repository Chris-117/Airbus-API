package com.ism.satellite.exception.handler;

import com.ism.satellite.exception.custom.BadRequestException;
import com.ism.satellite.exception.custom.NotFoundException;
import com.ism.satellite.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Global Exception handler.
     *
     * @param ex Exception
     * @return ResponseEntity with Error information
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(NotFoundException ex) {

        var errorResponse = ErrorResponse.builder()
          .code(HttpStatus.NOT_FOUND)
          .title("No Satellite found")
          .message(ex.getMessage())
          .build();

        LOGGER.error(ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handles BadRequestException.
     *
     * @param ex Exception
     * @return ResponseEntity with Error information
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(BadRequestException ex) {

        var errorResponse = ErrorResponse.builder()
          .code(HttpStatus.BAD_REQUEST)
          .title("Invalid input parameters")
          .message(ex.getMessage())
          .build();

        LOGGER.error(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles generic Exception.
     *
     * @param ex Exception
     * @return ResponseEntity with Error information
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        var errorResponse = ErrorResponse.builder()
          .code(HttpStatus.INTERNAL_SERVER_ERROR)
          .title("Error Processing Request")
          .message(ex.getMessage())
          .build();

        LOGGER.error(ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
