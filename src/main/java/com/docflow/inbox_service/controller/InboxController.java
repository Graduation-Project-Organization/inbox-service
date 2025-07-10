package com.docflow.inbox_service.controller;

import com.docflow.inbox_service.dto.*;
import com.docflow.inbox_service.entity.Message;
import com.docflow.inbox_service.service.InboxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Inbox Microservice REST API",
        description = "Operations for inbox messages (Create, Fetch, Fetch all received, Fetch all sent, Delete)"
)
@ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class InboxController {
    private InboxService inboxService;

    @Operation(
            summary = "Create Message REST API",
            description = "You determine the receiver and the message content",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "The message created successfully"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "There is unexpected error occurred"
                    )
            }
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createMessage(@Valid @RequestBody MessageRequestDto messageRequestDto) {
        if (inboxService.createMessage(messageRequestDto)) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseDto("201", "The message created successfully"));
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto("500", "An unexpected error occurred"));
        }
    }


    @Operation(
            summary = "Get Message REST API",
            description = "to get single message using the ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The message fetched successfully"
                    )
            }
    )
    @GetMapping("/get")
    public ResponseEntity<MessageResponseDto> getMessage( @RequestParam @Size(min = 24, max = 24) String messageId) {
        // fetch the received messages
        MessageResponseDto messageResponseDto = inboxService.getMessage(messageId);
        // return the response
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(messageResponseDto);
    }



    @Operation(
            summary = "Get All Sent Messages REST API",
            description = "to get all the sent messages",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The messages fetched successfully"
                    )
            }
    )
    @GetMapping("/inbox")
    public ResponseEntity<PageResponse<MessageSummaryResponseDto>> getAllMessages(
            @RequestParam(required = true) @Pattern(regexp = "sent|received", message = "value must be 'sent' or 'received'") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") @Pattern(regexp = "^(asc|desc)$", message = "Invalid sort value. Only 'asc' or 'desc' are allowed.") String sort
    ) {
        // configure pagination settings
        Sort.Direction direction = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, Math.min(size, 50), Sort.by(direction, "createdAt"));

        // fetch messages
        PageResponse<MessageSummaryResponseDto> messages = inboxService.getAllMessages(status, pageable);

        // return the response
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(messages);
    }

    @Operation(
            summary = "Delete Message REST API",
            description = "delete the message using the message ID",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Message deleted successfully."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "There is unexpected error occurred"
                    )
            }
    )
    @DeleteMapping("/inbox")
    public ResponseEntity<Void> deleteMessage(@RequestBody List<String> ids) {
        inboxService.deleteMessage(ids);
        return ResponseEntity.noContent().build();
    }
}
