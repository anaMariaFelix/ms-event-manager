package com.anamariafelix.ms_event_manager.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserResponseDTO {

    private String email;
    private String role;

}
