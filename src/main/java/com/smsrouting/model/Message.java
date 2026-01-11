package com.smsrouting.model;

import java.util.UUID;

public class Message {
    private String id;
    private String destinationNumber;
    private String content;
    private String format;
    private MessageStatus status;
    private Carrier carrier;

    public Message(String destinationNumber, String content, String format) {
        this.id = UUID.randomUUID().toString();
        this.destinationNumber = destinationNumber;
        this.content = content;
        this.format = format;
        this.status = MessageStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public void setDestinationNumber(String destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }
}
