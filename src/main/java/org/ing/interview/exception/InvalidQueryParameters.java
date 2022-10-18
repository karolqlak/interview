package org.ing.interview.exception;


import lombok.Value;

import java.util.List;

@Value
public class InvalidQueryParameters extends RuntimeException {
    private List<String> errorCodes;
}
