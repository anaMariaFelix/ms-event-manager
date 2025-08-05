package com.anamariafelix.ms_event_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @Size(min = 8, max = 8,  message = "The CEP must contain exactly 8 digits")
    private String cep;
}
