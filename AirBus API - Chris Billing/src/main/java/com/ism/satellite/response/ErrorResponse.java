package com.ism.satellite.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.springframework.http.HttpStatus;

/**
 * Constructs the Error response object used in the GlobalControllerExceptionHandler.
 */
@Value
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    HttpStatus code;

    String title;

    String message;

}
