package com.smsrouting.service;

import com.smsrouting.dto.MessageRequest;
import com.smsrouting.model.Message;
import com.smsrouting.model.MessageStatus;
import com.smsrouting.model.MessageType;
import com.smsrouting.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository repository;
    private final CarrierService carrierService;

    public MessageService(MessageRepository repository, CarrierService carrierService) {
        this.repository = repository;
        this.carrierService = carrierService;
    }

    public Message sendMessage(MessageRequest request) {
        return sendMessage(request.getDestinationNumber(), request.getContent(), request.getFormat(), request.getSendAt());
    }

    public Message sendMessage(String destinationNumber, String content, MessageType format) {
        return sendMessage(destinationNumber, content, format, null);
    }

    public Message sendMessage(String destinationNumber, String content, MessageType format, Long sendAt) {
        String standardisedNumber = carrierService.standardiseNumber(destinationNumber);

        if (!carrierService.isValidPhoneNumber(standardisedNumber)) {
            throw new IllegalArgumentException("Phone number: " + destinationNumber + " is invalid");
        }

        if (repository.isOptedOut(standardisedNumber)) {
            return createBlockedMessage(destinationNumber, content, format, sendAt);
        }

        return createAndSendMessage(destinationNumber, content, format, sendAt);
    }

    public Optional<Message> getMessage(String id) {
        return repository.findMessageById(id);
    }

    public void optOutNumber(String phoneNumber) {
        String standardisedNumber = carrierService.standardiseNumber(phoneNumber);
        repository.optOut(standardisedNumber);
    }

    /*
        Helper methods
     */
    private Message createAndSendMessage(String destinationNumber, String content, MessageType format, Long sendAt) {
        Message message = new Message(destinationNumber, content, format, sendAt);

        message.setCarrier(carrierService.determineCarrier(destinationNumber));

        repository.save(message);
        simulateSend(message);

        return message;
    }

    private Message createBlockedMessage(String destinationNumber, String content, MessageType format, Long sendAt) {
        Message message = new Message(destinationNumber, content, format, sendAt);
        message.setCarrier(carrierService.determineCarrier(destinationNumber));
        message.setStatus(MessageStatus.BLOCKED);

        repository.save(message);

        return message;
    }

    private void simulateSend(Message message) {
        Long sendAt = message.getSendAt();

        if (sendAt != null && sendAt > Instant.now().getEpochSecond()) {
            // sendAt is in the future, schedule the message
            message.setStatus(MessageStatus.SCHEDULED);
        } else {
            // sendAt is null or in the past, deliver immediately
            message.setStatus(MessageStatus.SENT);
            message.setStatus(MessageStatus.DELIVERED);
        }
    }
}
