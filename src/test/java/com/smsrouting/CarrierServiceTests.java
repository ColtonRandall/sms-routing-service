package com.smsrouting;

import com.smsrouting.model.Carrier;
import com.smsrouting.service.CarrierService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarrierServiceTests {

    private final CarrierService carrierService = new CarrierService();

    @Test
    void testDetermineCarrierForAustralianNumber() {
        Carrier result = carrierService.determineCarrier("+61123456789");
        assertTrue(result == Carrier.TELSTRA || result == Carrier.OPTUS);
    }

    @Test
    void testDetermineCarrierForNZNumber() {
        Carrier result = carrierService.determineCarrier("+64412345678");
        assertEquals(Carrier.SPARK, result);
    }

    @Test
    void testDetermineCarrierForOtherCountry() {
        Carrier result = carrierService.determineCarrier("+441234567890");
        assertEquals(Carrier.GLOBAL, result);
    }

    @Test
    void testValidPhoneNumberWithPlusPrefix() {
        boolean result = carrierService.isValidPhoneNumber("+61123456789");
        assertTrue(result);
    }

    @Test
    void testInvalidPhoneNumberMissingPrefix() {
        boolean result = carrierService.isValidPhoneNumber("61123456789");
        assertFalse(result);
    }

    @Test
    void testInvalidPhoneNumberTooShort() {
        boolean result = carrierService.isValidPhoneNumber("+6112345");
        assertFalse(result);
    }

    @Test
    void testInvalidPhoneNumberTooLong() {
        boolean result = carrierService.isValidPhoneNumber("+612345678901234567");
        assertFalse(result);
    }

    @Test
    void testInvalidPhoneNumberWithLetters() {
        boolean result = carrierService.isValidPhoneNumber("+61abc123456");
        assertFalse(result);
    }

    @Test
    void testInvalidPhoneNumberNull() {
        boolean result = carrierService.isValidPhoneNumber(null);
        assertFalse(result);
    }

    @Test
    void testInvalidPhoneNumberEmpty() {
        boolean result = carrierService.isValidPhoneNumber("");
        assertFalse(result);
    }

    @Test
    void testValidPhoneNumberWithSpaces() {
        boolean result = carrierService.isValidPhoneNumber("+64 123 456 789");
        assertTrue(result);
    }

    @Test
    void testValidPhoneNumberWithDashes() {
        boolean result = carrierService.isValidPhoneNumber("+64-123-456-789");
        assertTrue(result);
    }
}
