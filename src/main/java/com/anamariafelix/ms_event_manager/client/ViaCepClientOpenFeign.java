package com.anamariafelix.ms_event_manager.client;

import com.anamariafelix.ms_event_manager.dto.ViaCepResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
public interface ViaCepClientOpenFeign {

    @GetMapping("/{cep}/json/")
    ViaCepResponseDTO findByAddress(@PathVariable("cep") String cep);
}
