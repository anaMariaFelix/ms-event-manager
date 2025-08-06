package com.anamariafelix.ms_event_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @EqualsAndHashCode @ToString
public class EventCreateDTO {

    private String id;

    @NotBlank(message = "The Event name must be informed")
    private String eventName;

    @NotNull(message = "The Date must be informed")
    private LocalDateTime dateTime;

    @NotBlank(message = "The Cep must be informed")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "The CEP must be in the format 99999-999 or 99999999")
    private String cep;
}
