package com.dhl.assetmanager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@AllArgsConstructor
@Getter
public class UserRegistrationRequest implements Serializable {

    @Size(min = 3)
    @NotBlank(message = "Nazwa użytkownika nie może być pusta")
    private final String username;
    @Pattern(regexp = ".+@dhl.com")
    @NotBlank(message = "Email nie może być pusty")
    private final String email;
    @Size(min = 8, max = 128)
    @NotBlank(message = "Hasło nie może być puste")
    private final String password;

}
