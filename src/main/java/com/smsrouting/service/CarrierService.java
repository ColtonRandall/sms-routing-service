package com.smsrouting.service;

import com.smsrouting.model.Carrier;
import org.springframework.stereotype.Service;

@Service
public class CarrierService {

    private static final String AU_PREFIX = "+61";
    private static final String NZ_PREFIX = "+61";

    public Carrier determineCarrier(String phoneNumber){
        if (phoneNumber.startsWith(AU_PREFIX)){
            return Carrier.AUSTRALIA;
        } else if (phoneNumber.startsWith(NZ_PREFIX)){
            return Carrier.NEW_ZEALAND;
        } else {
            return Carrier.GLOBAL;
        }
    }

    public boolean isValidPhoneNumber(String phoneNumber){

        if(phoneNumber == null || phoneNumber.isEmpty()){
            return false;
        }

        boolean hasValidPrefix = phoneNumber.startsWith("+");
        boolean containsOnlyValidCharacters = phoneNumber.matches("\\+[0-9]+");
        boolean hasValidLength = phoneNumber.length() >= 9 && phoneNumber.length() <= 16; // includes the "+"
        // followed by 8-15 digits

        return hasValidPrefix && containsOnlyValidCharacters && hasValidLength;
    }
}
