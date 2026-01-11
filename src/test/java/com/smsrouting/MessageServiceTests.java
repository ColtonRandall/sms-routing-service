package com.smsrouting;

import com.smsrouting.model.Carrier;
import com.smsrouting.model.Message;
import com.smsrouting.model.MessageStatus;
import com.smsrouting.repository.MessageRepository;
import com.smsrouting.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MessageServiceTests {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository repository;

    @Test
    void testSendToValidAustralianNumber() {
        Message result = messageService.sendMessage("+61123456789", "Test AU message", "SMS");

        assertTrue(result.getCarrier() == Carrier.TELSTRA || result.getCarrier() == Carrier.OPTUS);
        assertEquals(MessageStatus.DELIVERED, result.getStatus());
    }

    @Test
    void testSendToValidNZNumber() {
        Message result = messageService.sendMessage("+64123456789", "Test NZ message", "SMS");

        assertEquals(Carrier.SPARK, result.getCarrier());
        assertEquals(MessageStatus.DELIVERED, result.getStatus());
    }

    @Test
    void testSendToOptedOutNumber() {
        String phoneNumber = "+61412345678";

        repository.optOut(phoneNumber);
        Message result = messageService.sendMessage(phoneNumber, "Test opted out number", "SMS");

        assertEquals(MessageStatus.BLOCKED, result.getStatus());
    }

    @Test
    void testSendToInvalidPhoneNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.sendMessage("123456789", "Test message", "SMS");
        });
    }

    @Test
    void testSendToPhoneNumberMissingCountryCode() {
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.sendMessage("0412345678", "Test message", "SMS");
        });
    }

    @Test
    void testSendEmptyMessageContent() {
        Message result = messageService.sendMessage("+61123456789", "", "SMS");

        assertEquals(MessageStatus.DELIVERED, result.getStatus());
        assertEquals("", result.getContent());
    }

    @Test
    void testSendNullMessageContent() {
        Message result = messageService.sendMessage("+61123456789", null, "SMS");

        assertEquals(MessageStatus.DELIVERED, result.getStatus());
        assertNull(result.getContent());
    }

    @Test
    void testSendMMSMessage() {
        Message result = messageService.sendMessage("+61123456789", "Test MMS message", "MMS");

        assertEquals(MessageStatus.DELIVERED, result.getStatus());
        assertEquals("MMS", result.getFormat());
    }
}
