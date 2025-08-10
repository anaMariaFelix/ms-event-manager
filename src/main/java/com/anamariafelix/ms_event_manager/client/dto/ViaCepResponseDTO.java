package com.anamariafelix.ms_event_manager.client.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ViaCepResponseDTO {

    @JsonAlias("logradouro")
    private String street;

    @JsonAlias("bairro")
    private String neighborhood;

    @JsonAlias("localidade")
    private String city;

    @JsonAlias("uf")
    private String state;

    @JsonAlias("erro")
    private Boolean error;

}