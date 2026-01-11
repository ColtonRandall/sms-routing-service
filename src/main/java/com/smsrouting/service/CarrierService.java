package com.smsrouting.service;

import com.smsrouting.model.Carrier;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CarrierService {

    private static final String AU_PREFIX = "+61";
    private static final String NZ_PREFIX = "+64";

    private final AtomicBoolean useTelstra = new AtomicBoolean(true); // maintains thread-safety

    public Carrier determineCarrier(String phoneNumber){
        String standardisedNumber = standardiseNumber(phoneNumber);
        if (standardisedNumber.startsWith(AU_PREFIX)){
            return useTelstra.getAndSet(!useTelstra.get()) ? Carrier.TELSTRA : Carrier.OPTUS; // alternate telstra
            // and optus
        } else if (standardisedNumber.startsWith(NZ_PREFIX)){
            return Carrier.SPARK;
        } else {
            return Carrier.GLOBAL;
        }
    }

    public boolean isValidPhoneNumber(String phoneNumber){
        if(phoneNumber == null || phoneNumber.isEmpty()){
            return false;
        }

        // check for letters before standardising the number
        if(phoneNumber.matches(".*[a-zA-Z].*")){
            return false;
        }

        String standardisedNumber = standardiseNumber(phoneNumber);

        boolean hasValidPrefix = standardisedNumber.startsWith("+");
        boolean containsOnlyValidCharacters = standardisedNumber.matches("\\+[0-9]+");
        boolean hasValidLength = standardisedNumber.length() >= 9 && standardisedNumber.length() <= 16; // includes the "+"
        // followed by 8-15 digits

        return hasValidPrefix && containsOnlyValidCharacters && hasValidLength;
    }

    public String standardiseNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[^0-9+]", "");
    }
}
