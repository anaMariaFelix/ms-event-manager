package com.anamariafelix.ms_event_manager.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @EqualsAndHashCode @ToString
public class UserCreateDTO {

    private String id;

    @NotBlank(message = "Provide an email!")
    @Email(message = "Invalid email format!", regexp = "^[a-z0-9.+-]+@[a-z0-9.+-]+\\.[a-z]{2,}$")
    private String email;

    @NotNull(message = "The Date must be informed")
    @Size(min = 6, max = 6, message = "The password must contain at least 6 and a maximum of 6 digits")
    private String password;

}
