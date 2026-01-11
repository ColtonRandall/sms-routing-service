package com.smsrouting.service;

import com.smsrouting.model.Message;
import com.smsrouting.model.MessageStatus;
import com.smsrouting.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository repository;
    private final CarrierService carrierService;

    public MessageService(MessageRepository repository, CarrierService carrierService) {
        this.repository = repository;
        this.carrierService = carrierService;
    }

    public Message sendMessage(String destinationNumber, String content, String format){
        String standardisedNumber = carrierService.standardiseNumber(destinationNumber);

        if(!carrierService.isValidPhoneNumber(standardisedNumber)){
            throw new IllegalArgumentException("Phone number: " + destinationNumber + " is invalid");
        }

        if(repository.isOptedOut(standardisedNumber)){
            return createBlockedMessage(destinationNumber, content, format);
        }

        return createAndSendMessage(destinationNumber, content, format);
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
    private Message createAndSendMessage(String destinationNumber, String content, String format) {
        Message message = new Message(destinationNumber, content, format);

        message.setCarrier(carrierService.determineCarrier(destinationNumber));

        repository.save(message);
        simulateSend(message);

        return message;
    }

    private Message createBlockedMessage(String destinationNumber, String content, String format) {
        Message message = new Message(destinationNumber, content, format);
        message.setCarrier(carrierService.determineCarrier(destinationNumber));
        message.setStatus(MessageStatus.BLOCKED);

        repository.save(message);

        return message;
    }

    private void simulateSend(Message message) {
        // simulate carrier api call
        message.setStatus(MessageStatus.SENT);

        // simulate immediate delivery of message
        // note - in production, the status would be set by carrier callback/webhook asynchronously
        message.setStatus(MessageStatus.DELIVERED);
    }
}
