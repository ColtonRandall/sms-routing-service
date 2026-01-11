package com.smsrouting;

import com.smsrouting.model.Carrier;
import com.smsrouting.service.CarrierService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarrierServiceTests {

    private final CarrierService carrierService = new CarrierService();

    @Test
    void testDetermineCarrierForAustralianNumber() {
        // arrange & act
        Carrier result = carrierService.determineCarrier("+61123456789");

        // assert
        assertTrue(result == Carrier.TELSTRA || result == Carrier.OPTUS);
    }

    @Test
    void testDetermineCarrierForNZNumber() {
        // arrange & act
        Carrier result = carrierService.determineCarrier("+64412345678");

        // assert
        assertEquals(Carrier.SPARK, result);
    }

    @Test
    void testDetermineCarrierForOtherCountry() {
        // arrange & act
        Carrier result = carrierService.determineCarrier("+441234567890");

        // assert
        assertEquals(Carrier.GLOBAL, result);
    }

    @Test
    void testValidPhoneNumberWithPlusPrefix() {
        // arrange & act
        boolean result = carrierService.isValidPhoneNumber("+61123456789");

        // assert
        assertTrue(result);
    }

    @Test
    void testInvalidPhoneNumberMissingPrefix() {
        // arrange & act
        boolean result = carrierService.isValidPhoneNumber("61123456789");

        // assert
        assertFalse(result);
    }

    @Test
    void testInvalidPhoneNumberTooShort() {
        // arrange & act
        boolean result = carrierService.isValidPhoneNumber("+6112345");

        // assert
        assertFalse(result);
    }

    @Test
    void testInvalidPhoneNumberTooLong() {
        // arrange & act
        boolean result = carrierService.isValidPhoneNumber("+612345678901234567");

        // assert
        assertFalse(result);
    }

    @Test
    void testInvalidPhoneNumberWithLetters() {
        // arrange & act
        boolean result = carrierService.isValidPhoneNumber("+61abc123456");

        // assert
        assertFalse(result);
    }

    @Test
    void testInvalidPhoneNumberNull() {
        // arrange & act
        boolean result = carrierService.isValidPhoneNumber(null);

        // assert
        assertFalse(result);
    }

    @Test
    void testInvalidPhoneNumberEmpty() {
        // arrange & act
        boolean result = carrierService.isValidPhoneNumber("");

        // assert
        assertFalse(result);
    }
}
