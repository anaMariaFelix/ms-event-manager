package com.anamariafelix.ms_event_manager.controller.docs;

import com.anamariafelix.ms_event_manager.controller.exception.ErrorMessage;
import com.anamariafelix.ms_event_manager.dto.EventCreateDTO;
import com.anamariafelix.ms_event_manager.dto.EventResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface EventControllerDocs {

    @Operation(summary = "Create a new Event", description = "Resources for creating a new Event.",
            tags = {"Event"},
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Resource created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponseDTO.class))),

                    @ApiResponse(responseCode = "422", description = "Appeal not processed due to lack of data or invalid data",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),

                    @ApiResponse(responseCode = "400", description = "Invalid zip code.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),

                    @ApiResponse(responseCode = "409", description = "Event already exists.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),

            })
    ResponseEntity<EventResponseDTO> create(EventCreateDTO eventCreateDTO);

    @Operation(summary = "Find a Event by Id", description = "Resources to find a Event by ID.",
            tags = {"Event"},
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully located",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = EventResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Ticket not found.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = EventResponseDTO.class))),
            })
    ResponseEntity<EventResponseDTO> findById(@PathVariable String id);

    @Operation(summary = "Find all Events", description = "Resources to find all Events.",
            tags = {"Event"},
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully located",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = EventResponseDTO.class))),
            })
    ResponseEntity<List<EventResponseDTO>> findAll();
}
