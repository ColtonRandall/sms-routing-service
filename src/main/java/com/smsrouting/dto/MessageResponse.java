package com.smsrouting.dto;

import com.smsrouting.model.Carrier;
import com.smsrouting.model.Message;
import com.smsrouting.model.MessageStatus;
import com.smsrouting.model.MessageType;

public class MessageResponse {

    private String id;
    private String destinationNumber;
    private String content;
    private MessageType format;
    private MessageStatus status;
    private Carrier carrier;
    private Long sendAt;


    public MessageResponse(String id, String destinationNumber, String content, MessageType format, MessageStatus status, Carrier carrier, Long sendAt) {
        this.id = id;
        this.destinationNumber = destinationNumber;
        this.content = content;
        this.format = format;
        this.status = status;
        this.carrier = carrier;
        this.sendAt = sendAt;
    }

    /*
        factory method that converts a Message entity into a MessageResponse DTO.
     */
    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getDestinationNumber(),
                message.getContent(),
                message.getFormat(),
                message.getStatus(),
                message.getCarrier(),
                message.getSendAt()
        );
    }

    public String getId() {
        return id;
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public String getContent() {
        return content;
    }

    public MessageType getFormat() {
        return format;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public Long getSendAt() {
        return sendAt;
    }
}

