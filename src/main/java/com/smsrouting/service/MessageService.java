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
        // validate num first
        if(!carrierService.isValidPhoneNumber(destinationNumber)){
            throw new IllegalArgumentException("Phone number: " + destinationNumber + " is invalid");
        }

        // check if number has opted out
        if(repository.isOptedOut(destinationNumber)){
            return createBlockedMessage(destinationNumber, content, format);
        }

        // send the message
        return createAndSendMessage(destinationNumber, content, format);
    }

    public Optional<Message> getMessage(String id) {
        return repository.findMessageById(id);
    }

    public void optOutNumber(String phoneNumber) {
        repository.optOut(phoneNumber);
    }


    // helper methods
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
        // mock carrier API call
        System.out.println("Sending message " + message.getId() +
                " to " + message.getCarrier() +
                " for " + message.getDestinationNumber());

        message.setStatus(MessageStatus.SENT);
    }

    /*
        NOTE: simulate delivered message - in real/production, it would be set by carrier callback/webhook
     */
    public void simulateDelivery(String messageId) {
        Message message = repository.findMessageById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found with id: " + messageId));

        if (message.getStatus() == MessageStatus.SENT) {
            message.setStatus(MessageStatus.DELIVERED);
            System.out.println("Message " + messageId + " delivered");
        }
    }
}
