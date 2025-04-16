package com.tta.tddrest;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class User {

    Long userId;
    String username;
    String firstName;
    String lastName;

}
