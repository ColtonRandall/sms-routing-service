package com.smsrouting.dto;

import com.smsrouting.model.MessageType;
import jakarta.validation.constraints.NotBlank;

import static com.smsrouting.model.MessageType.SMS;

public class MessageRequest {

    @NotBlank
    private String destinationNumber;

    @NotBlank
    private String content;

    private MessageType format = SMS;
    private Long sendAt;

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

    public MessageType getFormat() {
        return format;
    }

    public void setFormat(MessageType format) {
        this.format = format;
    }

    public Long getSendAt() {
        return sendAt;
    }

    public void setSendAt(Long sendAt) {
        this.sendAt = sendAt;
    }
}
