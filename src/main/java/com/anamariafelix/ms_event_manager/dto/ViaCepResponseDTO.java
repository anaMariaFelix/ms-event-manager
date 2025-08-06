package com.anamariafelix.ms_event_manager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ViaCepResponseDTO {

    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;

    @JsonProperty("erro")
    private Boolean erro;

}