package com.smsrouting.controller;

import com.smsrouting.model.Message;
import com.smsrouting.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // post messages
    @PostMapping("/messages")
    public ResponseEntity<Message> sendMessage(@RequestBody Map<String, String> request){
        try{
            String destinationNumber = request.get("destinationNumber");
            String content = request.get("content");
            String format = request.getOrDefault("format", "SMS");

            Message message = messageService.sendMessage(destinationNumber, content, format);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    // get messages by id
    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> getMessageStatus(@PathVariable String id){
        return messageService.getMessage(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // opt out management
    @PostMapping("/optout/{phoneNumber}")
    public ResponseEntity<Map<String, String>> optOut(@PathVariable String phoneNumber){
        try{
            messageService.optOutNumber(phoneNumber);
            Map<String, String> response = Map.of("phoneNumber", phoneNumber, "status", "opted out");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
