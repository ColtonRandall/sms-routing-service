package com.smsrouting;

import com.smsrouting.dto.MessageRequest;
import com.smsrouting.model.Carrier;
import com.smsrouting.model.Message;
import com.smsrouting.model.MessageStatus;
import com.smsrouting.model.MessageType;
import com.smsrouting.repository.MessageRepository;
import com.smsrouting.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageServiceTests {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository repository;

    private MessageRequest buildRequest(String destinationNumber, String content, MessageType format) {
        MessageRequest request = new MessageRequest();
        request.setDestinationNumber(destinationNumber);
        request.setContent(content);
        request.setFormat(format);
        return request;
    }

    @Test
    void testSendToValidAustralianNumber() {
        Message result = messageService.sendMessage(buildRequest("+61123456789", "Test AU message", MessageType.SMS));

        assertTrue(result.getCarrier() == Carrier.TELSTRA || result.getCarrier() == Carrier.OPTUS);
        assertEquals(MessageStatus.DELIVERED, result.getStatus());
    }

    @Test
    void testSendToValidNZNumber() {
        Message result = messageService.sendMessage(buildRequest("+64123456789", "Test NZ message", MessageType.SMS));

        assertEquals(Carrier.SPARK, result.getCarrier());
        assertEquals(MessageStatus.DELIVERED, result.getStatus());
    }

    @Test
    void testSendToOptedOutNumber() {
        String phoneNumber = "+61412345678";

        repository.optOut(phoneNumber);
        Message result = messageService.sendMessage(buildRequest(phoneNumber, "Test opted out number", MessageType.SMS));

        assertEquals(MessageStatus.BLOCKED, result.getStatus());
    }

    @Test
    void testSendToInvalidPhoneNumber() {
        assertThrows(IllegalArgumentException.class, () ->
                messageService.sendMessage(buildRequest("123456789", "Test message", MessageType.SMS)));
    }

    @Test
    void testSendToPhoneNumberMissingCountryCode() {
        assertThrows(IllegalArgumentException.class, () ->
                messageService.sendMessage(buildRequest("0412345678", "Test message", MessageType.SMS)));
    }

    @Test
    void testSendEmptyMessageContent() {
        Message result = messageService.sendMessage(buildRequest("+61123456789", "", MessageType.SMS));

        assertEquals(MessageStatus.DELIVERED, result.getStatus());
        assertEquals("", result.getContent());
    }

    @Test
    void testSendNullMessageContent() {
        Message result = messageService.sendMessage(buildRequest("+61123456789", null, MessageType.SMS));

        assertEquals(MessageStatus.DELIVERED, result.getStatus());
        assertNull(result.getContent());
    }

    @Test
    void testSendMMSMessage() {
        Message result = messageService.sendMessage(buildRequest("+61123456789", "Test MMS message", MessageType.MMS));

        assertEquals(MessageStatus.DELIVERED, result.getStatus());
        assertEquals(MessageType.MMS, result.getFormat());
    }
}
