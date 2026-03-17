package com.smsrouting.controller;

import com.smsrouting.dto.MessageRequest;
import com.smsrouting.dto.MessageResponse;
import com.smsrouting.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/messages")
    public ResponseEntity<MessageResponse> sendMessage(@Valid @RequestBody MessageRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(MessageResponse.from(messageService.sendMessage(request)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<MessageResponse> getMessageStatus(@PathVariable String id) {
        return messageService.getMessage(id)
                .map(MessageResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/optout/{phoneNumber}")
    public ResponseEntity<Map<String, String>> optOut(@PathVariable String phoneNumber) {
        try {
            messageService.optOutNumber(phoneNumber);
            Map<String, String> response = Map.of("phoneNumber", phoneNumber, "status", "opted out");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
