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

    // core tests
    @Test
    void testSendToValidAustralianNumber() {
        // arrange & act
        Message result = messageService.sendMessage("+61123456789", "Test AU message", "SMS");

        // assert
        assertTrue(result.getCarrier() == Carrier.TELSTRA || result.getCarrier() == Carrier.OPTUS);
        assertEquals(MessageStatus.SENT, result.getStatus());
    }

    @Test
    void testSendToValidNZNumber() {
        // arrange & act
        Message result = messageService.sendMessage("+64123456789", "Test NZ message", "SMS");

        // assert
        assertEquals(Carrier.SPARK, result.getCarrier());
        assertEquals(MessageStatus.SENT, result.getStatus());
    }

    @Test
    void testSendToOptedOutNumber() {
        // arrange
        String phoneNumber = "+61412345678";

        // act
        repository.optOut(phoneNumber);
        Message result = messageService.sendMessage(phoneNumber, "Test opted out number", "SMS");

        assertEquals(MessageStatus.BLOCKED, result.getStatus());
    }

    // edge cases
    @Test
    void testSendToInvalidPhoneNumber() {
        // arrange & act & assert
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.sendMessage("123456789", "Test message", "SMS");
        });
    }

    @Test
    void testSendToPhoneNumberMissingCountryCode() {
        // arrange & act & assert
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.sendMessage("0412345678", "Test message", "SMS");
        });
    }

    @Test
    void testSendEmptyMessageContent() {
        // arrange & act
        Message result = messageService.sendMessage("+61123456789", "", "SMS");

        // assert
        assertEquals(MessageStatus.SENT, result.getStatus());
        assertEquals("", result.getContent());
    }

    @Test
    void testSendNullMessageContent() {
        // arrange & act
        Message result = messageService.sendMessage("+61123456789", null, "SMS");

        // assert
        assertEquals(MessageStatus.SENT, result.getStatus());
        assertNull(result.getContent());
    }

    @Test
    void testSendMMSMessage() {
        // arrange & act
        Message result = messageService.sendMessage("+61123456789", "Test MMS message", "MMS");

        // assert
        assertEquals(MessageStatus.SENT, result.getStatus());
        assertEquals("MMS", result.getFormat());
    }
}
