package com.dhl.assetmanager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@AllArgsConstructor
@Getter
public class UserCredentialsRequest implements Serializable {
    @NotBlank(message = "Nazwa użytkownika nie może być pusta")
    private final String username;
    @Size(min = 8, max = 128)
    @NotBlank(message = "Hasło nie może być puste")
    private final String password;
}
