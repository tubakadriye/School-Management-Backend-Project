package com.project.schoolmanagment.payload.response.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) //only nonnull values will be included
public class ResponseMessage<T> {
    private T object;
    private String message;
    private HttpStatus httpStatus;
}
